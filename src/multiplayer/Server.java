package multiplayer;

import game.Game;
import game.HumanPlayer;
import game.Player;
import game.PlayerMinimal;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


public class Server extends Thread implements Serializable {
    public class DealWithClient extends Thread {
        Game game;
        Player player;
        private boolean mapSent = false;
        Socket socket;

        public DealWithClient(Socket socket, Game game) throws IOException {
            this.game = game;
            this.socket = socket;
            player = new HumanPlayer(1, game, (byte) 3);
            player.start();
            game.listPlayers.add(player);
            // doConnections(socket);
        }
        private ObjectInputStream in;
        @Override
        public void run() {
            try {
                serve();
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                // TODO Tratar da exceção quando a ligação é terminada.. talvez meter o player morto?
                System.out.println("Ligação terminada");
            }
        }
        private ObjectOutputStream out;

        private void serve() throws IOException, ClassNotFoundException, InterruptedException {

            while (true) {
                sendPlayers(socket);
                sleep(Game.REFRESH_INTERVAL);
            }
        }

        void put(String str) throws IOException {
            // TODO remver string str foi só para testar
            OutputStream oStream = socket.getOutputStream();
            ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
            ooStream.writeObject(str);
        }

        String get(){
            // TODO no futuro trocar por void isto foi so para teste
            try {
                InputStream iStream = socket.getInputStream();
                ObjectInputStream oiStream = new ObjectInputStream(iStream);
                return (String) oiStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void sendPlayers(Socket socket) throws IOException {
            try {
                OutputStream oStream = socket.getOutputStream();
                ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
                LinkedList<PlayerMinimal> minimals = new LinkedList<>();
                for (Player player: game.listPlayers){
                    minimals.add(new PlayerMinimal(player));
                }
                ooStream.writeObject(minimals);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    public static final int PORTO = 8080;
    public Game game;
    public Server(Game game){
        this.game = game;
    }
    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(PORTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            while(true){
                Socket socket = ss.accept();
                new DealWithClient(socket, game).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
