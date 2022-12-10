package environment;

import java.io.Serializable;

//Criação do jogador PlayerMinimal que vai ser usado para enviar para o Cliente
public class PlayerMinimal implements Serializable {
    private final byte currentStrength;
    private final Coordinate posicao;
    private final boolean type;
    public int id;

    public PlayerMinimal (int id, byte currentStrength, int x, int y, boolean isHuman){
        this.id = id;
        this.currentStrength = currentStrength;
        this.posicao = new Coordinate(x,y);
        this.type = isHuman;
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
