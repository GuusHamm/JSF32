package jsf32kochfractalfx;

import calculate.Edge;
import calculate.KochFractal;

import java.io.IOException;
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


	public KochCommandLine(int edgeNumber) throws IOException, InterruptedException {

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
		serializer.writeToBinaryBufferLineByLine(savableEdge);
		serializer.readFromBinaryBufferLineByLine();

	}



//	}


	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		System.out.println("Give me something too work with here");

		int i = input.nextInt();
		System.out.println("Calculating Edges");
		try {
			new KochCommandLine(i);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

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
