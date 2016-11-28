package sample;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yurireis on 13/11/16.
 */
public class Board {

    private Map<Integer,Boolean> board = new HashMap<>();
    private List<Integer> localPlayerPieces = new ArrayList<>();
    private Boolean movingPiece = false;
    private Integer moveFrom;
    private Integer moveTo;
    private Boolean myTurn = true;
    private Integer myPlayer;

    public void initializeBoard(){
        for (int i=1;i<10;i++){
            if (i<4 || i > 6){
                board.put(i,true);
            }
            else{
                board.put(i,false);
            }

        }
        resetMove();
    }

    public void remoteMove(Integer from, Integer to){
        myTurn = false;
        moveFrom = from;
        moveTo = to;
        makeMove();
    }

    public Boolean beginMove(Integer from){
        if (canBeginMove(from)){
            moveFrom = from;
            movingPiece = true;
            return true;
        }
        return false;

    }

    public Boolean endMove(Integer to){
        if (canEndMove(to)){
            moveTo = to;
            makeMove();
            return true;
        }
        return false;
    }

    private Boolean canBeginMove(Integer from){
        return board.get(from) && localPlayerPieces.contains(from);
    }

    private Boolean canEndMove(Integer to){
        return !board.get(to) && movingPiece;
    }

    private void makeMove(){
        board.replace(moveFrom,false);
        board.replace(moveTo,true);
        if (myTurn){
            localPlayerPieces.remove(moveFrom);
            localPlayerPieces.add(moveTo);
        }
    }

    public void revertMove(){
        board.replace(moveFrom,true);
        board.replace(moveTo,false);
        localPlayerPieces.remove(moveTo);
        localPlayerPieces.add(moveFrom);
    }

    public void resetMove(){
        movingPiece = false;
        moveFrom = 0;
        moveTo = 0;
    }

    public Map<Integer, Boolean> getBoard() {
        return board;
    }

    public List<Integer> getLocalPlayerPieces() {
        return localPlayerPieces;
    }

    public void setMyTurn(Boolean myTurn) {
        this.myTurn = myTurn;
    }

    public Integer getMoveFrom() {
        return moveFrom;
    }

    public Integer getMoveTo() {
        return moveTo;
    }

    public void setLocalPlayer(){
        if (myPlayer == 1){
            localPlayerPieces.add(1);
            localPlayerPieces.add(2);
            localPlayerPieces.add(3);
        }
        else{
            localPlayerPieces.add(7);
            localPlayerPieces.add(8);
            localPlayerPieces.add(9);
        }
    }

    public void setMyPlayer(Integer myPlayer) {
        this.myPlayer = myPlayer;
    }

    public Integer getMyPlayer() {
        return myPlayer;
    }
}
