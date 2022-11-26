package gui.server;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import game.Game;
import gui.BoardJComponent;
import multiplayer.Server;

import javax.swing.JFrame;

public class GameGuiMain implements Observer {
	private final JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;

	//RANDOM entre 1 e 3 (podemos tirar daqui e por no player)


	public GameGuiMain() throws IOException {
		super();
		game = new Game();
		game.addObserver(this);

		buildGui();

		Server server = new Server(game);
		server.start();
	}

	private void buildGui() {
		boardGui = new BoardJComponent(game);
		frame.add(boardGui);


		frame.setSize(800,800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init()  {
		frame.setVisible(true);
		game.addBots();

		System.out.println("\n #### 3 vencedores encontrados #### \n");

	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) throws IOException {
		GameGuiMain game = new GameGuiMain();
		game.init();
	}

}
