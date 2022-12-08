/**
 * Created by Alexandre Torres on IntelliJ IDEA.
 * Date: 25/11/2022
 * Time: 00:02
 * <p>
 * Details of the file:
 */
package game;

import environment.Coordinate;
import gui.cliente.CellClient;

import java.io.Serializable;

public class PlayerMinimal implements Serializable {
    private byte currentStrength;
    protected byte originalStrength;
    private Coordinate posicao;
    private boolean type;
    public int id;

    public PlayerMinimal (Player player){
        id = player.getIDPlayer();
        currentStrength = player.getCurrentStrength();
        posicao = player.getCurrentCell().getPosition();
        type = player.isHumanPlayer();
    }

    public boolean isHumanPlayer() {
        return type;
    }

    public byte getCurrentStrength() {
        return currentStrength;
    }

    public Coordinate getCurrentCell() {
        return posicao;
    }

    public int getIdentification() {
        return id;
    }
}
