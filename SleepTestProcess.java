/**Testing sleep funcitonality. TestingProcess is background noise to show the sleep cycle working */
public class SleepTestProcess extends UserlandProcess {
    public void run() {
        OS.CreateProcess(new TestingProcess("Background noise while testing Sleep"), PriorityLevel.BACKGROUND);
        OS.CreateProcess(new SleepTestProxy(), PriorityLevel.BACKGROUND);
    }

}

