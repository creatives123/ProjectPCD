package gui.cliente.environment;

import environment.Coordinate;
import environment.PlayerMinimal;
import gui.cliente.GameClient;

//Classe CellCliente é uma classe simplificada da Classe Cell do Game
//Apenas tem métodos simples para permitir a construção do jogo
public class CellClient {
    private final GameClient game;
    private final Coordinate position;
    private PlayerMinimal player = null;

    public CellClient(Coordinate position, GameClient g) {
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
