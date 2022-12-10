package game;

//Criação de uma Thread que interrompe os jogadores que estão parados à mais de 2 segundos
public class ThreadWait extends Thread {
    Player player;

    ThreadWait (Player player){
        this.player = player;
    }
    @Override
    public void run() {
        try {
            sleep(2000);
            this.player.interrupt();
        } catch (InterruptedException ignored) {}
    }
}
