public class Kernel implements Device {
    private Scheduler Scheduler;
    private VFS virtualFS = new VFS();
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
