public class SleepTestProxy extends UserlandProcess {
    public void run() {
        // This is the actual SleepTest - every 240 ms we sleep, otherwise print awake. 
        System.out.println("Going to sleep");
        OS.Sleep(240); 
        while(true){
            System.out.println("I'm awake");
            try{
                Thread.sleep(50);
            } catch (Exception e){}
        }
        
    }
}
