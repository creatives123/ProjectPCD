package game;

import environment.Cell;
import environment.Coordinate;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

public abstract class Player extends Thread implements Comparable<Player>,Serializable  {
	protected  Game game;
	private final int id;
	private byte currentStrength;
	protected byte originalStrength;
	public CountDownLatch cdl;


	private Coordinate posicao;
	public Cell getCurrentCell() {
		return game.getCell(posicao);
	}

	public Coordinate getPosicao(){
		return posicao;
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
		Cell newCell = game.getCell(direction);
		newCell.movePlayer(this);	
	}

	//Faz update da força de cada jogador e interrompe os jogadores mortos ou acima de 10 de vida
	public void updateStrenght(byte value){
		int newStrenght = this.currentStrength + value;
		this.currentStrength = (byte) newStrenght;
		if(this.currentStrength >= (byte) Game.MAXLIFE){
			this.currentStrength = (byte) Game.MAXLIFE;
			this.interrupt();
			game.cdl.countDown();
		}else if(this.currentStrength <= (byte) 0){
			this.interrupt();
			this.getCurrentCell().isFull.signal();
		}
	}

	//Interrompe o jogador
	public synchronized void killPlayer(){
		currentStrength = (byte) 0;
		this.interrupt();
		game.notifyChange();
	}

	public boolean isActive(){
		// Retorna se o jogador ainda está ativo no jogo
		return getCurrentStrength() > 0 && getCurrentStrength() < Game.MAXLIFE;
	}

	@Override
	public abstract void run();

	//Coloca o jogador numa posição aleatória
	public void setinitialposition(){
		try {
			Cell initialPos=game.getRandomCell();
			initialPos.setPlayer(this);
		} catch (InterruptedException e) {}

		//Valide se é interrompido por ter um jogador morto na célula onde se ia colocar
		//Volta a correr recursivamente a função
		if (this.isInterrupted()){
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
