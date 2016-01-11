package jsf32kochfractalfx;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochTask;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import watch.WatchDirMain;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by linux on 23-9-15.
 */
public class KochManager {

    private jsf32kochfractalfx.JSF31KochFractalFX application;
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private ArrayList<Edge> oldEdges = new ArrayList<Edge>();
    private int count =1;
    private CyclicBarrier cyclicBarrier;
    private ExecutorService pool;

	KochTask leftTask;
			KochTask bottomTask;
	KochTask rightTask;

	private StopWatch stopWatch;

	private WatchDirMain watchDirMain;

    public KochManager(jsf32kochfractalfx.JSF31KochFractalFX application) {
		watchDirMain = new WatchDirMain(System.getProperty("user.home"),false,this);

        this.application = application;
    }

	public void changeLevel(int nxt){
		stopWatch = new StopWatch();
		stopWatch.start();

		oldEdges.addAll(edges);
		edges.clear();

        count = 0;


        pool = Executors.newFixedThreadPool(3);
        cyclicBarrier = new CyclicBarrier(3);

		this.leftTask = new KochTask(this,cyclicBarrier,application.getProgressLeft(),application.getLabelProgressLeft(), KochFractal.position.LEFT,nxt);
		this.bottomTask = new KochTask(this,cyclicBarrier,application.getProgressBottom(),application.getLabelProgressBottom(), KochFractal.position.BOTTOM,nxt);
		this.rightTask = new KochTask(this, cyclicBarrier,application.getProgressRight(),application.getLabelProgressRight(), KochFractal.position.RIGHT,nxt);

		pool.submit(leftTask);
		pool.submit(bottomTask);
		pool.submit(rightTask);

        application.setTextCalc(stopWatch.toString());
    }


    public void drawEdges(Color color) {
        application.clearKochPanel();
		synchronized (edges) {
			for (Edge e : edges) {
				if (color == null) application.drawEdge(e);
				else application.drawEdge(new Edge(e, color));
			}
		}

		synchronized (oldEdges) {
			for (Edge e : oldEdges) {
				application.drawEdge(e);
			}
		}
    }

    public void signalEnd() {
        pool.shutdown();
        synchronized (oldEdges) {
            oldEdges.clear();
        }
        application.requestDrawEdges();
    }

    public synchronized void addEdge(Edge e )    {
        edges.add(e);
    }

	public void cancel() {
		leftTask.cancel();
		bottomTask.cancel();
		rightTask.cancel();
	}

	public synchronized void drawEdge(Edge edge){
		drawEdges(Color.WHITE);
		application.drawEdge(edge);
	}

	public void updateTimestamp() {
		String ms = stopWatch.toString();

			Platform.runLater(() -> {
				application.setTextDraw(ms);
				application.setTextNrEdges(String.valueOf(edges.size()));
			});
	}

	public ArrayList<Edge> getOldEdges() {
		return oldEdges;
	}
	public ArrayList<Edge> getNewEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	public void setOldEdges(ArrayList<Edge> oldEdges) {
		this.oldEdges = oldEdges;
	}

	public synchronized void entry_created() {
		Serializer serializer = new Serializer();
		SavableEdge savableEdge = serializer.readFromBinaryBuffer();

//		oldEdges = edges;
		edges = savableEdge.getEdges();

		try {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					application.clearKochPanel();
					application.setCurrentLevel(savableEdge.getLevel());
					application.requestDrawEdges();
				}});
		}catch (IllegalStateException e){

		}

	}

	public synchronized void entry_deleted() {
		return;
	}

	public synchronized void entry_modified() {
		return;
	}
}



