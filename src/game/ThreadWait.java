package game;

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
