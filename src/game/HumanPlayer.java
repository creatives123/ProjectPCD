package game;

import environment.Coordinate;

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
    public void move(Coordinate direction) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
    
}
