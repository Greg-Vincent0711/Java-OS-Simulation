import java.util.LinkedList;
public class KernelandProcess {
    private static int nextPID = 1;
    private int pID;
    private boolean started = false;
    private Thread runner;
    private PriorityLevel priorityLevel;
    private long sleepTime;
    private int demotionCounter = 0;
    private int[] deviceList = new int[10];
    private String name;

    private LinkedList<KernelMessage> messageQueue = new LinkedList<KernelMessage>();

    //default KLP constructor
    public KernelandProcess(UserlandProcess up){
        started = false;
        runner = new Thread(up);
        pID = nextPID++;
        for(int i = 0; i < deviceList.length; i++){
            deviceList[i] = -1;
        }
        name = up.getClass().getName();
    }



    //prioritized
    public KernelandProcess(UserlandProcess up, PriorityLevel level){
        started = false;
        runner = new Thread(up);
        this.priorityLevel = level;
        pID = nextPID++;
        for(int i = 0; i < deviceList.length; i++){
            deviceList[i] = -1;
        }
    }


    //needed to add a new device to the list
    public int findSpace(){
        for(int i = 0; i < deviceList.length; i++){
            if(deviceList[i] == -1){
                return i;
            }
        }
        //return -1 as an error if nothing is free
        return -1;
    }

    @SuppressWarnings("deprecated")
    void stop(){
        if(started){
            runner.suspend();
        }
    }

    boolean isDone(){
        return !runner.isAlive() && started;
    }

    void run(){
        //don't restart what's already going
        if(started){
            runner.resume();
        }
        else{
            started = true;
            runner.start();
            
        }
    }

    public void setNextPID(){
        nextPID+=1;
    }

    public void setPID(int pID){
        this.pID = pID;
    }

    public int getPID(){
        return this.pID;
    }

    public boolean hasStarted(){
        return started;
    }

    public int getDemotionCount(){
        return demotionCounter;
    }

    public void setDemotionCount(int demotionCounter){
        this.demotionCounter = demotionCounter;
    }

    public PriorityLevel getPriorityLevel(){
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel){
        this.priorityLevel = priorityLevel;
    }

    public long getSleepTime(){
        return sleepTime;
    }

    public void setSleepTime(long sleepTime){
        this.sleepTime = sleepTime;
    }

    public void setDeviceIndex(int index, int id){
        deviceList[index] = id;
    }

    public int getDeviceAtIndex(int index){
        return deviceList[index];
    }

    public int getDeviceListSize(){
        return deviceList.length;
    }

    public String getProcessName(){
        return this.name;
    }

    public void addToQueue(KernelMessage mess){
        messageQueue.add(mess);
    }

    public int queueLength(){
        return messageQueue.size();
    }

    public KernelMessage queuePop(){
        return messageQueue.pollFirst();
    }
}
