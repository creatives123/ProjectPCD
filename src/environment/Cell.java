package environment;

import game.Game;
import game.Player;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.midi.Track;

public class Cell {
    private Coordinate position;
    private Game game;
    private Player player = null;

    private Lock lock = new ReentrantLock();
    public Condition isEmpty = lock.newCondition();
    private Condition isFull = lock.newCondition();

    public Lock lockMove = new ReentrantLock();
    public boolean action = false;

    public Cell(Coordinate position, Game g) {
        super();
        this.position = position;
        this.game = g;


    }

    public Coordinate getPosition() {
        return position;
    }

    public boolean isOcupied() {
        return player != null;
    }

    public Player getPlayer() {
        return player;
    }

    //TODO Should not be used like this in the initial state: cell might be occupied, must coordinate this operation
    public void setPlayer(Player player) throws InterruptedException {
        lock.lock();
        try {
            while (this.isOcupied()) {
                System.out.print("Sou o " + player.getIDPlayer() + " e tenho a Celula " + this.getPosition() + " Ocupada pelo " + this.getPlayer().getIDPlayer() + "\n");
                isFull.await();
            }
            System.out.println("Player " + player.getIDPlayer() + " fui posto na celula");
            player.updatePosition(this.getPosition());
            this.player = player;
        } finally {
            lock.unlock();
            game.notifyChange();
        }

    }

    public synchronized void movePlayer(Player player) throws InterruptedException {
        Cell playerCurrentCell = player.getCurrentCell();

        lock.lock();
        playerCurrentCell.lock.lock();

        if (this.isOcupied() && this.player.isActive()) {
            switch (fightCastle(this.player, player)) {
                case 1 -> conquerCastle(this.player, player);
                case 2 -> conquerCastle(player, this.player);
            }
        } else if (this.isOcupied() && !this.player.isActive() && !this.player.isHumanPlayer()) {
            // Em caso um bot tentar mover-se para uma celula com um jogador morto ou vencedor fica a espera
            // Esta espera vai ser interrompida com uma sub thread que fica a espera 2 segundos no player
            wait();
        } else {
            game.getCell(player.getCurrentCell().getPosition()).removePlayer();
            player.updatePosition(this.getPosition());
            this.player = player;

        }
        lock.unlock();
        playerCurrentCell.lock.unlock();


        game.notifyChange();
    }

    public void removePlayer() {
        player = null;
        lock.lock();
        try {
            isFull.signal();
        } finally {
            lock.unlock();
        }
    }

    private int fightCastle(Player player1, Player player2) {
        // Player 1 dono do castelo, Player 2 mouro
        if (player1.getCurrentStrength() > player2.getCurrentStrength()) {
            return 1;
        } else if (player1.getCurrentStrength() < player2.getCurrentStrength()) {
            return 2;
        } else {
            return new Random().nextInt(2) + 1;
        }
    }

    private void conquerCastle(Player winnerPlayer, Player defeatPlayer) {
        winnerPlayer.updateStrenght(defeatPlayer.getCurrentStrength());
        defeatPlayer.updateStrenght((byte) -defeatPlayer.getCurrentStrength());
        defeatPlayer.interrupt();
    }

}
