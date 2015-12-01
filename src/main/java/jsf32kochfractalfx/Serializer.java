package jsf32kochfractalfx;

import com.google.gson.Gson;

import java.io.*;

/**
 * Created by woute on 1-12-2015.
 */
public class Serializer {

    private File fileBinary= new File(System.getProperty("user.home")+"/KochBinary.bin");;
    private File fileJson= new File(System.getProperty("user.home")+"/KochJson.json");;

    public void writeToBinaryBuffer(SavableEdge savable ){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileBinary);
            OutputStream buffer = new BufferedOutputStream(fileOutputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(buffer);

            objectOutputStream.writeObject(savable);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void writeToBinaryNoBuffer(SavableEdge savable ){
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

    public void writeToJsonBuffer(SavableEdge savableEdge){
        Gson gson = new Gson();
        String json = gson.toJson(savableEdge, SavableEdge.class);

        try {
            FileWriter fw = new FileWriter(fileJson);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeToJsonNoBuffer(SavableEdge savableEdge){
        Gson gson = new Gson();
        String json = gson.toJson(savableEdge, SavableEdge.class);

        try {
            FileWriter fw = new FileWriter(fileJson);
            fw.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SavableEdge readFromBinaryNoBuffer(){

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
    public SavableEdge readFromBinaryBuffer(){

        SavableEdge instance = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileBinary);
            InputStream buffer = new BufferedInputStream(fileInputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(buffer);

            instance = (SavableEdge) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    public SavableEdge readJSONBuffer() {
        Gson gson = new Gson();

        StringBuilder stringBuilder = new StringBuilder();
        FileReader fr;
        try {
            fr = new FileReader(fileJson);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }

            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = stringBuilder.toString();
        return gson.fromJson(json, SavableEdge.class);
    }
    public File getFileBinary() {
        return fileBinary;
    }
}
