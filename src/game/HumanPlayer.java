package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

import java.util.concurrent.CountDownLatch;

public class HumanPlayer extends Player {

    public HumanPlayer(int id, Game game, byte strength) {
        super(id, game, strength);
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean isHumanPlayer() {
        return true;
    }

    @Override
    public void move(Coordinate direction) throws InterruptedException {
        super.move(direction);
    }

    @Override
    public void run() {

        // TODO trocar isto porque tá igual ao BOT foi só para testar
        try {
            Cell initialPos=game.getRandomCell();
            initialPos.setPlayer(this);
        } catch (InterruptedException ignore) {
        }

        // O bot só executa enquanto o currentStrenght estiver entre ]0; 10[ e o jogo não tiver vencedores
        while (super.getCurrentStrength() != 0 && super.getCurrentStrength() != 10 && !game.Winners()){
            // move do bot
            try{
                Thread.sleep(1);
            } catch (InterruptedException ignore) {
            }



        }

        System.out.println("Fui morto" + this.getIDPlayer());
    }

    @Override
    public void setinitialposition() {
        // TODO Auto-generated method stub

    }
}
