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
        Player player;
        Socket socket;

        public DealWithClient(Socket socket, Game game) throws IOException {
            this.game = game;
            this.socket = socket;
            player = new HumanPlayer(1, game, (byte) 3);
            player.start();
            game.listPlayers.add(player);
        }
        private ObjectInputStream in;
        @Override
        public void run() {
            try {
                serve();
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                // TODO Tratar da exceção quando a ligação é terminada.. talvez meter o player morto?
                synchronized(this) {
                    player.killPlayer();
                    System.out.println(player.isActive());
                    System.out.println(player.isAlive());
                }
                System.out.println("Ligação terminada");
            }
        }

        private void serve() throws IOException, ClassNotFoundException, InterruptedException {
            // enquanto corre actualiza o cliente com os players e novas posições e recebe novos movimentos
            while (true) {
                // envia a lista actual dos jogadores
                sendPlayers(socket);

                // verifica se recebeu uma mudança de direção
                Coordinate direction = getDirection();
                // TODO remover pois era só para ver se recebia a nova direção
                if (direction != null) {
                    System.out.println(direction);
                    player.move(direction);
                }
                sleep(Game.REFRESH_INTERVAL);
            }
        }

        Coordinate getDirection() throws IOException{
            // Verifica se na socket existe algo para ler
            InputStream iStream = socket.getInputStream();
            BufferedReader oiStream = new BufferedReader(new InputStreamReader(iStream));
            // le o que está na socket
            String value = oiStream.readLine();
            // Verifica se o valor que recebe é diferente de null,
            // se for então retorna a coordenada que recebeu.
            if (!Objects.equals(value, "null"))
                return Direction.valueOf(value).getVector();
            return null;
        }

        private void sendPlayers(Socket socket) throws IOException {
            // Envia uma lista de PlayerMinimal que contem só os dados necessários para a leitura do cliente.
            OutputStream oStream = socket.getOutputStream();
            ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
            // cria nova lista de minimals
            LinkedList<PlayerMinimal> minimals = new LinkedList<>();
            for (Player player: game.listPlayers){
                minimals.add(new PlayerMinimal(player));
            }

            // envia a lista para o cliente
            ooStream.writeObject(minimals);
        }
    }
    public static final int PORTO = 8080;
    public Game game;
    public Server(Game game){
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
            while(true){
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
