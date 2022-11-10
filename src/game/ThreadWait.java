/**
 * Created by Alexandre Torres on IntelliJ IDEA.
 * Date: 10/11/2022
 * Time: 21:52
 * <p>
 * Details of the file:
 */
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
        } catch (InterruptedException ignored) {

        }
    }
}
