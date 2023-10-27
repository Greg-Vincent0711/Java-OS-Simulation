
public class pongProc extends UserlandProcess {
    public pongProc(){}
    @Override
    public void run() {
            while(true){
                System.out.println(OS.WaitForMessage().toString());
                OS.SendMessage(new KernelMessage(OS.getCurrentPID(),OS.getPIDByName("pingProc"), 1, "I am PONG".getBytes()));
            }
    }
    
}
