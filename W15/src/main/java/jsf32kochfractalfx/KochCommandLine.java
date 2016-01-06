package jsf32kochfractalfx;

import calculate.Edge;
import calculate.KochFractal;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Created by guushamm on 25-11-15.
 */
public class KochCommandLine implements Observer {
	KochManager kochManager;
	JSF31KochFractalFX jsf31KochFractalFX;
    Serializer serializer = new Serializer();
	public KochCommandLine(int edgeNumber) {

		jsf31KochFractalFX = new JSF31KochFractalFX();
		this.kochManager = new KochManager(jsf31KochFractalFX);

		KochFractal kochFractal = new KochFractal();
		kochFractal.setLevel(edgeNumber);
		kochFractal.addObserver(this);

		kochFractal.generateBottomEdge();
		kochFractal.generateLeftEdge();
		kochFractal.generateRightEdge();

		List<Edge> edges = kochManager.getNewEdges();
		System.out.println("calculated edges :" + edges.size());

		SavableEdge savableEdge = new SavableEdge(edges,edgeNumber,edges.size());
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		serializer.writeToBinaryNoBuffer(savableEdge);
		System.out.println(String.format("write: binary with no buffer: %s",stopWatch.toString()));
		stopWatch.reset();

		stopWatch.start();
		serializer.writeToBinaryBuffer(savableEdge);
		System.out.println(String.format("write: binary with buffer: %s",stopWatch.toString()));
		stopWatch.reset();

		stopWatch.start();
		serializer.writeToJsonBuffer(savableEdge);
		System.out.println(String.format("write: json with buffer: %s",stopWatch.toString()));
		stopWatch.reset();

		stopWatch.start();
		serializer.writeMapped(savableEdge);
		System.out.println(String.format("write: mapped: %s",stopWatch.toString()));
		stopWatch.reset();

		System.out.println("");

		stopWatch.start();
		serializer.readFromBinaryNoBuffer();
		System.out.println(String.format("read: binary with no buffer: %s",stopWatch.toString()));
		stopWatch.reset();

		stopWatch.start();
		serializer.readFromBinaryBuffer();
		System.out.println(String.format("read: binary with buffer: %s",stopWatch.toString()));
		stopWatch.reset();

		stopWatch.start();
		serializer.readJSONBuffer();
		System.out.println(String.format("read: json with buffer: %s",stopWatch.toString()));
		stopWatch.reset();

		stopWatch.start();
		serializer.readMapped();
		System.out.println(String.format("read: mapped: %s",stopWatch.toString()));
		stopWatch.reset();
	}



//	}


	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		System.out.println("Give me something too work with here");

		int i = input.nextInt();
		System.out.println("Calculating Edges");
		new KochCommandLine(i);

		System.out.println("Hurray Great Success");
	}

	//	private void writeToBufferedText(SavableEdge savable ,File outputFile){
//		try {
//			FileWriter fw = new FileWriter(outputFile);
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(txt);
//			bw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
	@Override
	public void update(Observable o, Object object) {
		Edge edge = (Edge) object;
		kochManager.addEdge(edge);
	}
}
