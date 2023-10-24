/**
 * @author Greg Vincent
 * Virtual File System
 */
public class VFS implements Device {
    private Device[] devices = new Device[10];
    private int[] deviceID = new int[10];
    private String deviceTypes = "file random";
    FakeFileSystem ffs = new FakeFileSystem();
    RandomDevice ranDev = new RandomDevice();
    public VFS(){
        //initialize the device array
        for(int i = 0; i < deviceID.length; i++){
            deviceID[i] = -1;
        }
    }

    @Override
    public int Open(String inputString) {
        if(validateDeviceName(inputString)){
            String deviceType = inputString.split(" ")[0];
            if (deviceTypes.contains(deviceType)){
                switch(deviceType.toLowerCase()){
                    case "file":
                        for(int i = 0; i < deviceID.length; i++){
                            if(deviceID[i] == -1){
                                //both arrays hold connected information at the same index
                                deviceID[i] = ffs.Open(inputString.split(" ")[1]);
                                devices[i] = ffs;
                                return i;
                            }
                        }
                        System.out.println("No empty spot in Filesystem.");
                        return -1;
                    case "random":
                        for(int i = 0; i < deviceID.length; i++){
                            if(deviceID[i] == -1){
                                deviceID[i] = ranDev.Open(inputString.split(" ")[1]);
                                devices[i] = ranDev;
                                return i;
                            }
                        }
                        System.out.println("No empty spot in Random Device system..");
                        return -1;
                    default:
                        System.out.println("Unsupported device.");
                        return -1;
                }
            }
        }
        System.out.println("Invalid parameter for open.");
        return -1;
    }

    @Override
    public void Close(int id) {
        deviceID[id] = -1;
        devices[id].Close(id);
        devices[id] = null;
    }
    
    @Override
    public byte[] Read(int id, int size) {
       return devices[id].Read(id, size);
    }
    
    @Override
    public void Seek(int id, int to) {
        devices[id].Seek(id, to);
    }

    @Override
    public int Write(int id, byte[] data) {
        return devices[id].Write(id, data);
    }

    /**
     * assuming all devices are in the form <deviceType> <Name>
     * file something.txt
     * random 100
     */
    private boolean validateDeviceName(String name){
        return name.split(" ").length == 2;
    }
 
}
