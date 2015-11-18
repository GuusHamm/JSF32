package jsf32kochfractalfx;

import calculate.Edge;
import calculate.KochCallable;
import calculate.KochFractal;
import timeutil.TimeStamp;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.*;

/**
 * Created by linux on 23-9-15.
 */
public class KochManager implements Observer, Callable {

    private jsf32kochfractalfx.JSF31KochFractalFX application;
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private int count =1;

    TimeStamp time = new TimeStamp();

    public KochManager(jsf32kochfractalfx.JSF31KochFractalFX application) {

        this.application = application;
    }

    public void changeLevel(int nxt) {

        count = 0;

        edges.clear();
        TimeStamp time = new TimeStamp();
        time.setBegin();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        ExecutorService pool = Executors.newFixedThreadPool(3);

        Future<ArrayList> listLeft = pool.submit(new KochCallable(this, cyclicBarrier, KochFractal.position.LEFT,nxt));
        Future<ArrayList> listBottom = pool.submit(new KochCallable(this, cyclicBarrier, KochFractal.position.BOTTOM,nxt));
        Future<ArrayList> listRight = pool.submit(new KochCallable(this, cyclicBarrier, KochFractal.position.RIGHT,nxt));

        try {

            edges.addAll(listLeft.get());
            edges.addAll(listBottom.get());
            edges.addAll(listRight.get());

        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }

        time.setEnd();
        application.setTextCalc(time.toString().substring(20));

        application.requestDrawEdges();
        time.setEnd();
        application.setTextCalc(time.toString().substring(20));
    }


    public void drawEdges() {

        application.clearKochPanel();
        TimeStamp time = new TimeStamp();
        time.setBegin();

        for(Edge e : edges)
        {
            application.drawEdge(e);
        }
        time.setEnd();

        application.setTextDraw(time.toString().substring(20));
        application.setTextNrEdges(edges.size() + "");
    }


    public void update(Observable o, Object arg) {

        count++;
        //application.drawEdge((Edge) arg);
        addEdge((Edge) arg);
//        if(count>=3)
//        {
//            drawEdges();
//        }
    }
    public synchronized void addEdge(Edge e )
    {
        edges.add(e);
    }

    public synchronized void addCount()
    {
        count++;
        if(count==3)
        {
            drawEdges();
            time.setEnd();
            application.setTextCalc(time.toString().substring(20));

            application.requestDrawEdges();

            time.setEnd();
            application.setTextCalc(time.toString().substring(20));
            count=1;
        }
    }

    @Override
    public Object call() throws Exception {
        return null;
    }
}



