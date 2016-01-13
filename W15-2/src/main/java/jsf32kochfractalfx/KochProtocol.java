package jsf32kochfractalfx;

import java.io.*;
import java.util.Base64;

/**
 * Created by guushamm on 6-1-16.
 */
public class KochProtocol {
	private Serializer serializer;
	private static KochProtocol instance;

	public static KochProtocol getInstance(){
		if (instance == null){
			instance = new KochProtocol();
			return instance;
		}else{
			return instance;
		}
	}

	public enum kochEnum{
		CalculateEdges,
		CalculateEdgesLive,
		Zoom
	}

	public KochProtocol() {
		serializer = new Serializer();
	}
	
	public String handleOutput(SavableEdge savableEdge){

		return serialeDesiredObjects64(savableEdge);
	}

	public SavableEdge handleInput(String input){

		return deserialeDesiredObjects64(input);
	}

	private String serialeDesiredObjects64(Object objectsToSerialize){

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream( baos );
			oos.writeObject( objectsToSerialize );
			oos.close();
			return Base64.getEncoder().encodeToString(baos.toByteArray());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private SavableEdge deserialeDesiredObjects64(String deserializeString) {

		SavableEdge o =null;
		try {
			byte[] data = Base64.getDecoder().decode(deserializeString);
			ObjectInputStream ois = new ObjectInputStream(
					new ByteArrayInputStream(data));
			o =  (SavableEdge)ois.readObject();
			ois.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return  o;
	}

}
