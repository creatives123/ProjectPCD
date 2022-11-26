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

import java.io.IOException;

public class GameClient {
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

    
}
