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

    //Coloca os Players em jogo
    public void setPlayer(Player player) throws InterruptedException {
        lock.lock();
        try {
            //Verifica se a posição está ocupada
            while (this.isOcupied()) {
                //Valida se está ocupada por um jogador vivo ou morto
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
            player.updatePosition(this.getPosition());
            this.player = player;
        } finally {
            lock.unlock();
            game.notifyChange();
        }

    }

    //Trata do movimento do jogador, função sync
    public synchronized void movePlayer(Player player) throws InterruptedException {
       
        if(this.isOcupied() && !this.player.isActive() && !player.isHumanPlayer()) {
            // Em caso um bot tentar mover-se para uma celula com um jogador morto ou vencedor fica a espera
            // Esta espera vai ser interrompida com uma sub thread que fica a espera 2 segundos no player
            wait();
        }
        else if(this.isOcupied() && !this.player.isActive() && player.isHumanPlayer()){
            // Faz com que os humanos não fiquem bloqueados por jogadores mortos
            return;
        }

        Cell playerCurrentCell = player.getCurrentCell();
        //Lock Lógica
        //1 - Bloquear a Célula onde estou | 2 - Bloquear a Célula para onde vou
        playerCurrentCell.lock.lock();
        lock.lock();

        try{
            //Valida se o jogador se está a mover para um posição ocupada por um jogador
            if(this.isOcupied() && this.player.isActive()) {
                //Começa o confronto
                switch (fightCastle(this.player, player)) {
                    case 1 -> conquerCastle(this.player, player);
                    case 2 -> conquerCastle(player, this.player);
                }
                //Unlock Lógica
                //1 - Desbloquear a Célula onde estou | 2 - Desbloquear a Célula para onde eu ia
                playerCurrentCell.lock.unlock();
                lock.unlock();
                
            }else{
                //Move o jogador para a nova posição
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

    //Remove o jogador da célula
    public synchronized void removePlayer() {
        player = null;
        lock.lock();
        try {
            //Notifica o jogador à espera de entrar nesta célula que já saiu dela
            isFull.signal();
        } finally {
            lock.unlock();
        }
    }

    //Resolve os Confrontos entre jogadores
    private int fightCastle(Player player1, Player player2) {
        // Player 1 - O que está na Célula
        // Player 2 - O que se move para a Célula
        if (player1.getCurrentStrength() > player2.getCurrentStrength()) {
            return 1;
        } else if (player1.getCurrentStrength() < player2.getCurrentStrength()) {
            return 2;
        } else {
            return new Random().nextInt(2) + 1;
        }
    }

    //Após confronto, atualiza as vidas dos jogadores
    private synchronized void conquerCastle(Player winnerPlayer, Player defeatPlayer){
        winnerPlayer.updateStrenght(defeatPlayer.getCurrentStrength());
        defeatPlayer.updateStrenght((byte) -defeatPlayer.getCurrentStrength());
       
    }

}
