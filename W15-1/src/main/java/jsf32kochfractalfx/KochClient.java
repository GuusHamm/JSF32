package jsf32kochfractalfx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wouter on 11-1-2016.
 */
public class KochClient implements Runnable {

    KochProtocol kochProtocol;
    KochManager manager;


    public KochClient(KochManager manager) {

        this.manager = manager;
        kochProtocol = new KochProtocol();
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            if (serverSocket == null) {
                serverSocket = new ServerSocket(4444);
            }

            while (true) {
                clientSocket = serverSocket.accept();
                try {

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String input = buffer.readLine();
                    buffer.close();

                    System.out.println("ClientSideServer :" + input);
                    SavableEdge savableEdge = kochProtocol.handleInput(input);

//                    manager.setOldEdges(manager.getNewEdges());
                    manager.setEdges(savableEdge.getEdges());

                    manager.getApplication().requestDrawEdges();


                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

