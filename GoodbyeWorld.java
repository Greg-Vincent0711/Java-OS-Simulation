public class GoodbyeWorld extends UserlandProcess {
    private String message;
    private int count = 0;
    @Override
    public void run() {
        while(true){
            System.out.println("I am PONG" + count++);
            try{
                Thread.sleep(50);
            } catch (Exception e){}
        }
    } 
    public GoodbyeWorld(String message){
        this.message = message;
    } 
    public GoodbyeWorld(){}
}
