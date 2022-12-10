
package gui.cliente;
import environment.PlayerMinimal;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class MainClient implements Observer {
    private final JFrame frame = new JFrame("Cliente");
    private BoardClient boardGui;
    private final GameClient game = new GameClient();
    private Socket socket;
    private String IP;
    private int porto;
    public MainClient(String IP, int porto, boolean multipleplayer) throws IOException {
        try {
            this.IP = IP;
            this.porto = porto;
            // liga-se ao servidor
            connectToServer();
            // constroi
            buildGui();
            game.addObserver(this);

            sendInformation(String.valueOf(multipleplayer));
            playGame();

        }catch (IOException | ClassNotFoundException | InterruptedException e){
            JOptionPane.showMessageDialog(frame, "Ligação ao servidor foi desconectada!",
                    "Server error!", JOptionPane.ERROR_MESSAGE);

        }
        closeConnection();
    }
    private void buildGui() {
        boardGui = new BoardClient(game);
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
            // Le a lista de jogadores vindos do servidor
            getPlayers();

            // Verifica se o jogo acabou
            if(!checkAlive()){
                JOptionPane.showMessageDialog(frame, "Jogo Terminado",
                        "Jogo Terminado!", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            // Verfica a tecla que foi pressionada
            String direction = boardGui.getLastPressedDirection();
            // Envia a tecla para o servidor
            sendInformation(direction);
            boardGui.clearLastPressedDirection();
        }
    }

    void connectToServer() throws IOException, ClassNotFoundException, InterruptedException {
        InetAddress address = InetAddress.getByName(IP);
        socket = new Socket(address, porto);
        System.out.println(socket);
    }

    void getPlayers() throws IOException, ClassNotFoundException, InterruptedException {
        InputStream iStream = socket.getInputStream();
        ObjectInputStream oiStream = new ObjectInputStream(iStream);
        // le a lista de players que recebe do servidor
        List<PlayerMinimal> listPlayers = (List<PlayerMinimal>) oiStream.readObject();
        game.updateBoard(listPlayers);
    }

    void sendInformation(String direction) throws IOException {
        OutputStream oStream = socket.getOutputStream();
        PrintWriter ooStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(oStream)), true);
        // Envia como texto para o servidor
        // neste caso ou é a direção, ou se o jogo é multiplayer
        ooStream.println(direction);
    }

    public boolean checkAlive() throws IOException {
        // Verifica se na socket existe algo para ler
        InputStream iStream = socket.getInputStream();
        BufferedReader oiStream = new BufferedReader(new InputStreamReader(iStream));
        // le o que está na socket
        String value = oiStream.readLine();
        return !Objects.equals(value, "end");
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        if(args.length == 3){
            MainClient game = new MainClient(args[0], Integer.parseInt(args[1]), Boolean.parseBoolean(args[2]));
        }else {
            MainClient game = new MainClient("localhost", 8080, true);
        }
    }

}
