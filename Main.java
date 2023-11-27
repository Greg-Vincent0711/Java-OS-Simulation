
public class Main {
    public static void main(String[] args){
        /**
         * Each uncommented line runs a different test process
         */       
        // OS.Startup(new DemotionTestProcess());
        /**
         * Testing Sleeping Process
         */
        OS.Startup(new HelloWorld("background noise"));
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                OS.CreateProcess(new VirtualMemTestProcess());
                while(true){
                    try{
                        Thread.sleep(100);
                    } catch (Exception e){}
                }

            }
        });
    }
}
    