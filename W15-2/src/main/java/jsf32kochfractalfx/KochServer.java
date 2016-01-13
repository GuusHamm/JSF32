package jsf32kochfractalfx;

import calculate.Edge;
import calculate.KochFractal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by guushamm on 6-1-16.
 */
public class KochServer implements Observer{
    //	ServerSocket serverSocket = null;
    boolean listening;
    KochManager manager;
    ArrayList<String> ips;
    KochProtocol kochProtocol;

    public KochServer(KochManager manager) {
        listening = true;
        this.manager = manager;
//        ips = new ArrayList<>();
//        ips.add("145.93.169.152");
//        ips.add("145.93.88.207");

        kochProtocol = KochProtocol.getInstance();
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

                    KochFractal kochFractal = new KochFractal();
                    kochFractal.setLevel(   Integer.parseInt(input)     );
                    System.out.println("Server int :" + input);
                    kochFractal.addObserver(this);

                    kochFractal.generateBottomEdge();
                    kochFractal.generateLeftEdge();
                    kochFractal.generateRightEdge();

                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


                    String output ;
                    if(manager.getNewEdges().size()>1) {
                        output = kochProtocol.handleOutput(KochProtocol.kochEnum.CalculateEdges, new SavableEdge(manager.getNewEdges(), Integer.parseInt(input), manager.getNewEdges().size(), SavableEdge.Type.AllEdges));
                    }else {
                        output = kochProtocol.handleOutput(KochProtocol.kochEnum.CalculateEdges, new SavableEdge(manager.getNewEdges(), Integer.parseInt(input), manager.getNewEdges().size(), SavableEdge.Type.singleEdge));
                    }
                    System.out.println("ClientSideServer :" + output);
                    out.println(output);
                    buffer.close();
                    manager.setEdges(new ArrayList<>());
                    manager.setOldEdges(new ArrayList<>());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleInput() {

    }

    public void sendData(SavableEdge savableEdge) {
        handleOutputPush(kochProtocol.handleOutput(KochProtocol.kochEnum.CalculateEdges, savableEdge));
    }

    private void handleOutputPush(String output) {
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

    public static void main(String[] args) {
        KochManager manager = new KochManager();
        new KochServer(manager);
    }
    @Override
    public void update(Observable o, Object object) {
        Edge edge = (Edge) object;
        manager.addEdge(edge);
    }
}
