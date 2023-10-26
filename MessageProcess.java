/**
 * @author Gregory Vincent
 * Object class represents a message
 */
public class MessageProcess extends UserlandProcess {
    public MessageProcess(){}
    // public MessageProcess(String message){this.message =  message;}

    @Override
    public void run() {
        KernelandProcess pingProc = new KernelandProcess(new HelloWorld());
        KernelandProcess pongProc = new KernelandProcess(new GoodbyeWorld());

        KernelMessage pingMessage = new KernelMessage(pingProc.getPID(), pongProc.getPID(), 0, null);
        KernelMessage pongMessage = new KernelMessage(pingProc.getPID(), pongProc.getPID(), 0, null);
        pingProc.addToQueue(pingMessage);        
        pongProc.addToQueue(pongMessage);        
        

    }
}

/**
 * to test this - use hello and goodbye world processes
 * 
 */
