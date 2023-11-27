/**
 * @author Gregory Vincent
 */
public class VirtualToPhysicalMapping {
    public int physicalPageNumber;
    public int onDiskPageNumber;

    public VirtualToPhysicalMapping(){
        physicalPageNumber = -1; 
        onDiskPageNumber = -1;
    }

    public void setPhysicalPageNumber(int value){
        physicalPageNumber = value;
    }

    public void setOnDiskPageNumber(int value){
        onDiskPageNumber = value;
    }
    
    public int getPhysicalPageNumber(){
        return physicalPageNumber;
    }

    public int getOnDiskPageNumber(){
        return onDiskPageNumber;
    }
}
