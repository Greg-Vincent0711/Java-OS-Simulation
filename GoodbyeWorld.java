public class GoodbyeWorld extends UserlandProcess {
    String message;
    public GoodbyeWorld(String message) {
        this.message = message;
    }
    public GoodbyeWorld(){}
    
    @Override
    public void run() {
        while(true){
            System.out.println(message);
            try{
                Thread.sleep(50);
            } catch (Exception e){}
        }
    } 
}
 