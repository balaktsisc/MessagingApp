import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This class represents a Server that replies to the client requests.
 * It is implemented based on the RMI technology for Server-Client communication
 * and defines (implements) the common/shared methods that both the two parts
 * know as signatures, by the interface Commands.
 * @author Christos Balaktsis
 * @email  mpalaktsc@csd.auth.gr
 * @aem    3865
 */
public class Server {
    public static void main(String[] args) {
        try {
            CommandExecution stub = new CommandExecution();
            Registry rmiRegistry = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
            rmiRegistry.rebind("commands", stub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}