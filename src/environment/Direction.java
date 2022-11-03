package environment;
import java.awt.event.KeyEvent;
import java.util.Random;

public enum Direction {
	UP(0,-1),DOWN(0,1),LEFT(-1,0),RIGHT(1,0);
	private Coordinate vector;
	Direction(int x, int y) {
		vector=new Coordinate(x, y);
	}
	public Coordinate getVector() {
		return vector;
	}



//Código NOSSO	
	public static Direction directionFor(int keyCode) {
	switch(keyCode){
		case KeyEvent.VK_DOWN:
			return DOWN;
		case KeyEvent.VK_UP:
			return UP;
		case KeyEvent.VK_LEFT:
			return LEFT;
		case KeyEvent.VK_RIGHT:
			return RIGHT;
	}
	

	throw new IllegalArgumentException();
    }

	public static Direction random() {
        Random generator = new Random();
        return values()[generator.nextInt(values().length)];
    }


}
