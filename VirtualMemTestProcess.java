public class VirtualMemTestProcess extends UserlandProcess {
    public void run(){
        while(true){
            OS.CreateProcess(new UserlandProcess() {
                @Override
                public void run() {
                    int maxAllocate = OS.AllocateMemory(1024 * 100);
                    Write(maxAllocate, (byte)55);
                    while(true){
                        try{
                            Thread.sleep(100);
                        } catch (Exception e){}
                    }
    
                }
            });
        }
    }   
}