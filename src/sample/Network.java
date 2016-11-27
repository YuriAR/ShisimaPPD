package sample;

/**
 * Created by yurireis on 13/11/16.
 */

import org.w3c.dom.css.ViewCSS;

import java.io.*;
import java.net.*;

public class Network{

    private Socket clientSocket;
    private DataOutputStream outToServer;
    private DataInputStream inFromServer;
    private NetworkMessaging listener;

//    private Boolean sentVictoryCondition = false;
//    private Boolean sentDrawCondition = false;

    public enum ConditionType{
        VICTORY,
        DRAW,
        COMPLAINT;
    }

    public Network(NetworkMessaging listener){
        this.listener = listener;
    }

    public void startConnection(){
        try{
            clientSocket = new Socket("localhost", 6789);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new DataInputStream(clientSocket.getInputStream());

            new Thread()
            {
                public void run() {
                    while (true){
                        try{
                            String received = inFromServer.readUTF();
                            receiveMessage(received);

                            Thread.sleep(2000);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }.start();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try{
            clientSocket.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //####### message - Code 1 ######### [message = 1-msg]
    public void handleChatMessage(String msg){
        listener.onChatMessageReceived(msg);
    }

    public void sendChatMessage(String message){
        if (message.equals("")){
            return;
        }
        try{
            outToServer.writeUTF("1-" + message);
            //outToServer.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //##### (move from - to) - Code  2 ###### [message = 2-from.to]
    private void handleMoveMessage(String message){
        Integer from = Integer.parseInt(message.split("\\.")[0]);
        Integer to = Integer.parseInt(message.split("\\.")[1].split("\\|")[0]);
        Integer reversed = Integer.parseInt(message.split("\\.")[1].split("\\|")[1]);
        listener.onMoveMessageReceived(from, to, reversed);
    }

    public void sendConditionMessage(ConditionType type){
        if (type == ConditionType.COMPLAINT){
            try {
                outToServer.writeUTF("3-3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type == ConditionType.VICTORY){
            try {
                outToServer.writeUTF("3-1");
//                sentVictoryCondition = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                outToServer.writeUTF("3-2");
//                sentDrawCondition = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMoveMessage(Integer from, Integer to, Integer reversed){
        try{
            outToServer.writeUTF("2-" + from.toString() + "." + to.toString() + "|" + reversed);
            //outToServer.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendConfirmationMessage(ConditionType type, Integer status){
        if (type == ConditionType.VICTORY){
            try {
                outToServer.writeUTF("4-" + status.toString() + "." + "0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type == ConditionType.DRAW){
            try {
                outToServer.writeUTF("4-" + status.toString() + "." + "1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleConfirmationMessage(String message){
        Integer status = Integer.parseInt(message.split("\\.")[0]);
        Integer type = Integer.parseInt(message.split("\\.")[1]);
        switch (type){
            case 0:
                listener.onConditionConfirmation(ConditionType.VICTORY, status);
                break;
            case 1:
                listener.onConditionConfirmation(ConditionType.DRAW, status);
                break;
        }
    }

    //###### Win(code 1), draw (code 2), complaint move (code 3) - Code 3 ####### [message = 3-code]
    private void handleConditionMessage(String message){
        Integer type = Integer.parseInt(message);
        if (type == 1){
            listener.onConditionMessageReceived(ConditionType.VICTORY);
//            if (sentVictoryCondition){
//                sentVictoryCondition = false;
//                listener.onConditionConfirmation(ConditionType.VICTORY);
//            }
//            else{
//
//            }
        }
        else if (type == 2){
            listener.onConditionMessageReceived(ConditionType.DRAW);
//            if (sentDrawCondition){
//                sentDrawCondition = false;
//                listener.onConditionConfirmation(ConditionType.DRAW);
//            }
//            else{
//
//            }
        }
        else{
            listener.onConditionMessageReceived(ConditionType.COMPLAINT);
        }
    }

    private void sendConditionMessage(Integer condition){

    }


    private void handleConnectionMessage(String message){
        listener.onConnectionMessageReceived(Integer.parseInt(message));
    }

    private void receiveMessage(String message){
        //Tratar tipo de msgs e chamar metodo certo [message = code-msg]
        System.out.print("Received Message: " + message);
        String code = message.split("-")[0];

        if (code.equals("0")){
            handleConnectionMessage(message.split("-")[1]);
        }
        else if (code.equals("1")){
            handleChatMessage(message.split("-")[1]);
        }
        else if (code.equals("2")){
            handleMoveMessage(message.split("-")[1]);
        }
        else if (code.equals("3")){
            handleConditionMessage(message.split("-")[1]);
        }
        else if (code.equals("4")){
            handleConfirmationMessage(message.split("-")[1]);
        }
    }

    public interface NetworkMessaging{
        void onChatMessageReceived(String msg);
        void onMoveMessageReceived(Integer from, Integer to, Integer reversed);
        void onConditionMessageReceived(ConditionType type);
        void onConnectionMessageReceived(Integer playerNumber);
        void onConditionConfirmation(ConditionType type, Integer status);
    }
}
