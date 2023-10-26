public class Main {
    public static void main(String[] args){
        /**
         * Each uncommented line runs a different test process
         */       
        // OS.Startup(new DemotionTestProcess());
        /**
         * Testing Sleeping Process
         */
        // OS.Startup(new SleepTestProcess());
        /**
         * Testing Devices
         */
        // OS.Startup(new DeviceTestProcess());
        OS.Startup(new MessageProcess());
    }
}
    