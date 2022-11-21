package game;

import environment.Coordinate;

import java.util.concurrent.CountDownLatch;

public class HumanPlayer extends Player {

    public HumanPlayer(int id, Game game, byte strength, CountDownLatch cdl) {
        super(id, game, strength, cdl);
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean isHumanPlayer() {
        return true;
    }

    @Override
    public void move(Coordinate direction) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
    
}
