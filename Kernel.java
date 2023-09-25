public class Kernel {
    private Scheduler Scheduler;
    public Kernel() {
        this.Scheduler = new Scheduler();
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
}
