public class GoodbyeWorld extends UserlandProcess {
    private String message;
    @Override
    public void run() {
        while(true){
            System.out.println(message);
            try{
                Thread.sleep(50);
            } catch (Exception e){}
        }
    } 
    public GoodbyeWorld(String message){
        this.message = message;
    } 
}
