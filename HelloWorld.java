public class HelloWorld extends UserlandProcess {
    int count = 0;
    @Override
    public void run() {
        while(true){
            // System.out.println("I am PING");
            try{
                Thread.sleep(50);
            } catch (Exception e){}
        }
    } 
}
 