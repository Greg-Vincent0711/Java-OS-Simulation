/**
 * @author Gregory Vincent
 */
import java.io.IOException;
import java.io.RandomAccessFile;
public class FakeFileSystem implements Device {
    public FakeFileSystem(){}
    private RandomAccessFile[] fileArr = new RandomAccessFile[10];
    public int Open(String name){
        if(validateFileName(name)){
            try{
                for(int i = 0; i < fileArr.length; i++){
                    if(fileArr[i] == null){
                        fileArr[i] = new RandomAccessFile(name, "rw");
                        return i;
                    }
                }
                return -1;
            } catch(IOException e){e.printStackTrace();}
        }
        return -1;
    }
    public byte[] Read(int id, int size){
        try{
            byte [] readBytes = new byte[size];
            fileArr[id].read(readBytes);
            return readBytes;
            
        } catch(IOException e){return null;}
    }
    public void Seek(int id, int to){
        try {
            fileArr[id].seek(to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int Write(int id, byte[] data){
        try {
            fileArr[id].write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }
    public void Close(int id){
        try {
            fileArr[id].close();
            fileArr[id] = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    private boolean validateFileName(String name){
        return name.length() >= 1;
    }
}
