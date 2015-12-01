package jsf32kochfractalfx;

/**
 * Created by guushamm on 20-11-15.
 */
public class StopWatch {

	private long start = 0;

	private long pauseMoment = 0;

	public StopWatch() {

	}

	public void start() {
		start = System.nanoTime();
	}

	public void reset() {
		start();
	}

	public void pause() {
		pauseMoment = System.nanoTime();
	}

	public void resume() {
		start += pauseMoment - System.nanoTime();
		pauseMoment = 0;
	}

	public double getCurrentTimeInMs() {
		return (System.nanoTime() - start) / 1000000;
	}

	@Override
	public String toString() {
		return Math.round(getCurrentTimeInMs()) + "ms";
	}
}
