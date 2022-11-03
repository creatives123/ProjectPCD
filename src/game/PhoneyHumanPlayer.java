package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class PhoneyHumanPlayer extends Player {
	public PhoneyHumanPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return false;
	}

	@Override
	public void run() {
		System.out.println("Start Bot ID: " + getIDPlayer());
		game.addPlayerToGame(this);

		while (super.getCurrentStrength() != 0){
			try {
				//Thread.sleep((long) ((Math.random() + 1)) * getIDPlayer() * 1000);
				Thread.sleep(Game.REFRESH_INTERVAL);
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
