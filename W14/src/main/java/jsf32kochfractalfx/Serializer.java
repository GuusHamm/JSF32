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

//	public void writeToBinaryBufferLineByLine(SavableEdge savable) {
//		fileBinaryLine.delete();
//		try {
//			fileBinaryLineDone.createNewFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
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
//                    System.out.println("PRODUCER: " + newValue );
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
//        System.out.println("PRODUCER: KLAAR" );
//}
//
//	public SavableEdge readFromBinaryBufferLineByLine() {
//
//		SavableEdge instance = null;
//		try {
//			FileInputStream fileInputStream = new FileInputStream(fileBinaryDone);
//			InputStream buffer = new BufferedInputStream(fileInputStream);
//			ObjectInputStream objectInputStream = new ObjectInputStream(buffer);
//
//			int lvl = objectInputStream.readInt();
//			int size = objectInputStream.readInt();
//			ArrayList<Edge> edges = new ArrayList<>();
//
//			for(int i=0;i<size;i++){
//				edges.add((Edge)objectInputStream.readObject());
//			}
//			instance = new SavableEdge(edges,lvl,size);
//
//			objectInputStream.close();
//			fileInputStream.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return instance;
//	}
//
//
//
//    private static final String BUFFERFILE = "buffer.bin";
//    private static final boolean EXCLUSIVE = false;
//    private static final boolean SHARED = true;
//    private static final int STATUS_NOT_READ = 1;
//    private static final int STATUS_READ = 0;
//    private static final int MAXVAL = 400;
//    private static final int NBYTES = 12;
//
//    public static void main(String arsg[]) throws IOException, InterruptedException {
//
//        Random r = new Random();
//        FileLock exclusiveLock = null;
//        try {
//            RandomAccessFile raf = new RandomAccessFile(BUFFERFILE, "rw");
//            FileChannel ch = raf.getChannel();
//
//            MappedByteBuffer out = ch.map(FileChannel.MapMode.READ_WRITE, 0, NBYTES);
//
//            boolean finished = false;
//            while (!finished) {
//
//                exclusiveLock = ch.lock(0, NBYTES, EXCLUSIVE);
//
//                /**
//                 * Try to read the data . . .
//                 */
//
//                // layout:
//                //      0 .. 3 :    4 bytes int with maxvalue
//                //      4 .. 7 :    4 bytes int with status
//                //      8 .. 11:    4 bytes int with value
//
//                // Vraag de maximumwaarde, status en geproduceerde waarde op
//                out.position(0);
//                int lvl = out.getInt();
//                int size = out.getInt();
//                ArrayList<Edge> edges = new ArrayList<>();
//                // Alleen als er iets "nieuws" geproduceerd is verwerken we de
//                // gelezen value
//                if (status == STATUS_NOT_READ) {
//                    // Nieuwe waarde gelezen. Zet status in bestand
//                    out.position(4);
//                    out.putInt(STATUS_READ);
//                    System.out.println("CONSUMER: " + value );
//                    // Bepaal of we klaar zijn, dat is als de gelezen waarde
//                    // gelijk is aan de maxVal in bytes 0 .. 3 van het bestand
//                    finished = (size == edges.size());
//                }
//
//                Thread.sleep(r.nextInt(10));
//                // release the lock
//                exclusiveLock.release();
//
//            }
//        } catch (java.io.IOException ioe) {
//            System.err.println(ioe);
//        } finally {
//            if (exclusiveLock != null) {
//                exclusiveLock.release();
//            }
//        }
//        System.out.println("CONSUMER: KLAAR");
//    }
//
//    }
//
//


}
