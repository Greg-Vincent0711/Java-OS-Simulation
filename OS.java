
import java.util.Random;
public class OS {
    private static Kernel kernelRef;
    private static Random rand = new Random(System.currentTimeMillis());
    private static int PAGE_SIZE = 1024;
    public static void Startup(UserlandProcess init){
        kernelRef = new Kernel();
        CreateProcess(init);
    }
    
    //default create
    public static int CreateProcess(UserlandProcess up){
        return kernelRef.CreateProcess(up);
    }

    //initialize the kernel, create an interactive process, start
    public static int CreateProcess(UserlandProcess up, PriorityLevel level){
        return kernelRef.CreateProcess(up, level);
    }
    
    public static int getCurrentPID(){
        return kernelRef.getCurrentPID();
    }
    
    public static void SendMessage(KernelMessage messageToSend){
        kernelRef.SendMessage(messageToSend);
    }

    public static KernelMessage WaitForMessage(){
        return kernelRef.WaitForMessage();
    }

    public static int getPID(){
        return kernelRef.getPID();
    }

    public static int getPIDByName(String name){
        return kernelRef.getPIDByName(name);
    }

    public static void Sleep(int milliseconds){
        kernelRef.Sleep(milliseconds);
    }
    //userland calls reference kerneland calls
    public static int Open(String name){
        return kernelRef.Open(name);
    }
    public static byte[] Read(int id, int size){
        return kernelRef.Read(id, size);
    }
    public static int Write(int id, byte[] data){
        return kernelRef.Write(id, data);
    }
    public static void Seek(int id, int size){
        kernelRef.Seek(id, size);
    }
    public static void Close(int id){
        kernelRef.Close(id);
    }

    public static int AllocateMemory(int size){
        if(size % 1024 == 0){
            return kernelRef.AllocateMemory(size);
        } 
        else {
            System.out.println("Invalid parameter for memory allocation.");
            return -1;
        }
        
    }

    public static boolean FreeMemory(int startingAddress, int size){
        if(startingAddress == -1){
            System.out.println("There was a problem allocating memory.");
            return false;
        }
        else if(startingAddress % PAGE_SIZE != 0 || size % PAGE_SIZE != 0){
            System.out.println("Invalid starting address or size for freeing memory");
            return false;
        } else{
            return kernelRef.FreeMemory(startingAddress, size);
        }
    }

    public static void getMapping(int virtualPageNumber){
        if(virtualPageNumber < 0 || virtualPageNumber > 99){
            System.out.println("Invalid parameter.");
        } else{
            KernelandProcess currentProc = kernelRef.getCurrentlyRunning();
            int pageLocation = currentProc.getVirtualPageLocation(virtualPageNumber);
            if(pageLocation != -1){
                int num = rand.nextInt(2);
                UserlandProcess.TLB[num][0] = virtualPageNumber;
                UserlandProcess.TLB[num][1] = pageLocation;
            } else{
                /**
                 * if there's no page for the current process
                 * kill it so we aren't stuck in an infinite loop
                */
                kernelRef.killProcess();
            }
            
        }
    }
}
