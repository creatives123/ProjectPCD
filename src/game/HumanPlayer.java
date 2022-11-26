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
            System.out.println("SOU HUMANO");
            // move do bot
            // Criamos uma thread de wait para prevenir que o bot fique parado eternamente.
            ThreadWait threadwait = new ThreadWait(this);
            try{

                threadwait.start();

                move(Direction.random().getVector());
                // move foi com sucesso então terminamos
                threadwait.interrupt();

                //Thread.sleep((long) ((Math.random() + 1)) * getIDPlayer() * 1000);
                //Multiplicamos o REFRESH_INTERVAL pelo initialstrenght para diferenciar os ciclos de movimento
                Thread.sleep(Game.REFRESH_INTERVAL * getCurrentStrength());
            } catch (InterruptedException e) {
                threadwait.interrupt();
            }



        }

        System.out.println("Fui morto" + this.getIDPlayer());
    }

    @Override
    public void setinitialposition() {
        // TODO Auto-generated method stub

    }
}
