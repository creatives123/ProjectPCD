package game;

import environment.Coordinate;

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
