/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package calculate;

import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Peter Boots
 */
public class Edge implements Serializable
{
    public double X1, Y1, X2, Y2;
    public transient Color color;
    
    public Edge(double X1, double Y1, double X2, double Y2, Color color)
    {
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;
        this.color = color;
    }

    public Edge(Edge edge, Color color){
        this(edge.X1, edge.Y1, edge.X2, edge.Y2, color);
    }

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
//        stream.writeDouble(X1);
//        stream.writeDouble(X2);
//        stream.writeDouble(Y1);
//        stream.writeDouble(Y2);

        stream.writeDouble(color.getRed());
        stream.writeDouble(color.getGreen());
        stream.writeDouble(color.getBlue());
        stream.writeDouble(color.getOpacity());
   }

    private void readObject(java.io.ObjectInputStream stream)
          throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
//        X1 = stream.readDouble();
//        X2 = stream.readDouble();
//        Y1 = stream.readDouble();
//        Y2 = stream.readDouble();
        color =  new Color(stream.readDouble(),stream.readDouble(),stream.readDouble(),stream.readDouble());

    }
}
