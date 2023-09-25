public class Kernel {
    private Scheduler processScheduler;
    public Kernel() {
        this.processScheduler = new Scheduler();
    }
    public int CreateProcess(UserlandProcess up){
        return processScheduler.CreateProcess(up);
    }
}
