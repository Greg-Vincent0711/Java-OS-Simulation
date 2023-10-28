
public class pongProc extends UserlandProcess {
    public pongProc(){}
    @Override
    public void run() {
            while(true){
                OS.SendMessage(new KernelMessage(OS.getCurrentPID(),OS.getPIDByName("pingProc"), 0, "PONG".getBytes()));
                try{
                    //adding a wait here lets messages print more clearly
                    System.out.println(OS.WaitForMessage().toString());
                    Thread.sleep(240);
                } catch (Exception e){}
            }
    }
    
}
