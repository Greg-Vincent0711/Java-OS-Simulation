public class KernelandProcess{
    private static int nextPID = 1;
    private int pID;
    private boolean started = false;
    private Thread runner;
    
    public KernelandProcess(UserlandProcess up){
        started = false;
        runner = new Thread(up);
        pID = nextPID++;
    }

    void stop(){
        if(runner.isAlive()){
            runner.interrupt();
        }
    }

    boolean isDone(){
        return runner.isAlive() && started;
    }

    void run(){
        //don't restart what's already going
        if(started){
            runner.resume();
        }
        else{
            runner.start();
            started = true;
        }
    }

    public void setNextPID() {nextPID+=1;}

    public void setPID(int pID){this.pID = pID;}

    public int getPID() {return this.pID;}

    public boolean hasStarted(){return started;}
}
