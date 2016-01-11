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
	KochServer kochServer;


	public KochCommandLine(int edgeNumber) {

		kochServer = new KochServer(new KochManager());

		jsf31KochFractalFX = new JSF31KochFractalFX();
		this.kochManager = new KochManager(jsf31KochFractalFX);

		KochFractal kochFractal = new KochFractal();
		kochFractal.setLevel(edgeNumber);
		kochFractal.addObserver(this);

		kochFractal.generateBottomEdge();
		kochFractal.generateLeftEdge();
		kochFractal.generateRightEdge();

	}


	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		System.out.println("Give me something too work with here");

		int i = input.nextInt();
		System.out.println("Calculating Edges");
		new KochCommandLine(i);

		System.out.println("Hurray Great Success");
	}

	@Override
	public void update(Observable o, Object object) {
		Edge edge = (Edge) object;
		kochManager.addEdge(edge);
	}
}
