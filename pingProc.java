public class pingProc extends UserlandProcess {
    @Override
    public void run() {
            while(true){
                System.out.println(OS.WaitForMessage().toString());
                OS.SendMessage(new KernelMessage(OS.getCurrentPID(),OS.getPIDByName("pingProc"), 1, "I am PONG".getBytes()));
            }
    }
    
}
