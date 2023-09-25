public class OS {
    private static Kernel OSKernel;
    //initialize the kernel and then start a process
    public static void Startup(UserlandProcess init){
        OSKernel = new Kernel();
        CreateProcess(init);
    }
    
    public static int CreateProcess(UserlandProcess up){
        //OS calls kernel createProcess
        return OSKernel.CreateProcess(up);
    }
}
