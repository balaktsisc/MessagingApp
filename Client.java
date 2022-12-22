import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;


public class Client {
    public static void main(String[] args) {
        try {
            String ip = args[0];
            int port = Integer.parseInt(args[1]);
            int fn = Integer.parseInt(args[2]);

            Registry rmiRegistry = LocateRegistry.getRegistry(port);
            Commands stub = (Commands) rmiRegistry.lookup("commands");

            if(fn == 1) {
                System.out.println(stub.createAccount(args[3]));
            } else if(fn == 2) {
                System.out.println(stub.showAccounts(Integer.parseInt(args[3])));
            } else if(fn == 3) {
                System.out.println(stub.sendMessage(Integer.parseInt(args[3]),args[4],args[5]));
            } else if(fn == 4) {
                System.out.println(stub.showInbox(Integer.parseInt(args[3])));
            } else if(fn == 5) {
                System.out.println(stub.readMessage(Integer.parseInt(args[3]),Integer.parseInt(args[4])));
            } else if(fn == 6) {
                System.out.println(stub.deleteMessage(Integer.parseInt(args[3]),Integer.parseInt(args[4])));
            } else {
                System.out.println("Unknown command");
            }
        } catch (Exception e) {
            System.out.println("----Mind your argument list!----");
            e.printStackTrace();
        }
    }
}