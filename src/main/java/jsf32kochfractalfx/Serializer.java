package jsf32kochfractalfx;

import java.io.*;

/**
 * Created by woute on 1-12-2015.
 */
public class Serializer {

    private File fileBinary= new File(System.getProperty("user.home")+"/KochBinary.bin");;

    public void writeToBinary(SavableEdge savable ){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileBinary);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(savable);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SavableEdge readFromBinary(){

        SavableEdge instance = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileBinary);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            instance = (SavableEdge) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    public File getFileBinary() {
        return fileBinary;
    }
}
