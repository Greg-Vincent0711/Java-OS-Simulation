public class pingProc extends UserlandProcess {
    @Override
    public void run() {
            while(true){
                OS.SendMessage(new KernelMessage(OS.getCurrentPID(),OS.getPIDByName("pongProc"), 0, "PING".getBytes()));
                try{
                    // adding a wait here let's messages print more clearly
                    Thread.sleep(240);
                    System.out.println(OS.WaitForMessage().toString());
                } catch (Exception e){}
            }
    }
    
}
