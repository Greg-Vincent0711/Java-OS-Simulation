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

    public static int CreateProcess(UserlandProcess up, PriorityLevel level){
        return Kernel.CreateProcess(up, level);
    }

    public static void Sleep(int miliseconds){
        Kernel.Sleep(miliseconds);
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
