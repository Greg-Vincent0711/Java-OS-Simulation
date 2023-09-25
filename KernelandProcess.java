public class KernelandProcess {
    private static int nextPID = 1;
    private int pID;
    private boolean started = false;
    private Thread runner;
    private PriorityLevel priorityLevel;
    private long sleepTime;
    private int demotionCounter = 0;
    
    //default KLP constructor
    public KernelandProcess(UserlandProcess up){
        started = false;
        runner = new Thread(up);
        pID = nextPID++;
    }

    //prioritized
    public KernelandProcess(UserlandProcess up, PriorityLevel level){
        started = false;
        runner = new Thread(up);
        this.priorityLevel = level;
        pID = nextPID++;
    }


    void stop(){
        if(runner.isAlive()){
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
}
