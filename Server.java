import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server {
    public static void main(String[] args) {
        try {
            CommandExecution stub = new CommandExecution();
            Registry rmiRegistry = LocateRegistry.createRegistry(5000);
            rmiRegistry.rebind("commands", stub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}