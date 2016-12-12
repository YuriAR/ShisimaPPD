package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

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
    Button buttonRestart;
    @FXML
    Button buttonSendMsg;
    @FXML
    GridPane gridCircles;

    Board board= new Board();
    Network network = new Network(this);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialize logic

        Line line1 = new Line(gridCircles.getChildren().get(0).getLayoutX(), gridCircles.getChildren().get(0).getLayoutY(), gridCircles.getChildren().get(1).getLayoutX(), gridCircles.getChildren().get(0).getLayoutY());
        gridCircles.getChildren().add(line1);
        network.startConnection();
        board.setMyPlayer(network.getMyPlayer());
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
                    network.sendChatMessage(chatField.getText(), board.getMyPlayer());
                }
                chatField.clear();
            }
        });

        buttonSendMsg.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!chatField.getText().equals("")){
                    chat.appendText("Você >> " + chatField.getText() + "\n");
                    network.sendChatMessage(chatField.getText(), board.getMyPlayer());
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
                network.sendConditionMessage(Network.ConditionType.COMPLAINT, board.getMyPlayer());
                disableSelection();
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
                network.sendConditionMessage(Network.ConditionType.VICTORY, board.getMyPlayer());
                disableSelection();
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
                disableSelection();
                network.sendConditionMessage(Network.ConditionType.DRAW, board.getMyPlayer());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("Você declarou empate! Aguardando confirmação...");
                    }
                });
            }
        });

        buttonRestart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                network.sendRestartMessage("0", board.getMyPlayer());
            }
        });
    }

    private void startGame(){
        board.setLocalPlayer();
        board.initializeBoard();

        buttonWin.setDisable(false);
        buttonDraw.setDisable(false);

        //Print jogo iniciado
        if (board.getMyPlayer() == 1){
            enableSelection();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Jogo iniciado! Você começa");
                }
            });
        }
        else{
            disableSelection();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Jogo iniciado! Aguardando vez do jogador remoto...");
                }
            });
        }
        refreshUI();
    }

    private void handleMove(Integer space, Circle circle){
        board.setMyTurn(true);
        if (board.beginMove(space)){
            circle.setStroke(Color.YELLOW);
            circle.setStrokeWidth(2);
        }
        if (board.endMove(space)){
            disableSelection();
            network.sendMoveMessage(board.getMoveFrom(),board.getMoveTo(), 0, board.getMyPlayer());
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

//                Line line = new Line();
//                line.startXProperty().bind(circle1.centerXProperty().add(circle1.translateXProperty()));
//                line.startYProperty().bind(circle1.centerYProperty().add(circle1.translateYProperty()));
//                line.endXProperty().bind(circle2.centerXProperty().add(circle2.translateXProperty()));
//                line.endYProperty().bind(circle2.centerYProperty().add(circle2.translateYProperty()));
//
//                gridCircles.getChildren().add(line);
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
            network.sendMoveMessage(board.getMoveTo(),board.getMoveFrom(), 1, board.getMyPlayer());
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
                        disableSelection();
                        buttonWin.setDisable(true);
                        buttonDraw.setDisable(true);
                        network.sendConfirmationMessage(Network.ConditionType.VICTORY, 1, board.getMyPlayer());
                        alert.close();
                    }

                    if (alert.getResult() == ButtonType.NO){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                statusText.setText("Vitória rejeitada. O jogo continua...");
                            }
                        });
                        //disableSelection();
                        network.sendConfirmationMessage(Network.ConditionType.VICTORY, 0, board.getMyPlayer());
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
                        disableSelection();
                        buttonWin.setDisable(true);
                        buttonDraw.setDisable(true);
                        network.sendConfirmationMessage(Network.ConditionType.DRAW, 1, board.getMyPlayer());
                        alert.close();
                    }

                    if (alert.getResult() == ButtonType.NO){
                        network.sendConfirmationMessage(Network.ConditionType.DRAW, 0, board.getMyPlayer());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                statusText.setText("Empate rejeitado. O jogo continua...");
                            }
                        });
                        //disableSelection();
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
        board.setMyPlayer(playerNumber);
        startGame();
    }

    @Override
    public void onDisconnectionMessageReceived() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Player remoto desconectou! Deseja procurar outro player?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    network.startConnection();
                    disableSelection();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            statusText.setText("Aguardando player remoto...");
                        }
                    });
                    alert.close();
                }

                if (alert.getResult() == ButtonType.NO){
                    //Close app
                    alert.close();
                    Platform.exit();
                    System.exit(0);
                }
            }
        });
    }

    @Override
    public void onConditionConfirmationReceived(Network.ConditionType type, Integer status) {
        if (type == Network.ConditionType.VICTORY){
            if (status == 1){
                //GANHEI
                disableSelection();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("VOCÊ GANHOU!!");
                    }
                });
                buttonWin.setDisable(true);
                buttonDraw.setDisable(true);
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
                disableSelection();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("EMPATE :(");
                    }
                });
                buttonWin.setDisable(true);
                buttonDraw.setDisable(true);
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

    @Override
    public void onRestartMessageReceived(String message) {
        if (message.equals("0")) {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Player remoto deseja reiniciar a partida! Aceita?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        network.sendRestartMessage("1", board.getMyPlayer());
                        startGame();
                        alert.close();
                    }

                    if (alert.getResult() == ButtonType.NO){
                        //Close app
                        network.sendRestartMessage("2", board.getMyPlayer());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                statusText.setText("Reinício rejeitado!");
                            }
                        });
                        alert.close();
                    }
                }
            });
        }
        else if (message.equals("1")){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Reiniciando jogo...");
                }
            });
            startGame();
        }
        else{
            //request para restart rejeitado
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Reinício rejeitado!");
                }
            });
        }
    }
}
