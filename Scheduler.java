/**
 * @author Gregory Vincent
 */
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
public class Scheduler {
    //keep track of everything that has to be done and a reference to what we're currently doing
    private LinkedList<KernelandProcess> ProcessList;
    private KernelandProcess currentProcess;
    private Timer interruptTimer;

    public Scheduler(){
        this.ProcessList = new LinkedList<KernelandProcess>();
        this.interruptTimer = new Timer();
        interrupt t = new interrupt();
        //every process has a quantum of 250ms before it's interrupted
        interruptTimer.schedule(t, 250l);
    }

    private class interrupt extends TimerTask{
        @Override
        public void run(){
            SwitchProcess();
        }
    }

    public int CreateProcess(UserlandProcess up){
        KernelandProcess newProcess = new KernelandProcess(up);
        ProcessList.add(newProcess);
        
        //if there is no current process, switch to the first one in the process list
        if(currentProcess == null){
            SwitchProcess();
        }
        newProcess.setNextPID();
        //keep a reference to the first process in the list
        currentProcess = ProcessList.peekFirst();
        return newProcess.getPID();        
    }

    public void SwitchProcess(){
        if(currentProcess != null){
            currentProcess.stop();
            if(!currentProcess.isDone()){
                //send to the back of the list if we interrupted and it didn't finish
                ProcessList.remove(0);
                ProcessList.add(currentProcess);
            } else{
                //if it did finish, remove it from our list since it's done
                ProcessList.remove(0);
            }
        }
        //make sure we always keep a reference to the current process and run it 
        currentProcess = ProcessList.getFirst();
        currentProcess.run();
        ProcessList.removeFirst();
    }
}
