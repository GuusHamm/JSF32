package jsf32kochfractalfx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    public void sendMessage(SavableEdge command) {
        try {
            manager.clearEdges();
            Socket socket = new Socket("127.0.0.1", 4444);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String output = KochProtocol.getInstance().handleOutput(command);
            out.println(output);

            String feedback = in.readLine();
//            System.out.println("The client received this as feedback :" + feedback);

            SavableEdge savableEdgeReturn = kochProtocol.handleInput(feedback);

            if(savableEdgeReturn.getType()== SavableEdge.Type.AllEdges) {
                manager.setEdges(savableEdgeReturn.getEdges());

            }else if(savableEdgeReturn.getType() == SavableEdge.Type.singleEdge){

                while(savableEdgeReturn.getType() != SavableEdge.Type.stop){
                    System.out.println("while hit");
                    manager.addEdge(savableEdgeReturn.getEdges().get(0));
                    System.out.println("edge :" + savableEdgeReturn.getEdges().get(0).X1);
                    manager.drawEdge(savableEdgeReturn.getEdges().get(0));
                    feedback = in.readLine();
                    savableEdgeReturn = kochProtocol.handleInput(feedback);
                }
            }

            in.close();
            out.close();
            socket.close();
            System.out.println("sizeeeeee:" + manager.getNewEdges().size());
            if(savableEdgeReturn.getType() != SavableEdge.Type.singleEdge) {
                manager.drawEdges(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

