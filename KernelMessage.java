public class KernelMessage {
    private int senderPID;
    private int targetPID;
    private int messageSubject;
    private byte [] message;


    public KernelMessage(KernelMessage KM ){
        this.senderPID = KM.senderPID;
        this.targetPID = KM.targetPID;
        this.messageSubject = KM.messageSubject;
        this.message = KM.message;
    }

    @Override
    public String toString(){
        return 
            "Debug Info " + 
            "Current Sender PID: " + Integer.toString(this.senderPID) + 
            "Current Target PID: " + Integer.toString(this.targetPID) +
            "Message Subject: " + Integer.toString(this.messageSubject) +
            "Message" + new String(this.message);
    }

    public void setSenderPID(int senderPID) {
        this.senderPID = senderPID;
    }
}
