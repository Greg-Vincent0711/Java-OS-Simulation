/**
 * @author Gregory Vincent
 */
import java.util.*;
import java.time.Clock;



public class Scheduler {    
    //different priority lists we may have
    private List<KernelandProcess> BackgroundProcesses;
    private List<KernelandProcess> realTimeProcesses;
    private List<KernelandProcess> InteractiveProcesses;
    private List<KernelandProcess> SleepingProcesses;

    private KernelandProcess currentProcess;
    private Timer interruptTimer;
    private Kernel kernelReference;
    private Clock currentTime = Clock.systemDefaultZone();
    //needed for SendMessage()
    public HashMap<Integer, KernelandProcess> targetProcessMap = new HashMap<>();
    private HashMap<String, KernelandProcess> nameMap = new HashMap<>();
    

    public Scheduler(Kernel parameterKernel){
        // all priority queues needed to be synced with each other
        this.BackgroundProcesses = Collections.synchronizedList(new LinkedList<KernelandProcess>());
        this.realTimeProcesses = Collections.synchronizedList(new LinkedList<KernelandProcess>());
        this.InteractiveProcesses = Collections.synchronizedList(new LinkedList<KernelandProcess>());
        this.SleepingProcesses = Collections.synchronizedList(new LinkedList<KernelandProcess>());

        this.kernelReference = parameterKernel;
        this.interruptTimer = new Timer();
        //every process has a quantum of 250ms before it's interrupted
        interruptTimer.schedule(new interrupt(), 250, 250);
    }

    public int getCurrentPID(){
        return currentProcess.getPID();
    }


    public int getPIDByName(String name){
        return nameMap.containsKey(name) ? nameMap.get(name).getPID() : -1;
    }

    private class interrupt extends TimerTask{
        @Override
        public void run(){
            SwitchProcess();
        }
    }

    public KernelandProcess getCurrentlyRunning(){
        return currentProcess;
    }

    private void closeAll(){
        //for all the devices in the current process
        for(int i = 0; i < currentProcess.getDeviceListSize(); i++){
            if(currentProcess.getDeviceAtIndex(i) != -1){
                //call close on the respective device
                kernelReference.Close(currentProcess.getDeviceAtIndex(i));
                //set its index to -1 - meaning it is open for a new device
                currentProcess.setDeviceIndex(i, -1);            
            }
        }
    }


    //returns the correct list for a process based on priority
    public List<KernelandProcess> getProcessList(KernelandProcess proc){
        switch(proc.getPriorityLevel()){
            case REAL_TIME:
                return this.realTimeProcesses;
            case INTERACTIVE:
                return this.InteractiveProcesses;
            default:
                return this.BackgroundProcesses;
        }
    }

    //default create process to the interactive list
    public int CreateProcess(UserlandProcess up){
        KernelandProcess newProcess = new KernelandProcess(up, PriorityLevel.INTERACTIVE);
        InteractiveProcesses.add(newProcess);
        if(currentProcess == null){
            SwitchProcess();
        }
        nameMap.put(newProcess.getProcessName(), newProcess);
        targetProcessMap.put(newProcess.getPID(), newProcess);
        return newProcess.getPID();        
    }
    
    //priority-based create process
    public int CreateProcess(UserlandProcess up, PriorityLevel priorityLevel){
        KernelandProcess newProcess = new KernelandProcess(up, priorityLevel);
        nameMap.put(newProcess.getProcessName(), newProcess);
        targetProcessMap.put(newProcess.getPID(), newProcess);
        switch(priorityLevel){
            case REAL_TIME:
                realTimeProcesses.add(newProcess);
                break;
            case INTERACTIVE:
                InteractiveProcesses.add(newProcess);
                break;
            default:
                BackgroundProcesses.add(newProcess);
                break;
            }
        if(currentProcess == null){
            SwitchProcess();
        }
        return newProcess.getPID();
    }



    public void makeCurrentProcNull(){
        currentProcess = null;
    }


    public void killProcess(){
        KernelandProcess tempCurrent = getCurrentlyRunning();
        if(tempCurrent != null){
            // terminate all of the processes devices
            closeAll();
            // remove it from all lists that track it
            targetProcessMap.remove(currentProcess.getPID());
            nameMap.remove(currentProcess.getProcessName());
            tempCurrent = currentProcess;
            
            currentProcess = null;
            //find something new to run
            SwitchProcess();
            tempCurrent.stop();
        }
    }

