package jsf32kochfractalfx;

import com.google.gson.Gson;
import org.apache.commons.lang.SerializationUtils;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by woute on 1-12-2015.
 */
public class Serializer {

	private File fileBinary = new File(System.getProperty("user.home") + "/KochBinary.bin");
	private File fileBinaryDone = new File(System.getProperty("user.home") + "/KochBinaryDone.bin");
	private File fileJson = new File(System.getProperty("user.home") + "/KochJson.json");
	private File fileMapped = new File(System.getProperty("user.home") + "/KochMapped.txt");

	private int fileSize = 68157440;

	public void writeToBinaryBuffer(SavableEdge savable) {
		fileBinary.delete();
		try {
			fileBinary.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

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

		fileBinary.renameTo(fileBinaryDone);
	}

	public void writeToBinaryNoBuffer(SavableEdge savable) {
		fileBinary.delete();
		try {
			fileBinary.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
    public SavableEdge readMapped(){


		SavableEdge savableEdge=null;

        byte[] serialized = new byte[fileSize];
        try {
            RandomAccessFile memoryMappedFile = new RandomAccessFile(fileMapped, "rw");
            MappedByteBuffer out = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            out.get(serialized);
            savableEdge = (SavableEdge) org.apache.commons.lang.SerializationUtils.deserialize(serialized);

        } catch (Exception ex) {
			ex.printStackTrace();
		}
		return savableEdge;
						
    }

    public void writeMapped(SavableEdge savableEdge){
		fileMapped.delete();
		try {
			fileMapped.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] serialized = SerializationUtils.serialize(savableEdge);
        try
        {
            RandomAccessFile memoryMappedFile = new RandomAccessFile(fileMapped, "rw");
            MappedByteBuffer out = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            out.put(serialized);

        }catch(Exception e){
			e.printStackTrace();
		}

    }


	public void writeToJsonBuffer(SavableEdge savableEdge) {
		fileJson.delete();
		try {
			fileJson.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	public SavableEdge readFromBinaryNoBuffer() {

		SavableEdge instance = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(fileBinaryDone);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

			instance = (SavableEdge) objectInputStream.readObject();

			objectInputStream.close();
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public SavableEdge readFromBinaryBuffer() {

		SavableEdge instance = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(fileBinaryDone);
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
