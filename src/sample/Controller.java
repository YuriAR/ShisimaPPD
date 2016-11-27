package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    @FXML
    Label statusText;
    @FXML
    Button buttonWin;
    @FXML
    Button buttonDraw;
    @FXML
    Button buttonComplaint;
    @FXML
    Button buttonAccept;
    @FXML
    Button buttonSendMsg;

    Board board= new Board();
    Network network = new Network(this);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialize logic

        network.startConnection();
        disableSelection();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statusText.setText("Aguardando player remoto...");
            }
        });

        chat.setEditable(false);
        chat.setMouseTransparent(true);
        chat.setFocusTraversable(false);

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

        buttonSendMsg.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
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
                handleMove(1, circle1);
            }
        });

        circle2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleMove(2, circle2);
            }
        });

        circle3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleMove(3, circle3);
            }
        });

        circle4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleMove(4, circle4);
            }
        });

        circle5.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleMove(5, circle5);
            }
        });

        circle6.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleMove(6, circle6);
            }
        });

        circle7.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleMove(7, circle7);
            }
        });

        circle8.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleMove(8, circle8);
            }
        });

        circle9.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleMove(9, circle9);
            }
        });

        buttonComplaint.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Enviar mensagem de reclamacao
                network.sendConditionMessage(Network.ConditionType.COMPLAINT);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("Você não gostou da jogada, revertendo...");
                    }
                });
            }
        });

        buttonWin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Enviar mensagem de reclamacao
                network.sendConditionMessage(Network.ConditionType.VICTORY);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("Você se declarou vitorioso! Aguardando confirmação");
                    }
                });
            }
        });

        buttonDraw.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Enviar mensagem de reclamacao
                network.sendConditionMessage(Network.ConditionType.DRAW);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("Você declarou empate! Aguardando confirmação...");
                    }
                });
            }
        });

        buttonAccept.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Enviar mensagem de reclamacao

//                statusText.setText("Você não gostou da jogada, revertendo...");
            }
        });
    }

    private void handleMove(Integer space, Circle circle){
        board.setMyTurn(true);
        if (board.beginMove(space)){
            circle.setStroke(Color.YELLOW);
            circle.setStrokeWidth(2);
        }
        if (board.endMove(space)){
            disableSelection();
            network.sendMoveMessage(board.getMoveFrom(),board.getMoveTo(), 0);
            refreshUI();
            resetSelection();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Aguardando movimento do outro jogador...!");
                }
            });
        }
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

    public void enableSelection(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                buttonWin.setDisable(false);
                buttonAccept.setDisable(false);
                buttonDraw.setDisable(false);
                buttonComplaint.setDisable(false);
                circle1.setDisable(false);
                circle2.setDisable(false);
                circle3.setDisable(false);
                circle4.setDisable(false);
                circle5.setDisable(false);
                circle6.setDisable(false);
                circle7.setDisable(false);
                circle8.setDisable(false);
                circle9.setDisable(false);
            }
        });
    }

    public void disableSelection(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                buttonWin.setDisable(true);
                buttonAccept.setDisable(true);
                buttonDraw.setDisable(true);
                buttonComplaint.setDisable(true);
                circle1.setDisable(true);
                circle2.setDisable(true);
                circle3.setDisable(true);
                circle4.setDisable(true);
                circle5.setDisable(true);
                circle6.setDisable(true);
                circle7.setDisable(true);
                circle8.setDisable(true);
                circle9.setDisable(true);
            }
        });
    }

    public void refreshUI(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    @Override
    public void onChatMessageReceived(String msg) {
        chat.appendText("Player remoto >> " + msg + "\n");
    }

    @Override
    public void onConditionMessageReceived(Network.ConditionType type) {
        if (type == Network.ConditionType.COMPLAINT){
            board.revertMove();
            network.sendMoveMessage(board.getMoveTo(),board.getMoveFrom(), 1);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Jogada revertida. Sua vez!");
                }
            });
            enableSelection();
            refreshUI();
        }
        else if (type == Network.ConditionType.VICTORY){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Player remoto está se declarando vitorioso. Você aceita?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        //PERDI
                        //1 = confirmado
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                statusText.setText("DERROTA :(");
                            }
                        });
                        network.sendConfirmationMessage(Network.ConditionType.VICTORY, 1);
                        alert.close();
                    }

                    if (alert.getResult() == ButtonType.NO){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                statusText.setText("Vitória rejeitada. O jogo continua...");
                            }
                        });
                        network.sendConfirmationMessage(Network.ConditionType.VICTORY, 0);
                        alert.close();
                    }
                }
            });

        }
        else if (type == Network.ConditionType.DRAW){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Player remoto está declarando empate. Você aceita?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        //EMPATE
                        //1 = confirmado
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                statusText.setText("EMPATE :(");
                            }
                        });
                        network.sendConfirmationMessage(Network.ConditionType.DRAW, 1);
                        alert.close();
                    }

                    if (alert.getResult() == ButtonType.NO){
                        network.sendConfirmationMessage(Network.ConditionType.DRAW, 0);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                statusText.setText("Empate rejeitado. O jogo continua...");
                            }
                        });
                        alert.close();
                    }
                }
            });
        }
    }

    @Override
    public void onMoveMessageReceived(Integer from, Integer to, Integer reversed) {
        if (reversed == 0){
            board.resetMove();
            board.remoteMove(from,to);
            enableSelection();
            //Print sua vez
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Sua vez!");
                }
            });
            refreshUI();
        }
        else{
            board.resetMove();
            board.remoteMove(from,to);
            disableSelection();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Movimento revertido! Aguardando jogador remoto...");
                }
            });
            refreshUI();
        }

    }

    @Override
    public void onConnectionMessageReceived(Integer playerNumber) {
        board.setLocalPlayer(playerNumber);
        board.initializeBoard();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statusText.setText("Jogo iniciado!");
            }
        });

        //Print jogo iniciado
        refreshUI();
        enableSelection();
    }

    @Override
    public void onConditionConfirmation(Network.ConditionType type, Integer status) {
        if (type == Network.ConditionType.VICTORY){
            if (status == 1){
                //GANHEI
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("VOCÊ GANHOU!!");
                    }
                });
            }
            else{
                //NAO CONFIRMADO, CONTINUA O JOGO
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("Vitória rejeitada. O jogo continua...");
                    }
                });
            }
        }
        else if (type == Network.ConditionType.DRAW){
            if (status == 1){
                //EMPATE
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("EMPATE :(");
                    }
                });
            }
            else{
                //NAO CONFIRMADO, CONTINUA O JOGO
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("Empate rejeitado. O jogo continua...");
                    }
                });
            }
        }
    }
}
