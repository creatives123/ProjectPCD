package gui.cliente;

import environment.Coordinate;
import game.Player;
import game.PlayerMinimal;
public class CellClient {
    private final GameClient game;
    private final Coordinate position;
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

    public void setPlayer(PlayerMinimal player) {
        this.player = player;
        game.notifyChange();
    }

    public void clearPlayer(){
        this.player = null;
        game.notifyChange();
    }

}
