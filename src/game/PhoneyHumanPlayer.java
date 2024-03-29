package game;

import environment.Coordinate;
import environment.Direction;

public class PhoneyHumanPlayer extends Player {
	//Variável para guardar a força com qual o player começa.
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
		setinitialposition();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {e1.printStackTrace();}

		// O bot só executa enquanto o currentStrenght estiver entre ]0; 10[ e o jogo não tiver vencedores
		while (super.getCurrentStrength() != 0 && super.getCurrentStrength() != Game.MAXLIFE && !game.Winners()){

			// move do bot
			// Criamos uma thread de wait para prevenir que o bot fique parado eternamente.
			ThreadWait threadwait = new ThreadWait(this);
			try{

				threadwait.start();

				move(generatedirection());
				// move foi com sucesso então terminamos
				threadwait.interrupt();

				//Multiplicamos o REFRESH_INTERVAL pelo initialstrenght para diferenciar os ciclos de movimento
				Thread.sleep(Game.REFRESH_INTERVAL * initialstrenght);
			} catch (InterruptedException e) {
				threadwait.interrupt();
			}

		}
	}

	// Gera a próxima posição random do BOT
	public Coordinate generatedirection(){
		Coordinate nextposition = this.getCurrentCell().getPosition().translate(Direction.random().getVector());
		while(nextposition.x > Game.DIMX -1 || nextposition.x < 0 || nextposition.y < 0 || nextposition.y > Game.DIMY -1){
			nextposition = this.getCurrentCell().getPosition().translate(Direction.random().getVector());
		}
		return nextposition;
	}

	@Override
	public void move(Coordinate direction) throws InterruptedException {
		super.move(direction);
	}
}
