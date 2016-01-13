package jsf32kochfractalfx;

import calculate.Edge;
import calculate.KochFractal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by guushamm on 6-1-16.
 */
public class KochServer implements Observer {
    //	ServerSocket serverSocket = null;
    boolean listening;
    KochManager manager;
    ArrayList<String> ips;
    KochProtocol kochProtocol;
    HashMap<Integer, SavableEdge> preCalculatedEdges;
    SavableEdge cache;
    PrintWriter out;
    int currentLvl = 0;
    SavableEdge currentCommand;


    public KochServer(KochManager manager) {
        listening = true;
        this.manager = manager;
//        ips = new ArrayList<>();
//        ips.add("145.93.169.152");
//        ips.add("145.93.88.207");

        preCalculatedEdges = new HashMap<>();

        int preCalculatedEdgesNumber = 8;
        for (int i = 1; i <= preCalculatedEdgesNumber; i++) {


            KochFractal kochFractal = new KochFractal();
            kochFractal.setLevel(i);
            kochFractal.addObserver(this);

            kochFractal.generateBottomEdge();
            kochFractal.generateLeftEdge();
            kochFractal.generateRightEdge();

            preCalculatedEdges.put(i, new SavableEdge(manager.getNewEdges(), i, manager.getNewEdges().size(), SavableEdge.Type.AllEdges));
            kochFractal.deleteObservers();
            kochFractal.cancel();
            System.out.println("lvl:" + i + "; should be:" + (3 * Math.pow(4, i - 1)) +"; is :" +manager.getNewEdges().size());
            manager.clearEdges();
        }


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
                    out = new PrintWriter(clientSocket.getOutputStream(), true);

                    SavableEdge command = kochProtocol.handleInput(input);
                    currentCommand = command;

                    KochFractal kochFractal = new KochFractal();
                    kochFractal.setLevel(command.getLevel());


                    String output;

//
//                    if (command.getType() == SavableEdge.Type.singleEdge) {
//
//                        kochFractal.addObserver(this);
//                        currentLvl = command.getLevel();
//                        int nrOfEdges = (int) (3 * Math.pow(4, currentLvl - 1));
////                        while (manager.getNewEdges().size() < nrOfEdges) {
////                            Thread.sleep(10);
////                            System.out.println("size" + );
////                        }
//                        kochFractal = new KochFractal();
//                        kochFractal.setLevel(command.getLevel());
//                        kochFractal.addObserver(this);
//
//                        kochFractal.generateBottomEdge();
//                        kochFractal.generateLeftEdge();
//                        kochFractal.generateRightEdge();
//
//                        output = kochProtocol.handleOutput(new SavableEdge(null, command.getLevel(), 0, SavableEdge.Type.stop));
//
//                    }else
                    if (command.getLevel() > preCalculatedEdgesNumber || command.getLevel()<4) {
                        kochFractal.addObserver(this);

                        kochFractal.generateBottomEdge();
                        kochFractal.generateLeftEdge();
                        kochFractal.generateRightEdge();
                        output = kochProtocol.handleOutput(new SavableEdge(manager.getNewEdges(), command.getLevel(), manager.getNewEdges().size(), SavableEdge.Type.AllEdges));

                    } else {
                        manager.setEdges(preCalculatedEdges.get(command.getLevel()).getEdges());
                        output = kochProtocol.handleOutput(new SavableEdge(manager.getNewEdges(), command.getLevel(), manager.getNewEdges().size(), SavableEdge.Type.AllEdges));

                    }



                    System.out.println("Size :" + manager.getNewEdges().size());
                    //System.out.println("ClientSideServer :" + output);
                    out.println(output);
                    output = kochProtocol.handleOutput(new SavableEdge(null, command.getLevel(), 0, SavableEdge.Type.stop));
                    out.println(output);

                    if(command.getType() == SavableEdge.Type.singleEdge){

                    }
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
        handleOutputPush(kochProtocol.handleOutput(savableEdge));
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
        KochManager manager = new KochManager(new JSF31KochFractalFX());
        new KochServer(manager);
    }

    @Override
    public void update(Observable o, Object object) {
        if (currentCommand != null && currentCommand.getType() == SavableEdge.Type.singleEdge) {
            manager.clearEdges();
        }
        Edge edge = (Edge) object;
        manager.addEdge(edge);
        if (currentCommand != null && currentCommand.getType() == SavableEdge.Type.singleEdge) {


            String output = kochProtocol.handleOutput(new SavableEdge(manager.getNewEdges(), currentLvl, manager.getNewEdges().size(), SavableEdge.Type.singleEdge));
            out.println(output);
            System.out.printf("sending a edge");
        }
    }
}
