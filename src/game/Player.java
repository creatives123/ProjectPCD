package game;



import environment.Cell;
import environment.Coordinate;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread  {
	protected  Game game;
	private int id;
	private byte currentStrength;
	protected byte originalStrength;

	private Coordinate posicao;
	// TODO: get player position from data in game
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

	public abstract void move();
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
