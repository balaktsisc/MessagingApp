import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MessagingServer {
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private DataInputStream in = null;

    private Set<HashSet> accountList;

    public MessagingServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            accountList = new HashSet<>();


            // take input from the client socket
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            // read msg from client till "Stop" is entered
            String line = "";
            while (!line.equals("Stop")) {
                try {
                    line = in.readUTF();
                    System.out.println(line);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]) {
        MessagingServer messagingServer = new MessagingServer(2000);
    }
}