package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, Network.NetworkMessaging{

    @FXML
    Circle circle1;
    @FXML
    Circle circle2;
    @FXML
    Circle circle3;
    @FXML
    Circle circle4;
    @FXML
    Circle circle5;
    @FXML
    Circle circle6;
    @FXML
    Circle circle7;
    @FXML
    Circle circle8;
    @FXML
    Circle circle9;
    @FXML
    TextField chatField;
    @FXML
    TextArea chat;

    Board board= new Board();
    Network network = new Network(this);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialize logic

        network.startConnection();

        chat.setEditable(false);
        chat.setMouseTransparent(true);
        chat.setFocusTraversable(false);

        board.initializeBoard();
        refreshUI();

        chatField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!chatField.getText().equals("")){
                    chat.appendText("Você >> " + chatField.getText() + "\n");
                    network.sendChatMessage(chatField.getText());
                }
                chatField.clear();
            }
        });

        circle1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (board.beginMove(1)){

                    circle1.setStroke(Color.YELLOW);
                    circle1.setStrokeWidth(2);
                }
                if (board.endMove(1)){
                    refreshUI();
                    resetSelection();
                }
            }
        });

        circle2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (board.beginMove(2)){
                    circle2.setStroke(Color.YELLOW);
                    circle2.setStrokeWidth(2);
                }
                if (board.endMove(2)){
                    refreshUI();
                    resetSelection();
                }
            }
        });

        circle3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (board.beginMove(3)){
                    circle3.setStroke(Color.YELLOW);
                    circle3.setStrokeWidth(2);
                }
                if (board.endMove(3)){
                    refreshUI();
                    resetSelection();
                }
            }
        });

        circle4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (board.beginMove(4)){
                    circle4.setStroke(Color.YELLOW);
                    circle4.setStrokeWidth(2);
                }
                if (board.endMove(4)){
                    refreshUI();
                    resetSelection();
                }
            }
        });

        circle5.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (board.beginMove(5)){
                    circle5.setStroke(Color.YELLOW);
                    circle5.setStrokeWidth(2);
                }
                if (board.endMove(5)){
                    refreshUI();
                    resetSelection();
                }
            }
        });

        circle6.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (board.beginMove(6)){
                    circle6.setStroke(Color.YELLOW);
                    circle6.setStrokeWidth(2);
                }
                if (board.endMove(6)){
                    refreshUI();
                    resetSelection();
                }
            }
        });

        circle7.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (board.beginMove(7)){
                    circle7.setStroke(Color.YELLOW);
                    circle7.setStrokeWidth(2);
                }
                if (board.endMove(7)){
                    refreshUI();
                    resetSelection();
                }
            }
        });

        circle8.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (board.beginMove(8)){
                    circle8.setStroke(Color.YELLOW);
                    circle8.setStrokeWidth(2);
                }
                if (board.endMove(8)){
                    refreshUI();
                    resetSelection();
                }
            }
        });

        circle9.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (board.beginMove(9)){
                    circle9.setStroke(Color.YELLOW);
                    circle9.setStrokeWidth(2);
                }
                if (board.endMove(9)){
                    refreshUI();
                    resetSelection();
                }
            }
        });
    }

    public void resetSelection(){
        circle1.setStroke(Color.BLACK);
        circle2.setStroke(Color.BLACK);
        circle3.setStroke(Color.BLACK);
        circle4.setStroke(Color.BLACK);
        circle5.setStroke(Color.BLACK);
        circle6.setStroke(Color.BLACK);
        circle7.setStroke(Color.BLACK);
        circle8.setStroke(Color.BLACK);
        circle9.setStroke(Color.BLACK);
    }

    public void refreshUI(){
        circle1.setFill(board.getBoard().get(1) ? board.getLocalPlayerPieces().contains(1) ? Color.RED : Color.BLUE : Color.BLACK);
        circle2.setFill(board.getBoard().get(2) ? board.getLocalPlayerPieces().contains(2) ? Color.RED : Color.BLUE : Color.BLACK);
        circle3.setFill(board.getBoard().get(3) ? board.getLocalPlayerPieces().contains(3) ? Color.RED : Color.BLUE : Color.BLACK);
        circle4.setFill(board.getBoard().get(4) ? board.getLocalPlayerPieces().contains(4) ? Color.RED : Color.BLUE : Color.BLACK);
        circle5.setFill(board.getBoard().get(5) ? board.getLocalPlayerPieces().contains(5) ? Color.RED : Color.BLUE : Color.BLACK);
        circle6.setFill(board.getBoard().get(6) ? board.getLocalPlayerPieces().contains(6) ? Color.RED : Color.BLUE : Color.BLACK);
        circle7.setFill(board.getBoard().get(7) ? board.getLocalPlayerPieces().contains(7) ? Color.RED : Color.BLUE : Color.BLACK);
        circle8.setFill(board.getBoard().get(8) ? board.getLocalPlayerPieces().contains(8) ? Color.RED : Color.BLUE : Color.BLACK);
        circle9.setFill(board.getBoard().get(9) ? board.getLocalPlayerPieces().contains(9) ? Color.RED : Color.BLUE : Color.BLACK);
    }

    @Override
    public void onChatMessageReceived(String msg) {
        chat.appendText("Player remoto >> " + msg + "\n");
    }

    @Override
    public void onConditionMessageReceived() {

    }

    @Override
    public void onMoveMessageReceived() {

    }
}
