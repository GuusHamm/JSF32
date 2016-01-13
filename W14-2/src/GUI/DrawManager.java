package GUI;

import w14filelocking.Edge;

import java.util.List;

/**
 * Created by Jim on 6-1-2016.
 */
public class DrawManager {
    JSF31KochFractalFX application;

    public DrawManager(JSF31KochFractalFX application){
        this.application = application;

    }

    public void drawEdge(List<Edge> edgeList, int index){
        application.drawEdge(edgeList.get(index));
    }

    public void setApplication(JSF31KochFractalFX application) {
        this.application = application;
    }
}
