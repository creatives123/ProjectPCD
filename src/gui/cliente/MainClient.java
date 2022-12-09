/**
 * Created by Alexandre Torres on IntelliJ IDEA.
 * Date: 24/11/2022
 * Time: 20:08
 * <p>
 * Details of the file:
 */
package gui.cliente;
import game.PlayerMinimal;
import gui.BoardClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class MainClient implements Observer {
    private final JFrame frame = new JFrame("Cliente");
    private BoardClient boardGui;
    private GameClient game = new GameClient();
    private ClienteCon thread;
    private Socket socket;
    private String IP;
    private int porto;
    public MainClient(String IP, int porto, boolean multipleplayer) throws IOException {
        try {
            this.IP = IP;
            this.porto = porto;
            connectToServer();
            buildGui();
            game.addObserver(this);

            sendInformation(String.valueOf(multipleplayer));
            playGame();

            closeConnection();
        }catch (IOException | ClassNotFoundException | InterruptedException e){
            // TODO Verficar o que devemos fazr aqui
            JOptionPane.showMessageDialog(frame, "Ligação ao servidor foi desconectada!",
                    "Server error!", JOptionPane.ERROR_MESSAGE);

        }
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
            getPlayers();
            if(!checkAlive()){
                JOptionPane.showMessageDialog(frame, "Jogo Terminado",
                        "Jogo Terminado!", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            // Envia a tecla que foi pressionada
            String direction = boardGui.getLastPressedDirection();
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
        LinkedList<PlayerMinimal> listPlayers = (LinkedList<PlayerMinimal>) oiStream.readObject();
        game.updateBoard(listPlayers);
    }

    void sendInformation(String direction) throws IOException {
        OutputStream oStream = socket.getOutputStream();
        PrintWriter ooStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(oStream)), true);
        // Envia como texto a direção
        ooStream.println(direction);
    }

    public boolean checkAlive() throws IOException {
        // Verifica se na socket existe algo para ler
        InputStream iStream = socket.getInputStream();
        BufferedReader oiStream = new BufferedReader(new InputStreamReader(iStream));
        // le o que está na socket
        String value = oiStream.readLine();
        if (Objects.equals(value, "end")) {
            return false;
        }
        return true;
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public static void main(String[] args) throws IOException{

        JFrame frame = new JFrame("Welcome to IGE Agario PCD");
        // para que o botao de fechar a janela termine a aplicacao
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500,500);

        frame.setLayout(new GridLayout(4, 2));
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);

        frame.add(new JLabel("Address"));
        JTextField address = new JTextField();
        frame.add(address);

        frame.add(new JLabel("Port"));
        JTextField width = new JTextField();
        frame.add(width);

        frame.add(new JLabel("Multiplayer"));
        // array of string containing cities
        Boolean s1[] = { false, true};
        // create checkbox
        JComboBox multiplayer = new JComboBox(s1);
        frame.add(multiplayer);
        JCheckBox check = new JCheckBox("center");

        JButton button = new JButton("update");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (address.getText().equals("") || width.getText().equals("")) {
                    JOptionPane.showMessageDialog(frame, "Campos por preenhcer");
                }else{
                        frame.setVisible(false);
                    try {
                        System.out.println(multiplayer.getSelectedItem());
                        MainClient game = new MainClient(address.getText(), Integer.parseInt(width.getText()) , (Boolean) multiplayer.getSelectedItem());
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        width.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // if it's not a number, ignore the event
                }
            }
        });
        frame.getRootPane().setDefaultButton(button);
        frame.add(button);

        frame.pack();
        frame.setResizable ( false );

        frame.setVisible(true);





    }

}
