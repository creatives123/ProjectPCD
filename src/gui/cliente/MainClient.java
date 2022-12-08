/**
 * Created by Alexandre Torres on IntelliJ IDEA.
 * Date: 24/11/2022
 * Time: 20:08
 * <p>
 * Details of the file:
 */
package gui.cliente;

import environment.Direction;
import game.PlayerMinimal;
import multiplayer.Server;
import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class MainClient implements Observer {
    private final JFrame frame = new JFrame("asdsad");
    private BoardClient boardGui;
    private GameClient game = new GameClient();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private String IP;
    private int porto;
    public MainClient(String IP, int porto) throws IOException, ClassNotFoundException, InterruptedException {
        this.IP = IP;
        this.porto = porto;
        try {
            buildGui();
            game.addObserver(this);

            connectToServer();

            playGame();
        }catch (IOException | ClassNotFoundException | InterruptedException ignore){
            // TODO Verficar o que devemos fazr aqui
            System.out.println("Jogo Terminado");
        }

    }

    void connectToServer() throws IOException, ClassNotFoundException, InterruptedException {
        InetAddress address = InetAddress.getByName(IP);
        socket = new Socket(address, Server.PORTO);
    }

    void getPlayers() throws IOException, ClassNotFoundException, InterruptedException {
        InputStream iStream = socket.getInputStream();
        ObjectInputStream oiStream = new ObjectInputStream(iStream);
        LinkedList<PlayerMinimal> listPlayers = (LinkedList<PlayerMinimal>) oiStream.readObject();
        game.updateBoard(listPlayers);
    }

    void put(Direction direction) throws IOException {
        OutputStream oStream = socket.getOutputStream();
        PrintWriter ooStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(oStream)), true);
        ooStream.println(direction);
    }

    private void buildGui() {
        boardGui = new BoardClient(game);
        frame.add(boardGui);

        frame.setSize(800,800);
        frame.setLocation(0, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {

        boardGui.repaint();
    }

    void playGame() throws IOException, ClassNotFoundException, InterruptedException {
        while (true){

            getPlayers();

            // Envia a tecla que foi pressionada
            put(boardGui.getLastPressedDirection());
            boardGui.clearLastPressedDirection();

        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        MainClient game = new MainClient("localhost", 8080);

    }

}
