package multiplayer;

import environment.Coordinate;
import environment.Direction;
import game.Game;
import game.HumanPlayer;
import game.Player;
import game.PlayerMinimal;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class Server extends Thread implements Serializable {
    public static class DealWithClient extends Thread {
        /*  Class criada para lidar com cada cliente
            Vai criar um player sempre que é criada a ligação
            Inicia o player e adiciona a lista do game de players
        */

        Game game;
        Player player1;
        Player player2;
        Socket socket;
        boolean multiplayer;

        public DealWithClient(Socket socket, Game game) throws IOException {
            this.game = game;
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                multiplayer = Boolean.parseBoolean(getMultiplayer());
                player1 = new HumanPlayer(1, game, (byte) 3);
                player1.start();
                game.listPlayers.add(player1);
                if (multiplayer) {
                    player2 = new HumanPlayer(1, game, (byte) 3);
                    player2.start();
                    game.listPlayers.add(player2);
                }

                serve();

                closeConnection();
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                // TODO Tratar da exceção quando a ligação é terminada.. talvez meter o player morto?
                player1.killPlayer();
                if (multiplayer)
                    player2.killPlayer();
                System.out.println("Ligação terminada");
            }
        }

        private void serve() throws IOException, ClassNotFoundException, InterruptedException {
            // enquanto corre actualiza o cliente com os players e novas posições e recebe novos movimentos
            while (checkStatus()) {
                // envia a lista actual dos jogadores
                sendPlayers(socket);

                sendPlayerStatus();
                // verifica se recebeu uma mudança de direção
                getDirection();

                sleep(200);
            }
        }

        public void closeConnection() throws IOException {
            socket.close();
        }

        void getDirection() throws IOException, InterruptedException {
            // Verifica se na socket existe algo para ler
            InputStream iStream = socket.getInputStream();
            BufferedReader oiStream = new BufferedReader(new InputStreamReader(iStream));
            // le o que está na socket
            String value = oiStream.readLine();

            if (!value.equals("null")) {
                String[] arrOfStr = value.split("\\|");
                if (Objects.equals(arrOfStr[0], "P1") && player1.isActive()) {
                    System.out.println("Mover Player1");
                    player1.move(Direction.valueOf(arrOfStr[1]).getVector());
                } else if (Objects.equals(arrOfStr[0], "P2") && multiplayer && player1.isActive()) {
                    System.out.println("Mover Player2");
                    player2.move(Direction.valueOf(arrOfStr[1]).getVector());
                }
            }
        }

        private boolean checkStatus() {
            if (multiplayer) {
                return !player1.isActive() || !player2.isActive();
            } else {
                return !player1.isActive();

            }
        }


        String getMultiplayer() throws IOException {
            // Verifica se na socket existe algo para ler
            InputStream iStream = socket.getInputStream();
            BufferedReader oiStream = new BufferedReader(new InputStreamReader(iStream));
            // le o que está na socket
            return oiStream.readLine();
        }

        public void sendPlayerStatus() throws IOException {
            OutputStream oStream = socket.getOutputStream();
            PrintWriter ooStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(oStream)), true);
            if (!player1.isActive() && !player2.isActive()) {
                ooStream.println("end");
                this.interrupt();
            } else {
                ooStream.println("alive");
            }

        }

        private void sendPlayers(Socket socket) throws IOException {
            // Envia uma lista de PlayerMinimal que contem só os dados necessários para a leitura do cliente.
            OutputStream oStream = socket.getOutputStream();
            ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
            // cria nova lista de minimals
            LinkedList<PlayerMinimal> minimals = new LinkedList<>();
            for (Player player : game.listPlayers) {
                minimals.add(new PlayerMinimal(player));
            }
            // envia a lista para o cliente
            ooStream.writeObject(minimals);
        }
    }

    public static final int PORTO = 8080;
    public Game game;

    public Server(Game game) {
        this.game = game;
    }

    public void run() {
        // cria um ServerSocket em escuta por novas ligações
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(PORTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            while (true) {
                // sempre que recebe um pedido cria um dealwith cliente para permitir multiplos users
                Socket socket = ss.accept();
                new DealWithClient(socket, game).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // fecha a ServerSocket
            try {
                ss.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
