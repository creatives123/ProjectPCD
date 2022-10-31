package game;

import environment.Cell;

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
				Thread.sleep((long) ((Math.random() + 1)) * getIDPlayer() * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			move();
			System.out.println("Bot id: " + getIDPlayer() + " still Alive " + this);
		}
		System.out.println("End Bot ID: " + getIDPlayer());
	}

	@Override
	public void move() {
		Cell newCell = game.getRandomCell();
		game.getCell(this.getCurrentCell().getPosition()).removePlayer();
		newCell.setPlayer(this);
		this.updatePosition(newCell.getPosition());
		game.notifyChange();
	}
}
