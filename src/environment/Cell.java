package environment;

import game.Game;
import game.Player;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player=null;

	private final Lock lock;
	private final Condition isEmpty;
	private final Condition isFull;

	public Cell(Coordinate position,Game g) {
		super();
		this.position = position;
		this.game=g;

		lock = new ReentrantLock();
		isEmpty = lock.newCondition();
		isFull = lock.newCondition();
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOcupied() {
		return player!=null;
	}


	public Player getPlayer() {
		return player;
	}

	//TODO Should not be used like this in the initial state: cell might be occupied, must coordinate this operation
	public void setPlayer(Player player) throws InterruptedException {
		lock.lock();
		while(this.isOcupied()){
			System.out.print("Sou o " + player.getIDPlayer() + " e tenho a Celula " + this.getPosition() + " Ocupada pelo " + this.getPlayer().getIDPlayer() + "\n");
			isFull.await();
		}
		player.updatePosition(this.getPosition());
		this.player = player;
		isEmpty.signal();
		lock.unlock();
		game.notifyChange();
	}

	public synchronized void movePlayer(Player player) throws InterruptedException {

		if (this.isOcupied()) {
			switch (fightCastle(this.player, player)) {
				case 1 -> conquerCastle(this.player, player);
				case 2 -> conquerCastle(player, this.player);
			}
		}else {
			game.getCell(player.getCurrentCell().getPosition()).removePlayer();
			player.updatePosition(this.getPosition());
			this.player = player;
		}
		game.notifyChange();
	}
	
	public void removePlayer(){
		player = null;
	}

	private int fightCastle(Player player1, Player player2){
		// Player 1 dono do castelo, Player 2 mouro
		if (player1.getCurrentStrength() > player2.getCurrentStrength()){
			return 1;
		} else if (player1.getCurrentStrength() < player2.getCurrentStrength()) {
			return 2;
		} else {
			return new Random().nextInt(2) + 1;
		}
	}

	private void conquerCastle(Player winnerPlayer, Player defeatPlayer){
		winnerPlayer.updateStrenght(defeatPlayer.getCurrentStrength());
		defeatPlayer.updateStrenght((byte) -defeatPlayer.getCurrentStrength());
		defeatPlayer.interrupt();
	}

}
