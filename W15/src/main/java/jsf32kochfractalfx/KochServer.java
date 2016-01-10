package jsf32kochfractalfx;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by guushamm on 6-1-16.
 */
public class KochServer {
	ServerSocket serverSocket = null;
	boolean listening;

	public KochServer() {
		listening = true;

		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(-1);
		}

		while (listening){
			try {
				Thread t = new Thread(new KochServerRunnable(serverSocket.accept()));
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
