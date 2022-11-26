package gui.cliente;

import environment.Coordinate;
import game.PlayerMinimal;
public class CellClient {
    private final GameClient game;
    private Coordinate position;
    private PlayerMinimal player = null;

    public CellClient(Coordinate position, GameClient g) {
        super();
        this.position = position;
        this.game = g;


    }

    public Coordinate getPosition() {
        return position;
    }

    public PlayerMinimal getPlayer() {
        return player;
    }

}
