package game;

import environment.Coordinate;

public class HumanPlayer extends Player {

    public HumanPlayer(int id, Game game, byte strength) {
        super(id, game, strength);
    }

    @Override
    public boolean isHumanPlayer() {
        return true;
    }

    //Move o Player para a próxima posição
    @Override
    public void move(Coordinate direction) throws InterruptedException {
        Coordinate nextposition = this.getCurrentCell().getPosition().translate(direction);
		if(nextposition.x <= Game.DIMX -1 && nextposition.x >= 0 && nextposition.y >= 0 && nextposition.y <= Game.DIMY -1){
			super.move(nextposition);
		}
		return;
    }

    @Override
    public void run() {
            setinitialposition();
    }

}
