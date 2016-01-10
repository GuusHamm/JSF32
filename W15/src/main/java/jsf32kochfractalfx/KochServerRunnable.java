package jsf32kochfractalfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by guushamm on 6-1-16.
 */
public class KochServerRunnable implements Runnable {
	Socket socket = null;

	public KochServerRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));

			String inputLine, outputLine;


		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
