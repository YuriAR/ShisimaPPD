package sample;

/**
 * Created by yurireis on 13/11/16.
 */

import org.w3c.dom.css.ViewCSS;

import java.io.*;
//import java.net.*;
import java.rmi.*;

public class Network{

//    private Socket clientSocket;
//    private DataOutputStream outToServer;
//    private DataInputStream inFromServer;
    private MessagingInterface messagingInterface;
    private NetworkMessaging listener;

    public enum ConditionType{
        VICTORY,
        DRAW,
        COMPLAINT
    }

    public Network(NetworkMessaging listener){
        this.listener = listener;
    }

    public void startConnection(){
        try{
            messagingInterface = (MessagingInterface)Naming.lookup("//localhost/Messaging");
            System.out.println("Objeto localizado");

//            clientSocket = new Socket("localhost", 6789);
//            outToServer = new DataOutputStream(clientSocket.getOutputStream());
//            inFromServer = new DataInputStream(clientSocket.getInputStream());
//
//            new Thread()
//            {
//                public void run() {
//                    while (true){
//                        try{
//                            String received = inFromServer.readUTF();
//                            receiveMessage(received);
//
//                            Thread.sleep(2000);
//                        }
//                        catch (Exception e){
//                            e.printStackTrace();
//                            break;
//                        }
//                    }
//                }
//            }.start();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void closeConnection(){
//        try{
//            clientSocket.close();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    public Integer getMyPlayer(){
        try {
            Integer myPlayer = messagingInterface.getMyPlayer();
            Thread readingThread = new ReadBufferThread(myPlayer);
            readingThread.start();
            return myPlayer;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    //####### Send messages

    public void sendChatMessage(String message, Integer player){
        if (message.equals("")){
            return;
        }
        try{
            messagingInterface.sendRemoteMessage("1-" + message, player);
            //outToServer.writeUTF("1-" + message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    public void sendConditionMessage(ConditionType type, Integer player){
        if (type == ConditionType.COMPLAINT){
            try {
                messagingInterface.sendRemoteMessage("3-3", player);
                //outToServer.writeUTF("3-3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type == ConditionType.VICTORY){
            try {
                messagingInterface.sendRemoteMessage("3-1", player);
                //outToServer.writeUTF("3-1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                messagingInterface.sendRemoteMessage("3-2", player);
                //outToServer.writeUTF("3-2");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMoveMessage(Integer from, Integer to, Integer reversed, Integer player){
        try{
            messagingInterface.sendRemoteMessage("2-" + from.toString() + "." + to.toString() + "|" + reversed, player);
            //outToServer.writeUTF("2-" + from.toString() + "." + to.toString() + "|" + reversed);
            //outToServer.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendConfirmationMessage(ConditionType type, Integer status, Integer player){
        if (type == ConditionType.VICTORY){
            try {
                messagingInterface.sendRemoteMessage("4-" + status.toString() + "." + "0", player);
                //outToServer.writeUTF("4-" + status.toString() + "." + "0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type == ConditionType.DRAW){
            try {
                messagingInterface.sendRemoteMessage("4-" + status.toString() + "." + "1", player);
                //outToServer.writeUTF("4-" + status.toString() + "." + "1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendRestartMessage(String confirmation, Integer player){
        try {
            messagingInterface.sendRemoteMessage("6-" + confirmation, player);
            //outToServer.writeUTF("6-" + confirmation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage(String message){
        //Tratar tipo de msgs e chamar metodo certo [message = code-msg]
        System.out.print("Received Message: " + message);
        String code = message.split("-")[0];

        if (code.equals("0")){
            //connection - Code 0 ####### [message = 0-player]
            handleConnectionMessage(message.split("-")[1]);
        }
        else if (code.equals("1")){
            //message - Code 1 ######### [message = 1-msg]
            handleChatMessage(message.split("-")[1]);
        }
        else if (code.equals("2")){
            //move - Code  2 ###### [message = 2-from.to]
            handleMoveMessage(message.split("-")[1]);
        }
        else if (code.equals("3")){
            //Win(code 1), draw (code 2), complaint move (code 3) - Code 3 ####### [message = 3-code]
            handleConditionMessage(message.split("-")[1]);
        }
        else if (code.equals("4")){
            //Code 4 ########## [message = 4-status.type]
            handleConfirmationMessage(message.split("-")[1]);
        }
        else if (code.equals("5")){
            //Code 5 ######### [message = 5]
            handleDisconnectionMessage();
        }
        else if (code.equals("6")){
            //Code 6 ######### [message = 6-status]
            handleRestartMessage(message.split("-")[1]);
        }
    }

    //######### Handlers

    //connection - Code 0 ####### [message = 0-player]
    private void handleConnectionMessage(String message){
        listener.onConnectionMessageReceived(Integer.parseInt(message));
    }

    //Code 5 ######### [message = 5]
    private void handleDisconnectionMessage(){
        listener.onDisconnectionMessageReceived();
    }

    //Win(code 1), draw (code 2), complaint move (code 3) - Code 3 ####### [message = 3-code]
    private void handleConditionMessage(String message){
        Integer type = Integer.parseInt(message);
        if (type == 1){
            listener.onConditionMessageReceived(ConditionType.VICTORY);
        }
        else if (type == 2){
            listener.onConditionMessageReceived(ConditionType.DRAW);
        }
        else{
            listener.onConditionMessageReceived(ConditionType.COMPLAINT);
        }
    }

    //Code 4 ########## [message = 4-status.type]
    private void handleConfirmationMessage(String message){
        Integer status = Integer.parseInt(message.split("\\.")[0]);
        Integer type = Integer.parseInt(message.split("\\.")[1]);
        switch (type){
            case 0:
                listener.onConditionConfirmationReceived(ConditionType.VICTORY, status);
                break;
            case 1:
                listener.onConditionConfirmationReceived(ConditionType.DRAW, status);
                break;
        }
    }

    //move - Code  2 ###### [message = 2-from.to]
    private void handleMoveMessage(String message){
        Integer from = Integer.parseInt(message.split("\\.")[0]);
        Integer to = Integer.parseInt(message.split("\\.")[1].split("\\|")[0]);
        Integer reversed = Integer.parseInt(message.split("\\.")[1].split("\\|")[1]);
        listener.onMoveMessageReceived(from, to, reversed);
    }

    //message - Code 1 ######### [message = 1-msg]
    private void handleChatMessage(String msg){
        listener.onChatMessageReceived(msg);
    }

    private void handleRestartMessage(String msg){
        listener.onRestartMessageReceived(msg);
    }

    public class ReadBufferThread extends Thread{
        Integer player;
        Boolean shouldContinue = true;

        public ReadBufferThread(Integer player){
            this.player = player;
        }

        public void run(){
            try {
                while (shouldContinue){
                    String message = messagingInterface.getRemoteMessage(player);
                    if (message != null){
                        receiveMessage(message);
                    }
                }
                Thread.sleep(1000);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        public void stopReading(){
            shouldContinue = false;
        }
    }

    public interface NetworkMessaging{
        void onChatMessageReceived(String msg);
        void onMoveMessageReceived(Integer from, Integer to, Integer reversed);
        void onConditionMessageReceived(ConditionType type);
        void onConnectionMessageReceived(Integer playerNumber);
        void onConditionConfirmationReceived(ConditionType type, Integer status);
        void onDisconnectionMessageReceived();
        void onRestartMessageReceived(String message);
    }
}
