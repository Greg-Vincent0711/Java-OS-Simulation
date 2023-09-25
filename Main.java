public class Main {
    public static void main(String[] args){
        /**
         * uncomment this line to test the DemotionProcess, and see Testing.txt to see an output file detailing a run and the stats 
         * for each process
         */       
        // OS.Startup(new DemotionTestProcess());
        /**
         * Testing Sleeping Process
         */
        OS.Startup(new SleepTestProcess());
    }
}
    