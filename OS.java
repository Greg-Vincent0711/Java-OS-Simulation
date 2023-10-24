public class OS {
    private static Kernel Kernel;
    //initialize the kernel, create an interactive process, start
    public static void Startup(UserlandProcess init){
        Kernel = new Kernel();
        CreateProcess(init);
    }

    //default create
    public static int CreateProcess(UserlandProcess up){
        return Kernel.CreateProcess(up);
    }

    public static int getCurrentPID(){
        return Kernel.getCurrentPID();
    }

    public static int CreateProcess(UserlandProcess up, PriorityLevel level){
        return Kernel.CreateProcess(up, level);
    }

    public static void SendMessage(KernelMessage messageToSend){
        Kernel.SendMessage(messageToSend);
    }


    public static int getPID(){
        return Kernel.getPID();
    }

    public static int getPIDbyName(String name){
        return Kernel.getPIDByName(name);
    }

    public static void Sleep(int milliseconds){
        Kernel.Sleep(milliseconds);
    }
    //userland calls reference kerneland calls
    public static int Open(String name){
        return Kernel.Open(name);
    }
    public static byte[] Read(int id, int size){
        return Kernel.Read(id, size);
    }
    public static int Write(int id, byte[] data){
        return Kernel.Write(id, data);
    }
    public static void Seek(int id, int size){
        Kernel.Seek(id, size);
    }
    public static void Close(int id){
        Kernel.Close(id);
    }
}
