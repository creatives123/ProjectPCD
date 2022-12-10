package game;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectClient extends Thread {
    public ConnectClient(Socket socket) throws IOException {
        doConnections(socket);
    }
    private BufferedReader in;
    @Override
    public void run() {
        try {
            serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private PrintWriter out;

    void doConnections(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),
                true);
    }
    private void serve() throws IOException {
        while (true) {
            String str = in.readLine();
            if (str.equals("FIM"))
                break;
            System.out.println("Eco:" + str);
            out.println(str);
        }
    }
}
