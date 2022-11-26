/**
 * Created by Alexandre Torres on IntelliJ IDEA.
 * Date: 24/11/2022
 * Time: 20:08
 * <p>
 * Details of the file:
 */
package gui.cliente;

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
    public MainClient() throws IOException, ClassNotFoundException, InterruptedException {
        buildGui();
        game.addObserver(this);
        init();

        connectToServer();

        sendMessages();
        /* Cliente client = new Cliente(game);
        client.runClient(); */
    }

    void connectToServer() throws IOException, ClassNotFoundException, InterruptedException {
        InetAddress endereco = InetAddress.getByName(null);
        socket = new Socket(endereco, Server.PORTO);

        getPlayers();
    }

    void getPlayers() throws IOException, ClassNotFoundException, InterruptedException {
        InputStream iStream = socket.getInputStream();
        ObjectInputStream oiStream = new ObjectInputStream(iStream);
        LinkedList<PlayerMinimal> listaPlayers = (LinkedList<PlayerMinimal>) oiStream.readObject();
        game.updateBoard(listaPlayers);
    }

    void put(int i) throws IOException {
        OutputStream oStream = socket.getOutputStream();
        ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
        ooStream.writeObject(("Ola " + i));
    }

    void get(){
        try {
            InputStream iStream = socket.getInputStream();
            ObjectInputStream oiStream = new ObjectInputStream(iStream);
            String str = (String) oiStream.readObject();
            System.out.println(str);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        frame.setVisible(true);
    }
    private void buildGui() {
        boardGui = new BoardClient(game);
        frame.add(boardGui);

        frame.setSize(800,800);
        frame.setLocation(0, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void update(Observable o, Object arg) {
        boardGui.repaint();
    }

    void sendMessages() throws IOException, ClassNotFoundException, InterruptedException {
        while (true){
            getPlayers();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        MainClient game = new MainClient();
    }

}
