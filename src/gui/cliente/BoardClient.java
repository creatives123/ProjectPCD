package gui.cliente;
import environment.Coordinate;
import environment.Direction;
import game.PlayerMinimal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BoardClient extends JComponent implements KeyListener {
	private GameClient game;
	private Image obstacleImage = new ImageIcon("obstacle.png").getImage();
	private Image humanPlayerImage= new ImageIcon("abstract-user-flat.png").getImage();
	private Direction lastPressedDirection=null;
	
	public BoardClient(GameClient game) {
		this.game = game;
		setFocusable(true);
		addKeyListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		double cellHeight=getHeight()/(double) GameClient.DIMY;
		double cellWidth=getWidth()/(double) GameClient.DIMX;

		for (int y = 1; y < GameClient.DIMY; y++) {
			g.drawLine(0, (int)(y * cellHeight), getWidth(), (int)(y* cellHeight));
		}
		for (int x = 1; x < GameClient.DIMX; x++) {
			g.drawLine( (int)(x * cellWidth),0, (int)(x* cellWidth), getHeight());
		}
		for (int x = 0; x < GameClient.DIMX; x++)
			for (int y = 0; y < GameClient.DIMY; y++) {
				Coordinate p = new Coordinate(x, y);

				PlayerMinimal player = game.getCell(p).getPlayer();
				if(player!=null) {
					// Fill yellow if there is a dead player
					if(player.getCurrentStrength()==0) {
						g.setColor(Color.YELLOW);
						g.fillRect((int)(p.x* cellWidth), 
								(int)(p.y * cellHeight),
								(int)(cellWidth),(int)(cellHeight));
						g.drawImage(obstacleImage, (int)(p.x * cellWidth), (int)(p.y*cellHeight), 
								(int)(cellWidth),(int)(cellHeight), null);
						// if player is dead, don'd draw anything else?
						continue;
					}
					// Fill green if it is a human player
					if(player.isHumanPlayer()) {
						g.setColor(Color.GREEN);
						g.fillRect((int)(p.x* cellWidth), 
								(int)(p.y * cellHeight),
								(int)(cellWidth),(int)(cellHeight));
						// Custom icon?
						g.drawImage(humanPlayerImage, (int)(p.x * cellWidth), (int)(p.y*cellHeight), 
								(int)(cellWidth),(int)(cellHeight), null);
					}
					g.setColor(new Color(player.getIdentification() * 1000));
					((Graphics2D) g).setStroke(new BasicStroke(5));
					Font font = g.getFont().deriveFont( (float)cellHeight);
					g.setFont( font );
					String strengthMarking=(player.getCurrentStrength()==10?"X":""+player.getCurrentStrength());
					g.drawString(strengthMarking,
							(int) ((p.x + .2) * cellWidth),
							(int) ((p.y + .9) * cellHeight));
				}

			}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_LEFT :
			lastPressedDirection= Direction.LEFT;
			break;
		case KeyEvent.VK_RIGHT:
			lastPressedDirection= Direction.RIGHT;
			break;
		case KeyEvent.VK_UP:
			lastPressedDirection= Direction.UP;
			break;
		case KeyEvent.VK_DOWN:
			lastPressedDirection= Direction.DOWN;
			break;
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		//ignore
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Ignored...
	}

	public Direction getLastPressedDirection() {
		return lastPressedDirection;
	}

	public void clearLastPressedDirection() {
		lastPressedDirection=null;
	}
}
