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

    public void initializeBoard(){
        localPlayerPieces.add(1);
        localPlayerPieces.add(2);
        localPlayerPieces.add(3);
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
        localPlayerPieces.remove(moveFrom);
        localPlayerPieces.add(moveTo);
        resetMove();
    }

    public void revertMove(){
        board.replace(moveFrom,true);
        board.replace(moveTo,false);
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
}
