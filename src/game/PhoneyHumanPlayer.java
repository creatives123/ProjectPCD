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
			game.addPlayerToGame(this);
		} catch (InterruptedException e1) {
			System.out.println("Sou o " + getIDPlayer() + " e estou à espera \n");
			e1.printStackTrace();
		}

		while (super.getCurrentStrength() != 0){
			try {
				//Thread.sleep((long) ((Math.random() + 1)) * getIDPlayer() * 1000);
				//Multiplicamos o REFRESH_INTERVAL pelo initialstrenght para diferenciar os ciclos de movimento
				Thread.sleep(Game.REFRESH_INTERVAL * initialstrenght);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			move(Direction.random().getVector());
			System.out.println("Bot id: " + getIDPlayer() + " still Alive " + this);
		}
		System.out.println("End Bot ID: " + getIDPlayer());
	}

	@Override
	public void move(Coordinate direction) {
		super.move(direction);
	}
}
