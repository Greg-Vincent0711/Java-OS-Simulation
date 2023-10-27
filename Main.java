
public class Main {
    public static void main(String[] args){
        /**
         * Each uncommented line runs a different test process
         */       
        // OS.Startup(new DemotionTestProcess());
        /**
         * Testing Sleeping Process
         */
        OS.Startup(new SleepTestProcess());
        /**
         * Testing Devices
         */
        OS.Startup(new UserlandProcess() {
            @Override
            public void run() {
                OS.CreateProcess(new pingProc());
                OS.CreateProcess(new pongProc());

                OS.CreateProcess(new UserlandProcess() {
                    @Override
                    public void run() {
                        while(true) {
                            try {
                                Thread.sleep(40);
                            }
                            catch (Exception e) {}
                        }
                    }
                });
            }
        });
    }
}
    