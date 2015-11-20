package calculate;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import jsf32kochfractalfx.KochManager;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by xubuntu on 13-10-15.
 */
public class KochTask extends Task<ArrayList> implements Observer{

    private KochFractal kochFractal;
    private KochManager kochManager;
    private int currentLevel;
    private KochFractal.position position;
    private CyclicBarrier cyclicBarrier;
	private int edgesCalculated;
	private ProgressBar progressBar;
	private Label label;

	private static final long sleepTimer = 50;

    public KochTask(KochManager kochManager, CyclicBarrier cyclicBarrier, ProgressBar progressBar, Label label, KochFractal.position position, int currentLevel)
    {
        this.kochFractal = new KochFractal(position,kochManager);
        kochFractal.setLevel(currentLevel);

		this.kochManager = kochManager;
		this.kochFractal.addObserver(this);

		this.cyclicBarrier = cyclicBarrier;
		this.progressBar = progressBar;
		this.label = label;
		this.position = position;
		progressBar.progressProperty().bind(this.progressProperty());
		label.textProperty().bind(this.messageProperty());
    }

    @Override
    public ArrayList<Edge> call() throws Exception
    {
		switch (position) {
			case LEFT:
				kochFractal.generateLeftEdge();
				break;
			case RIGHT:
				kochFractal.generateRightEdge();
				break;
			case BOTTOM:
				kochFractal.generateBottomEdge();
				break;
		}

		if (cyclicBarrier.await() == 0){

			kochManager.signalEnd();
		}
		return null;
    }

//	@Override
//	public ArrayList<Edge> get(){
//		return edges;
//	}

    @Override
    public void update(Observable o, Object object)
    {
        Edge edge = (Edge) object;
		kochManager.addEdge(edge);

		edgesCalculated++;

		Platform.runLater(() -> {
			kochManager.drawEdge(new Edge(edge, Color.WHITE));
			updateProgress(edgesCalculated, kochFractal.getNrOfEdges() / 3);
			updateMessage(edgesCalculated + " / " + kochFractal.getNrOfEdges() / 3);


        });

		kochManager.updateTimestamp();
		try {
			Thread.sleep((kochFractal.getLevel() < 4) ? sleepTimer : 5);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

    }

	@Override
	protected void cancelled() {
		super.cancelled();
		kochFractal.cancel();
	}


}
