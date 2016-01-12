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

            Socket socket = new Socket("127.0.0.1", 4444);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Sending message
            out.println(lvl);

            String feedback = in.readLine();
            System.out.println("The client received this as feedback :" + feedback);

            SavableEdge savableEdgeReturn = kochProtocol.handleInput(feedback);
            manager.setEdges(savableEdgeReturn.getEdges());

            in.close();
            out.close();
            socket.close();
            manager.drawEdges(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

