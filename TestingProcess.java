/**
 * process used to test different functionality and provide some realistic background noise
 * for other processes that are running
 */
public class TestingProcess extends UserlandProcess {
        private String message;
        @Override
        public void run() {
            while(true){
                System.out.println(message);
                try{
                    Thread.sleep(240);
                } catch (Exception e){}
            }
        }
        public TestingProcess(String message){
            this.message = message;
        } 
    }
    
