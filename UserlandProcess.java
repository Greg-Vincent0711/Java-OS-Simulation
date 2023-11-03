/**
 * @author Gregory Vincent
 */
public abstract class UserlandProcess implements Runnable{

    public byte Read(int address){
        return 0;
    }

    public void Write(int address, byte vale){

    }

    private int findPageNumber(int address){
        return 0;
    }
}
