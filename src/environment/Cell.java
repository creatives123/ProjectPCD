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
    public Condition isFull = lock.newCondition();

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

    public void setPlayer(Player player) throws InterruptedException {
        lock.lock();
        try {
            while (this.isOcupied()) {
                if(this.getPlayer().isActive()){
                    System.out.print("Sou o " + player.getIDPlayer() + " e tenho a Celula " + this.getPosition() + " Ocupada pelo " + this.getPlayer().getIDPlayer() + "\n");
                    isFull.await();
                }
                else{
                    System.out.print("Sou o " + player.getIDPlayer() + " e vou ser realocado \n" );
                    player.interrupt();
                    throw new InterruptedException();
                }
                
            }
            //System.out.println("Player " + player.getIDPlayer() + " fui posto na celula" + this.getPosition());
            player.updatePosition(this.getPosition());
            this.player = player;
        } finally {
            lock.unlock();
            game.notifyChange();
        }

    }

    public synchronized void movePlayer(Player player) throws InterruptedException {
       
        if(this.isOcupied() && !this.player.isActive() && !player.isHumanPlayer()) {
            // Em caso um bot tentar mover-se para uma celula com um jogador morto ou vencedor fica a espera
            // Esta espera vai ser interrompida com uma sub thread que fica a espera 2 segundos no player
            wait();
        }
        else if(this.isOcupied() && !this.player.isActive() && player.isHumanPlayer()){
            return;
        }

        Cell playerCurrentCell = player.getCurrentCell();
        //Lock Lógica
        //1 - Bloquear a Célula onde estou | 2 - Bloquear a Célula para onde vou
        playerCurrentCell.lock.lock();
        lock.lock();

        try{
            if(this.isOcupied() && this.player.isActive()) {
                switch (fightCastle(this.player, player)) {
                    case 1 -> conquerCastle(this.player, player);
                    case 2 -> conquerCastle(player, this.player);
                }
                //Unlock Lógica
                //1 - Desbloquear a Célula onde estou | 2 - Desbloquear a Célula para onde eu ia
                playerCurrentCell.lock.unlock();
                lock.unlock();
                
            }else{
                game.getCell(player.getCurrentCell().getPosition()).removePlayer();
                player.updatePosition(this.getPosition());
                this.player = player;
                //Unlock Lógica
                //1 - Desbloquear a Célula onde estou | 2 - Desbloquear a Célula de onde eu vim
                lock.unlock();
                playerCurrentCell.lock.unlock();
            }

        }finally{
        game.notifyChange();
        }

        
    }

    public synchronized void removePlayer() {
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

    private synchronized void conquerCastle(Player winnerPlayer, Player defeatPlayer){
        winnerPlayer.updateStrenght(defeatPlayer.getCurrentStrength());
        defeatPlayer.updateStrenght((byte) -defeatPlayer.getCurrentStrength());
       
    }

}
