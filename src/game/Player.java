package game;



import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread  {
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
		if (newStrenght >= 10){
			this.currentStrength = (byte) 10;
			this.interrupt();
			game.updateWinners();
		}else {
			this.currentStrength = (byte) newStrenght;
		}
	}

	public boolean isActive(){
		// Retorna se o jogador ainda está ativo no jogo
		return getCurrentStrength() > 0 && getCurrentStrength() < 10;
	}

	@Override
	public abstract void run();

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
}
