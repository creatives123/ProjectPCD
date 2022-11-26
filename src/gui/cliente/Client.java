/**
 * Created by Alexandre Torres on IntelliJ IDEA.
 * Date: 26/11/2022
 * Time: 14:26
 * <p>
 * Details of the file:
 */
package gui.cliente;

import game.PlayerMinimal;
import multiplayer.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

public class Client extends Thread {

    GameClient game;
    Socket socket;

    public Client(GameClient game){
        this.game = game;
    }

    @Override
    public void run(){
        try {
            connectToServer();
        } catch (IOException | ClassNotFoundException | InterruptedException ignore) {
        }

        while (true){
            try {
                getPlayers();
            } catch (IOException | ClassNotFoundException | InterruptedException ignore) {
            }
        }

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

}
