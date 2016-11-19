package sample;

/**
 * Created by yurireis on 13/11/16.
 */

import java.io.*;
import java.net.*;

public class Network{

    private Socket clientSocket;
    private DataOutputStream outToServer;
    private DataInputStream inFromServer;
    private NetworkMessaging listener;

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
    private void handleMoveMessage(){
        listener.onMoveMessageReceived();
    }

    public void sendMoveMessage(Integer from, Integer to){

    }

    //###### Win(code 1), draw (code 2), complaint move (code 3), confirm move (code 4) - Code 3 ####### [message = 3-code]
    private void handleConditionMessage(){
        listener.onConditionMessageReceived();
    }

    private void sendConditionMessage(Integer condition){

    }

    private void receiveMessage(String message){
        //Tratar tipo de msgs e chamar metodo certo [message = code-msg]
        System.out.print("Received Message: " + message);
        if (message.split("-")[0].equals("1")){
            handleChatMessage(message.split("-")[1]);
        }
    }

    public interface NetworkMessaging{
        void onChatMessageReceived(String msg);
        void onMoveMessageReceived();
        void onConditionMessageReceived();
    }
}
