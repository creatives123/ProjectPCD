package game;



import environment.Cell;
import environment.Coordinate;
import environment.Direction;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread implements Comparable<Player>,Serializable  {
	protected  Game game;
	private final int id;
	private byte currentStrength;
	protected byte originalStrength;


	private Coordinate posicao;
	public Cell getCurrentCell() {
		return game.getCell(posicao);
	}

	public void updatePosition(Coordinate at){
		posicao = at;
	}
	CountDownLatch cdl;

	public Player(int id, Game game, byte strength) {
		super();
		this.id = id;
		this.game=game;
		currentStrength=strength;
		originalStrength=strength;
	}

	public int getIDPlayer(){
		return id;
	}
	public abstract boolean isHumanPlayer();

	public void move(Coordinate direction) throws InterruptedException {
		Coordinate nextposition = this.getCurrentCell().getPosition().translate(direction);
		while(nextposition.x > 29 || nextposition.x < 0 || nextposition.y < 0 || nextposition.y > 29){
			nextposition = this.getCurrentCell().getPosition().translate(Direction.random().getVector());
		}
		Cell newCell = game.getCell(nextposition);

		newCell.movePlayer(this);
	
	}

	/*
	public void updateStrenght(byte value){
		this.currentStrength += value;
	}*/

	public void updateStrenght(byte value){
		int newStrenght = this.currentStrength + value;
		this.currentStrength = (byte) newStrenght;
		if(this.currentStrength >= (byte) 10){
			this.currentStrength = (byte) 10;
			this.interrupt();
			game.cdl.countDown();
		}else if(this.currentStrength <= (byte) 0){
			this.interrupt();
			this.getCurrentCell().isFull.signal();
		}
	}

	public boolean isActive(){
		// Retorna se o jogador ainda está ativo no jogo
		return getCurrentStrength() > 0 && getCurrentStrength() < 10;
	}

	@Override
	public abstract void run();

	public void setinitialposition(){
		try {
			Cell initialPos=game.getRandomCell();
			initialPos.setPlayer(this);
		} catch (InterruptedException e) {}

		if (this.isInterrupted()){
			System.out.println("Entrei aqui");
			setinitialposition();
		}
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", getCurrentCell()=" + getCurrentCell()
		+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}


	public int getIdentification() {
		return id;
	}

	@Override
	public int compareTo(Player o){
		return id-o.id;
	}
}
