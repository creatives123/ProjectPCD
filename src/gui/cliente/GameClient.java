/**
 * Created by Alexandre Torres on IntelliJ IDEA.
 * Date: 26/11/2022
 * Time: 12:14
 * <p>
 * Details of the file:
 */
package gui.cliente;
import environment.Coordinate;
import game.Game;
import game.PlayerMinimal;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class GameClient extends Observable {
    public static final int DIMY = 30;
    public static final int DIMX = 30;
    protected CellClient[][] board;

    public GameClient() throws IOException {
        board = new CellClient[GameClient.DIMX][GameClient.DIMY];

        for (int x = 0; x < Game.DIMX; x++)
            for (int y = 0; y < Game.DIMY; y++)
                board[x][y] = new CellClient(new Coordinate(x, y), this);
    }

    public CellClient getCell(Coordinate at) {
        return board[at.x][at.y];
    }

    public void updateBoard(List<PlayerMinimal> listaplayers) throws InterruptedException {
        clearBoard();
        for(PlayerMinimal player: listaplayers){
            getCell(player.getCurrentCell()).setPlayer(player);
        }
    }

    private void clearBoard(){
        for (int x = 0; x < Game.DIMX; x++)
            for (int y = 0; y < Game.DIMY; y++)
                board[x][y].clearPlayer();
    }

    public void notifyChange() {
        setChanged();
        notifyObservers();
    }
}
