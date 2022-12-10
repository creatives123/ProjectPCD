package game;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import environment.Coordinate;
import environment.PlayerMinimal;

public class Game extends Observable {

    public static final int DIMY = 30;
    public static final int DIMX = 30;
    public static final int MAXLIFE = 10;
    private static final int NUM_PLAYERS = 90;
    private static final int NUM_FINISHED_PLAYERS_TO_END_GAME = 3;
    public static final long REFRESH_INTERVAL = 400;
    public static final double MAX_INITIAL_STRENGTH = 3;
    public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
    public static final long INITIAL_WAITING_TIME = 10000;
    private Boolean winner = false;
    protected Cell[][] board;
    Random randomGenerator = new Random();
    private static final int MAXBOTS = 90;
    public LinkedList<Player> listPlayers = new LinkedList<>();
    public CountDownLatch cdl = new CountDownLatch(NUM_FINISHED_PLAYERS_TO_END_GAME);


    public Game() throws IOException {
        board = new Cell[Game.DIMX][Game.DIMY];

        for (int x = 0; x < Game.DIMX; x++)
            for (int y = 0; y < Game.DIMY; y++)
                board[x][y] = new Cell(new Coordinate(x, y), this);
    }

    public Cell getCell(Coordinate at) {
        return board[at.x][at.y];
    }



    /**
     * Updates GUI. Should be called anytime the game state changes
     */
    public void notifyChange() {
        setChanged();
        notifyObservers();

    }

    public Cell getRandomCell() {
        return getCell(new Coordinate((int) (Math.random() * Game.DIMX), (int) (Math.random() * Game.DIMY)));
    }

    public boolean Winners() {
        // Saber se ja temos vencedores
        return winner;
    }

    public void addBots(){
        try {
            Thread.sleep(10000);
            //Cria os jogadores BOTS e iniciaos
            System.out.println("Vai ser criado o jogo com " + MAXBOTS + " BOTS");
            for (int i = 1; i <= MAXBOTS; i++ ){
                int rand = randomGenerator.nextInt((int)MAX_INITIAL_STRENGTH) + 1;
                Player player = new PhoneyHumanPlayer(i, this, (byte)(rand));
                listPlayers.add(player);
                player.start();
            }

            cdl.await();
            endgame();
        } catch (InterruptedException ignored) {}
    }

    public void endgame(){
        winner = true;
        for(Player player: listPlayers){
            player.interrupt();
        }
    }

    //Cria uma lista de jogsadores com apenas a informação necessária a enviar para o Cliente
    public List<PlayerMinimal> getPlayers(){
        List<PlayerMinimal> listPlayers = new ArrayList<PlayerMinimal>();
        for (int x = 0; x < Game.DIMX; x++)
            for (int y = 0; y < Game.DIMY; y++)
            {
                Cell cell = board[x][y];
                if(cell.isOcupied()) {
                    Player player = cell.getPlayer();
                    listPlayers.add(new PlayerMinimal(player.getIDPlayer(), player.getCurrentStrength(),
                            x , y, player.isHumanPlayer()));
                }
            }
        return listPlayers;
    }
}
