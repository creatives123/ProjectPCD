package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

import java.util.concurrent.CountDownLatch;

public class HumanPlayer extends Player {

    public HumanPlayer(int id, Game game, byte strength) {
        super(id, game, strength);
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean isHumanPlayer() {
        return true;
    }

    @Override
    public void move(Coordinate direction) throws InterruptedException {
        Coordinate nextposition = this.getCurrentCell().getPosition().translate(direction);
		if(nextposition.x <= 29 && nextposition.x >= 0 && nextposition.y >= 0 && nextposition.y <= 29){
			super.move(nextposition);
		}
		return;
    }

    @Override
    public void run() {
            setinitialposition();
    }

}