    public void Sleep(int milliseconds){
        KernelandProcess temp = currentProcess;
        currentProcess = null;
        
        //used for sorting the sleeping process list by process sleeptime
        temp.setSleepTime(currentTime.millis() + milliseconds);
        temp.setDemotionCount(0);
        SleepingProcesses.add(temp);
        SwitchProcess();
        temp.stop();
    }

    public void SwitchProcess(){
        if(!SleepingProcesses.isEmpty()){
            for(KernelandProcess process : SleepingProcesses){
                //wake up the ones that have passed the correct sleep time
                if(process.getSleepTime() >= currentTime.millis()){
                    switch(process.getPriorityLevel()){
                        case BACKGROUND:
                            BackgroundProcesses.add(process);
                            break;
                        case INTERACTIVE:
                            InteractiveProcesses.add(process);
                            break;
                        default:
                            realTimeProcesses.add(process);
                            break;
                    }
                }
        }
        }
        //if we have a current process
        if(currentProcess != null){
            currentProcess.stop();
            if(!currentProcess.isDone()){
                //increment it's demotion count since we had to interrupt it. used too much of it's quantum
                currentProcess.setDemotionCount(currentProcess.getDemotionCount() + 1);
                if(currentProcess.getDemotionCount() > 5){
                    //Demote the process and it's respective priority level
                    switch(currentProcess.getPriorityLevel()){
                        case REAL_TIME:
                            currentProcess.setPriorityLevel(PriorityLevel.INTERACTIVE);
                            currentProcess.setDemotionCount(0);
                            break;
                        case INTERACTIVE:
                            currentProcess.setPriorityLevel(PriorityLevel.BACKGROUND);
                            currentProcess.setDemotionCount(0);
                            break;
                        default:
                            break;       
                    }
                }
                //if the current process isn't done and can't be demoted yet but is interrupted:
                KernelandProcess temp = currentProcess;
                getProcessList(temp).add(temp);

            //if it's done, remove it from the list entirely
            }else{
                //reset current process, close all devices attached to it
                // and remove it's mappings
                closeAll();
                targetProcessMap.remove(currentProcess.getPID());
                nameMap.remove(currentProcess.getProcessName());
                currentProcess = null;
                
            }
        } 
        // find something to run
        prioritizeAndRun();
    }


    //probabilistic model to find a process from the correct list and run it
    private void prioritizeAndRun(){
        int priorityFinder;
        //if we have real time processes
        if(!realTimeProcesses.isEmpty()){
            //use the 6(realtime) 3(interactive) 1(background) probabilistic model
            priorityFinder = new Random(currentTime.millis()).nextInt(10); 
                if(priorityFinder <= 5){
                    currentProcess = realTimeProcesses.get(0);
                    currentProcess.run();
                    realTimeProcesses.remove(0);
                //if we need to grab an interactive process and the interactive queue isn't empty
                } else if(!InteractiveProcesses.isEmpty() && priorityFinder <= 8){
                    currentProcess = InteractiveProcesses.get(0);
                    currentProcess.run();
                    InteractiveProcesses.remove(0);
                // if we need to grab a background process and the background queue isn't empty  
                } else if(!BackgroundProcesses.isEmpty() && priorityFinder == 9){
                    currentProcess = BackgroundProcesses.get(0);
                    currentProcess.run();
                    BackgroundProcesses.remove(0);
                //if both of those are empty, default to the non-empty real-time list and run that
                } else{
                    currentProcess = realTimeProcesses.get(0);
                    currentProcess.run();
                    realTimeProcesses.remove(0);
                }
        } else{
            //if we have no real time processes, use the 3(interactive) 1(background) probabilistic model
            priorityFinder = new Random(currentTime.millis()).nextInt(4); 
            //first two else if statements follow the same logic as the 6 3 1 model
            if(!InteractiveProcesses.isEmpty() && priorityFinder <= 3){
                currentProcess = InteractiveProcesses.get(0);
                currentProcess.run();
                InteractiveProcesses.remove(0);
            } else if(!BackgroundProcesses.isEmpty() && priorityFinder == 4){
                currentProcess = BackgroundProcesses.get(0);
                currentProcess.run();
                BackgroundProcesses.remove(0);
            //default to background if we have no interactive processes
            } else if(InteractiveProcesses.isEmpty()){
                currentProcess = BackgroundProcesses.get(0);
                currentProcess.run();
                BackgroundProcesses.remove(0);
            //default to interactive if we have no background processes
            } else if(BackgroundProcesses.isEmpty()){
                currentProcess = InteractiveProcesses.get(0);
                currentProcess.run();
                InteractiveProcesses.remove(0);
            }
        }
    }
}
