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
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class MainClient implements Observer {
    private final JFrame frame = new JFrame("Cliente");
    private BoardClient boardGui;
    private GameClient game = new GameClient();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private String IP;
    private int porto;
    private int playerID;
    private PlayerMinimal player;
    public MainClient(String IP, int porto) throws IOException, ClassNotFoundException, InterruptedException {
        this.IP = IP;
        this.porto = porto;
        try {
            buildGui();
            game.addObserver(this);

            connectToServer();

            playGame();

            closeConnection();
        }catch (IOException | ClassNotFoundException | InterruptedException e){
            // TODO Verficar o que devemos fazr aqui
            JOptionPane.showMessageDialog(frame, "Ligação ao servidor foi desconectada!",
                    "Server error!", JOptionPane.ERROR_MESSAGE);
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

    void sendDirection(Direction direction) throws IOException {
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

    private void closeConnection() throws IOException {
        socket.close();
    }

    void playGame() throws IOException, ClassNotFoundException, InterruptedException {
        while (true){

            getPlayers();

            if(!checkAlive()){
                JOptionPane.showMessageDialog(frame, "Jogo Terminado",
                        "Jogo Terminado!", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            // Envia a tecla que foi pressionada
            sendDirection(boardGui.getLastPressedDirection());
            boardGui.clearLastPressedDirection();



        }
    }

    public boolean checkAlive() throws IOException {
        // Verifica se na socket existe algo para ler
        InputStream iStream = socket.getInputStream();
        BufferedReader oiStream = new BufferedReader(new InputStreamReader(iStream));
        // le o que está na socket
        String value = oiStream.readLine();
        if (Objects.equals(value, "dead")){
            return false;
        }
        return true;


    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        MainClient game = new MainClient("localhost", 8080);


    }

}
