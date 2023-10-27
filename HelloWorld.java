public class HelloWorld extends UserlandProcess {
    String message;
    public HelloWorld(String message) {
        this.message = message;
    }
    public HelloWorld(){}
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
 