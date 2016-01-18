package jsf32kochfractalfx;

import calculate.Edge;
import com.google.gson.Gson;
import javafx.scene.paint.Color;
import org.apache.commons.lang.SerializationUtils;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by woute on 1-12-2015.
 */
public class Serializer {

    private File fileBinary = new File(System.getProperty("user.home") + "/KochBinary.bin");
    private File fileBinaryLineDone = new File(System.getProperty("user.home") + "/KochBinaryLineDone.bin");
    private File fileBinaryLine = new File(System.getProperty("user.home") + "/KochBinaryLine.bin");
    private File fileBinaryDone = new File(System.getProperty("user.home") + "/KochBinaryDone.bin");
    private File fileJson = new File(System.getProperty("user.home") + "/KochJson.json");
    private int fileSize = 68157440;

    private File fileMapped = new File(System.getProperty("user.home") + "/KochMapped.txt");

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

    public SavableEdge readMapped() {


        SavableEdge savableEdge = null;

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

    public void writeMapped(SavableEdge savableEdge) {
        fileMapped.delete();
        try {
            fileMapped.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] serialized = SerializationUtils.serialize(savableEdge);
        try {
            RandomAccessFile memoryMappedFile = new RandomAccessFile(fileMapped, "rw");
            MappedByteBuffer out = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            out.put(serialized);

        } catch (Exception e) {
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

//    public void writeToBinaryBufferLineByLine(SavableEdge savable) {
//        fileBinaryLine.delete();
//        try {
//            fileBinaryLineDone.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////		try {
////			FileOutputStream fileOutputStream = new FileOutputStream(fileBinaryLine);
////			OutputStream buffer = new BufferedOutputStream(fileOutputStream);
////			ObjectOutputStream objectOutputStream = new ObjectOutputStream(buffer);
////
////			objectOutputStream.writeInt(savable.getLevel());
////			objectOutputStream.writeInt(savable.getEdges().size());
////
////
////			for(Edge e : savable.getEdges()) {
////				System.out.println(e);
////				objectOutputStream.writeObject(e);
////			}
////
////			objectOutputStream.close();
////			fileOutputStream.close();
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////
////		fileBinaryLine.renameTo(fileBinaryLineDone);
////
//        FileLock exclusiveLock = null;
//        try {
//            RandomAccessFile raf = new RandomAccessFile(BUFFERFILE, "rw");
//            FileChannel ch = raf.getChannel();
//            MappedByteBuffer out = ch.map(FileChannel.MapMode.READ_WRITE, 0, NBYTES);
//
//            int newValue = 0;
//            while (newValue <= savable.getEdges().size()) {
//
//                exclusiveLock = ch.lock(0, NBYTES, EXCLUSIVE);
//
//                out.position(4);
//                int status = out.getInt();
//
//
//                if (((status != STATUS_NOT_READ) || (newValue == 0))) {
//
////                    out.position(0);
//
//                    out.putInt(MAXVAL);
//                    out.putInt(STATUS_NOT_READ);
//                    out.putInt(savable.getLevel());
//                    out.putInt(savable.getEdges().size());
//
//                    out.put(SerializationUtils.serialize(savable.getEdges().get(newValue)));
//
//                    System.out.println("PRODUCER: " + newValue);
//
//                    newValue++;
//                }
//
//                try {
//                    Thread.sleep(r.nextInt(10));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                // release the lock
//                exclusiveLock.release();
//
//            }
//        } catch (java.io.IOException ioe) {
//            System.err.println(ioe);
//        } finally {
//            if (exclusiveLock != null) {
//                try {
//                    exclusiveLock.release();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        System.out.println("PRODUCER: KLAAR");
//    }




    private static final boolean EXCLUSIVE = false;
    private static final int NBYTES = 76;

    public void writeToBinaryBufferLineByLine(SavableEdge savableEdge) throws IOException, InterruptedException {

        Random r = new Random();
        FileLock exclusiveLock = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(System.getProperty("user.home") + "/KochBinaryDone.bin", "rw");
            FileChannel ch = raf.getChannel();
            MappedByteBuffer out = ch.map(FileChannel.MapMode.READ_WRITE, 0, NBYTES);

            List<Edge> newValue=  savableEdge.getEdges();

            out.putInt(savableEdge.getLevel());
            out.putInt(newValue.size());

            for (int i = 0; i < newValue.size(); i++) {

                exclusiveLock = ch.lock((i*NBYTES)+8, NBYTES, EXCLUSIVE);

                out.putInt(0);//can read
                out.putDouble(newValue.get(i).X1);
                out.putDouble(newValue.get(i).Y1);
                out.putDouble(newValue.get(i).X2);
                out.putDouble(newValue.get(i).Y2);
                out.putDouble(newValue.get(i).color.getRed());
                out.putDouble(newValue.get(i).color.getGreen());
                out.putDouble(newValue.get(i).color.getBlue());
                out.putDouble(newValue.get(i).color.getOpacity());

                System.out.println("position" + out.position());
                int newPosition = (i*NBYTES)+8;
                System.out.println("new position of writer" + newPosition);
                out.position(newPosition);
                out.putInt(1);
                exclusiveLock.release();
                System.out.println(i);
                out = ch.map(FileChannel.MapMode.READ_WRITE, (i*NBYTES)+8, NBYTES);
            }
        } catch (java.io.IOException ioe) {
            System.err.println(ioe);
        }
    }
    public SavableEdge readFromBinaryBufferLineByLine() {

        SavableEdge instance = null;
        Random r = new Random();
        FileLock exclusiveLock = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(System.getProperty("user.home") + "/KochBinaryDone.bin", "rw");
            FileChannel ch = raf.getChannel();

            MappedByteBuffer out = ch.map(FileChannel.MapMode.READ_WRITE, 0, NBYTES);
//            exclusiveLock = ch.lock(0, NBYTES, false);

            int lvl = out.getInt();
            int size = out.getInt();
            ArrayList<Edge> edges = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                System.out.println("reading pos:" +((int)(i*NBYTES)+8));
                exclusiveLock = ch.lock( (i*NBYTES)+8, NBYTES, EXCLUSIVE);

                int oldPosition = out.position();
                while(out.getInt()!=1){
                    out.position(oldPosition);
                    Thread.sleep(10);
                }
                double x1 = out.getDouble();
                double y1 = out.getDouble();
                double x2 = out.getDouble();
                double y2 = out.getDouble();

                Color color = new Color(out.getDouble(),out.getDouble(),out.getDouble(),out.getDouble());
                edges.add(new Edge(x1,y1,x2,y2, color));
                exclusiveLock.release();
                out = ch.map(FileChannel.MapMode.READ_WRITE, (i*NBYTES)+8, NBYTES);
            }
            System.out.println("total read edges:"+edges.size());
            instance = new SavableEdge(edges, lvl, size);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }
}



