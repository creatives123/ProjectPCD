/**
 * Created by Alexandre Torres on IntelliJ IDEA.
 * Date: 24/11/2022
 * Time: 20:08
 * <p>
 * Details of the file:
 */
package gui.cliente;
import gui.BoardClient;
import javax.swing.*;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

public class MainClient implements Observer {
    private final JFrame frame = new JFrame("Cliente");
    private BoardClient boardGui;
    private GameClient game = new GameClient();
    private ClienteCon thread;
    public MainClient(String IP, int porto, boolean alternativeKeys) throws IOException, ClassNotFoundException, InterruptedException {
        try {
            buildGui(alternativeKeys);
            game.addObserver(this);

            thread = new ClienteCon(IP, porto, game);
            thread.start();
            //connectToServer();

            playGame();

            thread.closeConnection();
            thread.interrupt();
        }catch (IOException | ClassNotFoundException | InterruptedException e){
            // TODO Verficar o que devemos fazr aqui
            JOptionPane.showMessageDialog(frame, "Ligação ao servidor foi desconectada!",
                    "Server error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void buildGui(boolean alternativeKeys) {
        boardGui = new BoardClient(game, alternativeKeys);
        frame.add(boardGui);

        frame.setSize(800,800);
        frame.setLocation(0, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        boardGui.repaint();
    }

    void playGame() throws IOException, ClassNotFoundException, InterruptedException {
        while (true){
            thread.getPlayers();
            if(!thread.checkAlive()){
                JOptionPane.showMessageDialog(frame, "Jogo Terminado",
                        "Jogo Terminado!", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            // Envia a tecla que foi pressionada
            thread.sendDirection(boardGui.getLastPressedDirection());
            boardGui.clearLastPressedDirection();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        MainClient game = new MainClient("localhost", 8080 , false);

    }

}
