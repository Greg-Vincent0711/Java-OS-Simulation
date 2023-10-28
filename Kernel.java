import java.util.HashMap;

public class Kernel implements Device {
    private Scheduler Scheduler;
    private VFS virtualFS = new VFS();
    //since we set the senderPID, we have to increment it 
    //creating a hashmap for waiting process
    private HashMap<Integer, KernelandProcess> waitList = new HashMap<>();
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
