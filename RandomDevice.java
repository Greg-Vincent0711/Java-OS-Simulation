/**
 * @author Gregory Vincent
 */
import java.util.Random;
//interface for other devices
public class RandomDevice implements Device {
    private Random randomDevArr[] = new Random[10];
    Random random;

public RandomDevice(){}
    public int Open(String s){
        //if open param isn't null or empty, use as random's seed
        random = s.length() > 0 ? new Random(Integer.parseInt(s)) : new Random();
        //place into an empty spot in the array
        for(int i = 0; i < randomDevArr.length; i++){
            if(randomDevArr[i] == null){
                randomDevArr[i] = new Random();
                return i;
            }
        }
        System.out.println("No space to make another device");
        return -1;
    }
    public void Close(int id){
        randomDevArr[id] = null;
    }
    public byte[] Read(int id, int size){
        byte[] arr = new byte[size];
        randomDevArr[id].nextBytes(arr);
        return arr;
    }
    public void Seek(int id, int to){
        Read(id, to);
    }
    // you wouldn't ever write to a random device
    public int Write(int id, byte[] data){
        return 0;
    }
}