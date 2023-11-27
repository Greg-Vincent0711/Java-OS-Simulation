
import java.util.Random;

public class OS {
    private static Kernel kernelRef;
    private static Random rand = new Random(System.currentTimeMillis());
    private static int PAGE_SIZE = 1024;
    private static int fakeFileDescriptor = 0;
    private static int currentDiskPage = 0;
    public static void Startup(UserlandProcess init) {
        kernelRef = new Kernel();
        fakeFileDescriptor = kernelRef.getFFS().Open("swapFile.txt");
        CreateProcess(init);
    }

    // default create
    public static int CreateProcess(UserlandProcess up) {
        return kernelRef.CreateProcess(up);
    }

    // initialize the kernel, create an interactive process, start
    public static int CreateProcess(UserlandProcess up, PriorityLevel level) {
        return kernelRef.CreateProcess(up, level);
    }

    public static int getCurrentPID() {
        return kernelRef.getCurrentPID();
    }

    public static void SendMessage(KernelMessage messageToSend) {
        kernelRef.SendMessage(messageToSend);
    }

    public static KernelMessage WaitForMessage() {
        return kernelRef.WaitForMessage();
    }

    public static int getPID() {
        return kernelRef.getPID();
    }

    public static int getPIDByName(String name) {
        return kernelRef.getPIDByName(name);
    }

    public static void Sleep(int milliseconds) {
        kernelRef.Sleep(milliseconds);
    }

    // userland calls reference kerneland calls
    public static int Open(String name) {
        return kernelRef.Open(name);
    }

    public static byte[] Read(int id, int size) {
        return kernelRef.Read(id, size);
    }

    public static int Write(int id, byte[] data) {
        return kernelRef.Write(id, data);
    }

    public static void Seek(int id, int size) {
        kernelRef.Seek(id, size);
    }

    public static void Close(int id) {
        kernelRef.Close(id);
    }

    public static int AllocateMemory(int size) {
        if (size % 1024 != 0) {
            System.out.println("Invalid parameter for memory allocation.");
            return -1;
        } else {
            return kernelRef.AllocateMemory(size);
        }
    }

    public static boolean FreeMemory(int startingAddress, int size) {
        if (startingAddress == -1) {
            System.out.println("Memory allocation failed.");
            return false;
        } else if (startingAddress % PAGE_SIZE != 0 || size % PAGE_SIZE != 0) {
            System.out.println("Invalid starting address or size for freeing memory");
            return false;
        } else {
            return kernelRef.FreeMemory(startingAddress, size);
        }
    }

    public static KernelandProcess getRandomProcess() {
        return kernelRef.getRandomProcess();
    }

    public static void getMapping(int virtualPageNumber) {
        if (virtualPageNumber < 0 || virtualPageNumber > 99) {
            System.out.println("Request page number isn't in virtual memory scope.");
        } else {
            // get the current process
            KernelandProcess currentProc = kernelRef.getCurrentlyRunning();
            KernelandProcess victimProcess = getRandomProcess();

            int vPage = 0;
            while (victimProcess.getVirtualPageLocation(vPage) == -1) {
                vPage++;
            }
            if (currentProc.getVirtualPhysicalMapping(virtualPageNumber).getPhysicalPageNumber() == -1) {
                if (victimProcess.getVirtualPagesArray()[vPage].getOnDiskPageNumber() == -1) {
                    kernelRef.getFFS().Seek(kernelRef.getSwapFilePtr(), currentDiskPage);
                    byte[] retrievedData = new byte[PAGE_SIZE];
                    for (int i = victimProcess.getVirtualPageLocation(vPage) * PAGE_SIZE,
                            j = 0; i < (victimProcess.getVirtualPageLocation(vPage) * PAGE_SIZE)
                                    + PAGE_SIZE; i++, j++) {
                        retrievedData[j] = UserlandProcess.accessMemory()[i];
                    }
                    kernelRef.getFFS().Seek(kernelRef.getSwapFilePtr(),
                            victimProcess.getVirtualPhysicalMapping(virtualPageNumber).getOnDiskPageNumber());
                    kernelRef.getFFS().Write(kernelRef.getSwapFilePtr(), retrievedData);
                    // need to figure out what disk size is
                    victimProcess.getVirtualPhysicalMapping(vPage).setOnDiskPageNumber(vPage);
                }

                // get the physical page location mapped to the virtual page
                int pageLocation = currentProc.getVirtualPageLocation(virtualPageNumber);
                if (pageLocation != -1) {
                    int randomizer = rand.nextInt(2);
                    UserlandProcess.TLB[randomizer][0] = virtualPageNumber;
                    // perform a page swap
                } else {
                    int id = -1;
                    OS.Seek(pageLocation, 0);
                    KernelandProcess randProcess = getRandomProcess();
                    VirtualToPhysicalMapping[] randProcVirtualMem = randProcess.getVirtualPagesArray();
                    for (int i = 0; i < randProcVirtualMem.length; i++) {
                        // find a place on disk that is in use
                        if (randProcVirtualMem[i].getOnDiskPageNumber() != -1) {
                            id = i;
                            break;
                        }
                    }
                    // if a place on disk is in use, swap it's data
                    if (id != -1) {
                        byte[] existingData = OS.Read(kernelRef.getSwapFilePtr(), 1024);
                        randProcess.upRef.Write(pageLocation, existingData[0]);
                        kernelRef.getCurrentlyRunning().getVirtualPagesArray()[id].setOnDiskPageNumber(-1);
                        OS.Write(kernelRef.getSwapFilePtr(), new byte[] { 0 });
                        // if there's free space, write
                    } else {
                        randProcess.upRef.Write(pageLocation, (byte) 0);
                    }
                }
            }
        UserlandProcess.TLB[0][0] = virtualPageNumber;
        UserlandProcess.TLB[0][1] = currentProc.getVirtualPhysicalMapping(virtualPageNumber).getPhysicalPageNumber();
        }
    }
}