package jsf32kochfractalfx;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by guushamm on 6-1-16.
 */
public class KochServer {
//	ServerSocket serverSocket = null;
	boolean listening;

	ArrayList<String> ips;

	KochProtocol kochProtocol;

	public KochServer() {
		listening = true;

		ips = new ArrayList<>();

//		YAYAYAYAYYA
		ips.add("145.93.169.152");
		ips.add("145.93.88.207");

		kochProtocol = KochProtocol.getInstance();

//		try {
////			serverSocket = new ServerSocket(4444);
//		} catch (IOException e) {
//			System.err.println("Could not listen on port: 4444.");
//			System.exit(-1);
//		}
	}

	public void handleInput(){

	}

	public void sendData(SavableEdge savableEdge ){
		handleOutputPush(kochProtocol.handleOutput(KochProtocol.kochEnum.CalculateEdges,savableEdge));
	}

	private void handleOutputPush(String output){
		for (String ip : ips) {
			try {
				Socket socket = new Socket(ip, 4444);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println(output);
				socket.close();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
