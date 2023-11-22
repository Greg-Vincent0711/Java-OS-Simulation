import java.util.HashMap;
public class Kernel implements Device {
    private Scheduler Scheduler;
    private VFS virtualFS = new VFS();
    private int PAGE_SIZE = 1024;
    //creating a hashmap for waiting process
    private HashMap<Integer, KernelandProcess> waitList = new HashMap<>();
    private boolean [] usedMemoryList = new boolean[1024];
    public Kernel() {
        //scheduler takes a kernel reference on construction
        this.Scheduler = new Scheduler(this);
    }

    public int CreateProcess(UserlandProcess up){
        return Scheduler.CreateProcess(up);
    }

    public int CreateProcess(UserlandProcess up, PriorityLevel level){
        return Scheduler.CreateProcess(up, level);
    }

    public void Sleep(int milliseconds){
        Scheduler.Sleep(milliseconds);
    }

    public int getCurrentPID(){
        return Scheduler.getCurrentPID();
    }

    public KernelMessage WaitForMessage(){
        //if there's a message in the queue
        if(Scheduler.getCurrentlyRunning().queueLength() != 0){
            //pop it off the queue and return it
            return Scheduler.getCurrentlyRunning().queuePop();
        } else{
            //add the process to the wait list if there's no message in the queue
            waitList.put(Scheduler.getCurrentPID(), Scheduler.getCurrentlyRunning());
            KernelandProcess tempProcess = Scheduler.getCurrentlyRunning();
            //stop the current process
            Scheduler.makeCurrentProcNull();
            //find something new to run
            Scheduler.SwitchProcess();
            //after we call switchProcess, some new process will be able to run and have messages
            tempProcess.stop();
            return Scheduler.getCurrentlyRunning().queuePop();
        }
    }
    
        public void SendMessage(KernelMessage newMessage){
            //make a copy of the current message object
            KernelMessage messageToSend = new KernelMessage(newMessage);
            int targetPID = messageToSend.getTargetPID();
            //find the target process id and add the message to the queue
            if(waitList.containsKey(targetPID)){
                waitList.get(targetPID).addToQueue(messageToSend);
                //add the message to the target process' list of messages
                Scheduler.getProcessList(waitList.get(targetPID)).add(waitList.get(targetPID));
            } else{
                // even if the process isn't waiting send a message anyway after we check that it exists
                if(Scheduler.targetProcessMap.containsKey(targetPID)){
                    Scheduler.targetProcessMap.get(targetPID).addToQueue(messageToSend);
                } else{
                    System.out.println("Process doesn't exist");
                }
            }
        }

    public void killProcess(){
        Scheduler.killProcess();
    }

    public int getPID(){
        return Scheduler.getCurrentPID();
    }

    public int getPIDByName(String name){
        return Scheduler.getPIDByName(name);
    }

    @Override
    public int Open(String s) {
        KernelandProcess temp = Scheduler.getCurrentlyRunning();
        int space = temp.findSpace();
        if( space == -1){
            System.out.println("No empty space. KLP is full");
            return -1;
        } else{
            temp.setDeviceIndex(space, virtualFS.Open(s));
            return space;
        }

    }

    public KernelandProcess getCurrentlyRunning(){
        return Scheduler.getCurrentlyRunning();
    }

    //returns the start value address
    public int AllocateMemory(int size){
        KernelandProcess temp =  Scheduler.getCurrentlyRunning();
        // size is error checked by the OS
        int neededPageAmount = size / PAGE_SIZE;
        int [] currentProcessVirtualSpace = temp.getVirtualPagesArray();
        boolean enoughPages = false;
        // set to -1 since we don't know if there's any contiguous blocks of memory
        int startingAddress = -1;
        for(int firstPointer = 0; firstPointer < currentProcessVirtualSpace.length; firstPointer++){
            int currentFreePages = 0;
            //we find a free space
            if(currentProcessVirtualSpace[firstPointer] == -1){
                //iterate from the first free space to the last one we have
                for(int secondPointer = firstPointer; secondPointer < currentProcessVirtualSpace.length && currentFreePages < neededPageAmount; secondPointer++){
                    // keep track of the amount of free spaces we have
                    if(currentProcessVirtualSpace[secondPointer] == -1){
                        currentFreePages++;
                    }
                    if(currentFreePages == neededPageAmount){
                        startingAddress = firstPointer;
                        // at this point, stop finding space and move onto allocation
                        enoughPages = true;
                    }
                    // if we end up in a space that is occupied, jump over it and keep searching
                    if(currentProcessVirtualSpace[secondPointer] != -1){
                        firstPointer = secondPointer;
                        break;
                    }
                }
            }

            if(enoughPages){
                break;
            }
        }
        
        if(startingAddress == -1){
            System.out.println("Couldn't find a starting page.");
            return -1;
        }

        //after we confirm we have enough space what's needed virtually, confirm this with the physical memory
        int [] neededPhysicalPages = new int[neededPageAmount];
        int count = 0;
        for(int i = 0; i < usedMemoryList.length; i++){
            // find unused indexes within the free index array until we have as much as we need
            if(!usedMemoryList[i]){
                neededPhysicalPages[count] = i;
                count++;
            }
            if(count == neededPageAmount){
                break;
            }
        }
        if(count != neededPageAmount){
            System.out.println("Not enough physical memory for allocation");
            return -1;
        }
        // since we have enough physical and virtual memory, allocate here
        for(int i = 0; i < neededPhysicalPages.length; i++){
            //perform the mapping between virtual and physical addresses
            currentProcessVirtualSpace[startingAddress + i] = neededPhysicalPages[i];
            usedMemoryList[neededPhysicalPages[i]] = true;
        }
        /**
         * returned starting address needs to be inline with how memory is accessed
         */
        return startingAddress * 1024;
    }

    /**
     * @param startingAddress / 1024 returns the page to free from
     * @return
     */
    public boolean FreeMemory(int startingAddress, int size){
        int currentPage = startingAddress / 1024;
        int [] currentProcVirtualMem = Scheduler.getCurrentlyRunning().getVirtualPagesArray();
        for(int index = currentPage; index < currentProcVirtualMem.length; index++){ 
            int virualMemoryBlock = currentProcVirtualMem[index];
            if(virualMemoryBlock != -1){
                //erase the memory from the physical address mapping
                usedMemoryList[virualMemoryBlock] = false;
                //erase the virtual memory 
                currentProcVirtualMem[index] = -1;
            }
        }
        return true;
    }   
        
    //all functions below return a call from the equivalent VFS fn
    @Override
    public void Close(int id) {
        //get the current process, which holds different devices
        KernelandProcess temp = Scheduler.getCurrentlyRunning();
        //close the specified device
        virtualFS.Close(temp.getDeviceAtIndex(id));
        temp.setDeviceIndex(id, -1);    
    }

    @Override
    public byte[] Read(int id, int size) {
        KernelandProcess temp = Scheduler.getCurrentlyRunning();
        return virtualFS.Read(temp.getDeviceAtIndex(id), size);    
    }

    @Override
    public void Seek(int id, int to) {
        KernelandProcess temp = Scheduler.getCurrentlyRunning();
        virtualFS.Seek(temp.getDeviceAtIndex(id), to);     
    }

    @Override
    public int Write(int id, byte[] data) {
        KernelandProcess temp = Scheduler.getCurrentlyRunning();
        return virtualFS.Write(temp.getDeviceAtIndex(id), data);
    }
}
