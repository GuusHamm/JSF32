package jsf32kochfractalfx;

/**
 * Created by guushamm on 6-1-16.
 */
public class KochProtocol {
	public enum kochEnum{
		CalculateEdges,
		CalculateEdgesLive,
		Zoom
	}

	public KochProtocol(kochEnum kochCommand, int level) {
		switch (kochCommand){
			case CalculateEdges:

				break;
			case CalculateEdgesLive:
				break;
			case Zoom:
				break;
		}
	}
}
