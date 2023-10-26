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

    public KernelMessage(int senderPID, int targetPID, int messageSubject, byte [] message){
        this.senderPID = senderPID;
        this.targetPID = targetPID;
        this.message = message;
    }

    public void setSenderPID(int senderPID) {
        this.senderPID = senderPID;
    }

    public int getTargetPID(){
        return targetPID;
    }

    @Override
    public String toString(){
        return 
            "\nCurrent KernelMessage Object info " + 
            "\nCurrent Sender PID: " + Integer.toString(this.senderPID) + 
            "\nCurrent Target PID: " + Integer.toString(this.targetPID) +
            "\nMessage Subject: " + Integer.toString(this.messageSubject) +
            "\nMessage" + new String(this.message);
    }
}
