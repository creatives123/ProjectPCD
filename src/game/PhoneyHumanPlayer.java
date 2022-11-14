package game;

import java.util.Random;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class PhoneyHumanPlayer extends Player {
	//Variável para guardar a força com qual o player começa. Importante para o move
	int initialstrenght;


	public PhoneyHumanPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
		initialstrenght = (int) strength;
	}

	public boolean isHumanPlayer() {
		return false;
	}

	@Override
	public void run() {
		try {
			Cell initialPos=game.getRandomCell();
			initialPos.setPlayer(this);
		} catch (InterruptedException ignore) {
		}

		// O bot só executa enquanto o currentStrenght estiver entre ]0; 10[ e o jogo não tiver vencedores
		while (super.getCurrentStrength() != 0 && super.getCurrentStrength() != 10 && !game.Winners()){
			try {
				//Thread.sleep((long) ((Math.random() + 1)) * getIDPlayer() * 1000);
				//Multiplicamos o REFRESH_INTERVAL pelo initialstrenght para diferenciar os ciclos de movimento
				Thread.sleep(Game.REFRESH_INTERVAL * initialstrenght);
			} catch (InterruptedException ignore) {
			}

			// move do bot
			// Criamos uma thread de wait para prevenir que o bot fique parado eternamente.
			ThreadWait threadwait = new ThreadWait(this);
			try{

				threadwait.start();

				move(Direction.random().getVector());
				// move foi com sucesso então terminamos
				threadwait.interrupt();
			} catch (InterruptedException e) {
				threadwait.interrupt();
			}

		}
	}

	@Override
	public void move(Coordinate direction) throws InterruptedException {
		super.move(direction);
	}
}
