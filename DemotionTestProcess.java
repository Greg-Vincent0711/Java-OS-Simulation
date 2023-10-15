/**
 * The way this test works:
 * Overtime the output of this file should smoothen out
 * Eventually, the user should see a smooth output of all the different processes as they're all demoted to background if this runs long enough
 * In Testing.txt there's a sample output explaning how much each process type appears
 *  which should be somewhat close to the probabilistic model.
 */
public class DemotionTestProcess extends UserlandProcess {
    @Override
    public void run() {
        OS.CreateProcess(new TestingProcess("realtime one"), PriorityLevel.REAL_TIME);
        OS.CreateProcess(new TestingProcess("realtime two"), PriorityLevel.REAL_TIME);
        OS.CreateProcess(new TestingProcess("realtime three"), PriorityLevel.REAL_TIME);
        OS.CreateProcess(new TestingProcess("realtime four"), PriorityLevel.REAL_TIME);
        OS.CreateProcess(new TestingProcess("realtime six"), PriorityLevel.REAL_TIME);
        OS.CreateProcess(new TestingProcess("realtime seven"), PriorityLevel.REAL_TIME);
        OS.CreateProcess(new TestingProcess("realtime eight"), PriorityLevel.REAL_TIME);
        OS.CreateProcess(new TestingProcess("realtime nine"), PriorityLevel.REAL_TIME);
        //Once realtime process nine runs, and repeats 8 times, all real time processes have been ran

        OS.CreateProcess(new TestingProcess("interactive one"), PriorityLevel.INTERACTIVE);
        OS.CreateProcess(new TestingProcess("interactive two"), PriorityLevel.INTERACTIVE);
        OS.CreateProcess(new TestingProcess("interactive three"), PriorityLevel.INTERACTIVE);
        OS.CreateProcess(new TestingProcess("interactive four"), PriorityLevel.INTERACTIVE);
        OS.CreateProcess(new TestingProcess("interactive five"), PriorityLevel.INTERACTIVE);
        OS.CreateProcess(new TestingProcess("interactive six"), PriorityLevel.INTERACTIVE);
        OS.CreateProcess(new TestingProcess("interactive seven"), PriorityLevel.INTERACTIVE);
        OS.CreateProcess(new TestingProcess("interactive eight"), PriorityLevel.INTERACTIVE);
        OS.CreateProcess(new TestingProcess("interactive nine"), PriorityLevel.INTERACTIVE);
        //Once interactive process nine runs, and repeats 8 times, all interactive time processes have been ran

        //Don't expect to see too many background processes since they appear so little
        OS.CreateProcess(new TestingProcess("background one"), PriorityLevel.BACKGROUND);
        OS.CreateProcess(new TestingProcess("background two"), PriorityLevel.BACKGROUND);
        OS.CreateProcess(new TestingProcess("background three"), PriorityLevel.BACKGROUND);
        OS.CreateProcess(new TestingProcess("background four"), PriorityLevel.BACKGROUND);
        OS.CreateProcess(new TestingProcess("background five"), PriorityLevel.BACKGROUND);
        OS.CreateProcess(new TestingProcess("background six"), PriorityLevel.BACKGROUND);
        OS.CreateProcess(new TestingProcess("background seven"), PriorityLevel.BACKGROUND);
        OS.CreateProcess(new TestingProcess("background eight"), PriorityLevel.BACKGROUND);
        OS.CreateProcess(new TestingProcess("background nine"), PriorityLevel.BACKGROUND);
        
    }
    
}
