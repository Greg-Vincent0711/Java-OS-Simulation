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
}
