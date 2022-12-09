package gui.cliente;

import environment.Direction;
import game.Game;
import game.PlayerMinimal;
import multiplayer.Server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Objects;

public class ClienteCon extends Thread {
    private Socket socket;
    private String IP;
    private int porto;
    private GameClient game;

    ClienteCon(String IP, int porto, GameClient game) throws IOException, ClassNotFoundException, InterruptedException {
        this.IP = IP;
        this.porto = porto;
        this.game = game;
        connectToServer();
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    void connectToServer() throws IOException, ClassNotFoundException, InterruptedException {
        InetAddress address = InetAddress.getByName(IP);
        socket = new Socket(address, Server.PORTO);
        System.out.println(socket);
    }

    void getPlayers() throws IOException, ClassNotFoundException, InterruptedException {
        InputStream iStream = socket.getInputStream();
        ObjectInputStream oiStream = new ObjectInputStream(iStream);
        // le a lista de players que recebe do servidor
        LinkedList<PlayerMinimal> listPlayers = (LinkedList<PlayerMinimal>) oiStream.readObject();
        game.updateBoard(listPlayers);
    }

    void sendDirection(String direction) throws IOException {
        OutputStream oStream = socket.getOutputStream();
        PrintWriter ooStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(oStream)), true);
        // Envia como texto a direção
        ooStream.println(direction);
    }

    public boolean checkAlive() throws IOException {
        // Verifica se na socket existe algo para ler
        InputStream iStream = socket.getInputStream();
        BufferedReader oiStream = new BufferedReader(new InputStreamReader(iStream));
        // le o que está na socket
        String value = oiStream.readLine();
        if (Objects.equals(value, "dead")) {
            return false;
        }
        return true;
    }

    public void closeConnection() throws IOException {
        socket.close();
    }
}
