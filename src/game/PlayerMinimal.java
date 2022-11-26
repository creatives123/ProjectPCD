/**
 * Created by Alexandre Torres on IntelliJ IDEA.
 * Date: 25/11/2022
 * Time: 00:02
 * <p>
 * Details of the file:
 */
package game;

import environment.Coordinate;

import java.io.Serializable;

public class PlayerMinimal extends Player implements Serializable {
    private byte currentStrength;
    protected byte originalStrength;
    private Coordinate posicao;
    private boolean type;

    public PlayerMinimal (Player player){
        super(player.getIDPlayer(), null, player.getCurrentStrength());
        currentStrength = player.getCurrentStrength();
        posicao = player.getCurrentCell().getPosition();
        type = player.isHumanPlayer();
    }

    @Override
    public boolean isHumanPlayer() {
        return type;
    }

    @Override
    public void run() {

    }
}
