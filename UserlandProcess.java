
/**
 * @author Gregory Vincent
 */
public abstract class UserlandProcess implements Runnable{
    private static byte [] memory =  new byte[1024 * 1024];
    //translation lookaside buffer
    public static int [][] TLB = new int[2][2];
    private int PAGE_SIZE = 1024;

    public UserlandProcess(){}
    
    public byte Read(int address){
        int virtualPageNumber = address / PAGE_SIZE;
        int offset = address % PAGE_SIZE;
        int physicalPage = getPhysicalPage(virtualPageNumber);
            if(physicalPage != -1){
                //findVirtualLocation returns an array with the 2nd element equal to the location within the physical page
                return memory[(physicalPage * PAGE_SIZE + offset)];
            } else{
                OS.getMapping(virtualPageNumber);
            }
        return 0;
    }
    
    public void Write(int address, byte value){
        int virtualPageNumber = address / PAGE_SIZE;
        int offset = address % PAGE_SIZE;
        int physicalPage = getPhysicalPage(virtualPageNumber);
        memory[(physicalPage * PAGE_SIZE + offset)] = value;
    }

    public void clearTLB(){
        TLB[0][0] = -1; //Virtual
        TLB[0][1] = -1; //Virtual
        TLB[1][0] = -1; //Physical
        TLB[1][1] = -1; //Physical
    }

    //returns the physical page given a virtual mapping or -1 if it doesn't exist
    private int getPhysicalPage(int virtualAddress){
        while(true){
            if(TLB[0][0] == virtualAddress){
                return TLB[1][0];
            } else if(TLB[0][1] == virtualAddress){
                return TLB[1][1];
            }
            else{
                OS.getMapping(virtualAddress);
            }
        }
    }    
}
