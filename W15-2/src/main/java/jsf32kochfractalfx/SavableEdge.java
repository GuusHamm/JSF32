package jsf32kochfractalfx;

import calculate.Edge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guushamm on 1-12-15.
 */
public class SavableEdge implements Serializable {

	private List<Edge> edges;
	private int level;
	private int size;

	public Type getType() {
		return type;
	}

	private Type type;
	public enum Type{
		singleEdge,
		AllEdges,
		stop
	}
	public SavableEdge(List<Edge> edges, int level, int size, Type type) {
		this.edges = edges;
		this.level = level;
		this.size = size;
		this.type = type;
	}

	public ArrayList<Edge> getEdges() {
		return (ArrayList) edges;
	}

	public int getLevel() {
		return level;
	}

	public int getSize() {
		return size;
	}

	@Override
	public String toString(){
		return String.format("Level : %d, Size: %d",level,size);
	}
}
