package jsf32kochfractalfx;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wouter on 11-1-2016.
 */
public class KochClient {

    KochProtocol kochProtocol;
    KochManager manager;


    public KochClient(KochManager manager) {

        this.manager = manager;
        kochProtocol = new KochProtocol();
    }

    public void sendMessage(int lvl) {
        try {

            Socket echoSocket = new Socket("127.0.0.1", 4444);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            out.println(lvl);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            String input = in.readLine();
            SavableEdge savableEdgeReturn = kochProtocol.handleInput(input);
            manager.setEdges(savableEdgeReturn.getEdges());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

