package game;


import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import environment.Cell;
import environment.Coordinate;

public class Game extends Observable {

    public static final int DIMY = 30;
    public static final int DIMX = 30;
    private static final int NUM_PLAYERS = 90;
    private static final int NUM_FINISHED_PLAYERS_TO_END_GAME = 3;

    public static final long REFRESH_INTERVAL = 400;
    public static final double MAX_INITIAL_STRENGTH = 3;
    public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
    public static final long INITIAL_WAITING_TIME = 10000;
    //Tentativa de um Lock


    private final AtomicInteger totalWinners = new AtomicInteger(0);
    protected Cell[][] board;
    Random randomGenerator = new Random();
    private static final int MAXBOTS = 100;
    private LinkedList<Player> listBots = new LinkedList<>();

    public Game() {
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

        // Ao haver vencedore termina todos os players
        if (Winners()){
            terminateAll();
        }
    }

    public Cell getRandomCell() {
        return getCell(new Coordinate((int) (Math.random() * Game.DIMX), (int) (Math.random() * Game.DIMY)));
    }

    public void updateWinners() {
        // incrementa sempre que um jogador chega a 10
        totalWinners.incrementAndGet();
        System.out.println("Winner Count: " + totalWinners.get());
    }

    public boolean Winners() {
        // Saber se ja temos vencedores
        return totalWinners.get() == 3;
    }

    public void addBots(){
        try {
            // Thread.sleep(3000);
            for (int i = 1; i <= MAXBOTS; i++ ){
                // RANDOM entre 1 e 3 (podemos tirar daqui e por no player)
                int rand = randomGenerator.nextInt(3) + 1;
                Player player = new PhoneyHumanPlayer(i, this, (byte)(rand));
                listBots.add(player);
                player.start();
            }

            for(Player player: listBots){
                player.join();
            }

        } catch (InterruptedException ignore) {
        }
    }

    private void terminateAll(){
        // terminar todos os jogadores
        for (Player player: listBots){
            player.interrupt();
        }
    }
}
