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
		System.out.println("Start Bot ID: " + getIDPlayer());
		try {
			Cell initialPos=game.getRandomCell();
			initialPos.setPlayer(this);
		} catch (InterruptedException e1) {
			System.out.println("Sou o " + getIDPlayer() + " e estou à espera \n");
			e1.printStackTrace();
		}

		while (super.getCurrentStrength() != 0 && super.getCurrentStrength() != 10){
			try {
				//Thread.sleep((long) ((Math.random() + 1)) * getIDPlayer() * 1000);
				//Multiplicamos o REFRESH_INTERVAL pelo initialstrenght para diferenciar os ciclos de movimento
				Thread.sleep(Game.REFRESH_INTERVAL * initialstrenght);
			} catch (InterruptedException ignored) {
			}
			ThreadWait threadwait = new ThreadWait(this);
			threadwait.start();

			try {
				move(Direction.random().getVector());
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			threadwait.interrupt();
		}
	}

	@Override
	public void move(Coordinate direction) throws InterruptedException {
		super.move(direction);
	}
}
