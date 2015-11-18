package calculate;

import jsf32kochfractalfx.KochManager;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by xubuntu on 13-10-15.
 */
public class KochCallable implements Observer, Callable<ArrayList>{
    private KochFractal kochFractal;
    private KochManager kochManager;
    private int currentLevel;
    private KochFractal.position position;
    private CyclicBarrier cyclicBarrier;

    private ArrayList<Edge> edges;

    public KochCallable(KochManager kochManager, CyclicBarrier cyclicBarrier,KochFractal.position position,int currentLevel)
    {
        this.kochFractal = new KochFractal(position,kochManager);
        kochFractal.setLevel(currentLevel);
        this.position = position;
        this.kochManager = kochManager;
        this.cyclicBarrier = cyclicBarrier;
        kochFractal.addObserver(kochManager);
        edges = new ArrayList<Edge>();
    }

    @Override

    public ArrayList call() throws Exception
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
        cyclicBarrier.await();
        return edges;
    }


    @Override
    public void update(Observable o, Object arg)
    {    Edge e = (Edge) arg;
        edges.add(e);

    }
}
