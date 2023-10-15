import java.io.UnsupportedEncodingException;

/**
 * @author Gregory Vincent
 */
public class DeviceTestProcess extends UserlandProcess {
    public void run(){
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run(){
                while(true){
                    try{
                        Thread.sleep(240);    
                    } catch(Exception e){}
                }
            }
        });
        //Testing two different random devices with different seeds.
        int testRandID1 = OS.Open("random 50");
        byte [] dataFromRandom1 = OS.Read(testRandID1, 5);
        System.out.println("Reading data from a random object with a seed 50.");
        for(int i = 0; i < dataFromRandom1.length; i++){
            System.out.println(dataFromRandom1[i] + " ");
        }

        int testRandID2 = OS.Open("random 100");
        byte [] dataFromRandom2 = OS.Read(testRandID2, 5);
        System.out.println("Reading data from a random object with a seed 100. This object should have different numbers");
        for(int i = 0; i < dataFromRandom2.length; i++){
            System.out.println(dataFromRandom2[i] + " ");
        }
        OS.Close(testRandID1);
        OS.Close(testRandID2);

        try {
            byte[] byteArray = "WRITING SOME SENTENCES TO A FILE. ".getBytes("utf-8");
            byte[] byteArray2 = "The strings created here should be printed in the terminal below".getBytes("utf-8");
            int writeReadFileID = OS.Open("file WriteReadTestFile.txt");
            OS.Write(writeReadFileID, byteArray);
            OS.Write(writeReadFileID, byteArray2);

            //reset the file ptr
            OS.Seek(writeReadFileID, 0);
            System.out.println(new String(OS.Read(writeReadFileID, byteArray.length + byteArray2.length)));
            OS.Close(writeReadFileID);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Opening ten random devices and trying to open an 11th");

        //Opening ten random devices, and printing to the screen
        byte [] current;
        int RandID1 = OS.Open("random 1");
        System.out.println();
        System.out.println("Random 1");
        current = OS.Read(RandID1, 5);
        for(int i = 0; i < 5; i++){
            System.out.println(current[i]);
        }
        System.out.println();
        System.out.println("Random 2");
        int RandID2 = OS.Open("random 2");
        current = OS.Read(RandID2, 5);
        for(int i = 0; i < 5; i++){
            System.out.println(current[i]);
        }
        System.out.println();
        System.out.println("Random 3");
        int RandID3 = OS.Open("random 3");
        current = OS.Read(RandID3, 5);
        for(int i = 0; i < 5; i++){
            System.out.println(current[i]);
        }
        System.out.println();
        System.out.println("Random 4");
        int RandID4 = OS.Open("random 4");
        current = OS.Read(RandID4, 5);
        for(int i = 0; i < 5; i++){
            System.out.println(current[i]);
        }
        System.out.println();
        System.out.println("Random 5");
        int RandID5 = OS.Open("random 5");
        current = OS.Read(RandID5, 5);
        for(int i = 0; i < 5; i++){
            System.out.println(current[i]);
        }
        System.out.println();
        System.out.println("Random 6");
        int RandID6 = OS.Open("random 6");
        current = OS.Read(RandID6, 5);
        for(int i = 0; i < 5; i++){
            System.out.println(current[i]);
        }
        System.out.println();
        System.out.println("Random 7");
        int RandID7 = OS.Open("random 7");
        current = OS.Read(RandID7, 5);
        for(int i = 0; i < 5; i++){
            System.out.println(current[i]);
        }
        System.out.println();
        System.out.println("Random 8");
        int RandID8 = OS.Open("random 8");
        current = OS.Read(RandID8, 5);
        for(int i = 0; i < 5; i++){
            System.out.println(current[i]);
        }
        System.out.println();
        System.out.println("Random 9");
        int RandID9 = OS.Open("random 9");
        current = OS.Read(RandID9, 5);
        for(int i = 0; i < 5; i++){
            System.out.println(current[i]);
        }
        System.out.println();
        System.out.println("Random 10");
        int RandID10 = OS.Open("random 10");
        current = OS.Read(RandID10, 5);
        for(int i = 0; i < 5; i++){
            System.out.println(current[i]);
        }
        //this call won't work since everything is taken, hence the output
        OS.Open("random 11");

        OS.Close(RandID1);
        OS.Close(RandID2);
        OS.Close(RandID3);
        OS.Close(RandID4);
        OS.Close(RandID5);
        OS.Close(RandID6);
        OS.Close(RandID7);
        OS.Close(RandID8);
        OS.Close(RandID9);
        OS.Close(RandID10);
        
    }
}
