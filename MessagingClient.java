import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagingClient {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private List<String> funcArgs = null;

    public MessagingClient(String address, int port, int funcID, String args) {
        String[] argsArr = args.split(" ");
        funcArgs = new ArrayList<>(Arrays.asList(argsArr));
        establishConnection(address, port);


        dropConnection();
    }


    private void establishConnection(String address, int port) {
        try {
            this.socket = new Socket(address, port);
            this.input = new DataInputStream(System.in);
            this.output = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void dropConnection() {
        try {
            this.input.close();
            this.output.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MessagingClient messagingClient = new MessagingClient(args[0], Integer.getInteger(args[1]), Integer.getInteger(args[2]),args[3]);
    }
}