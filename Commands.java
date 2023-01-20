import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface represents a generic template for the shared methods
 * between Server and Client objects.
 * @author Christos Balaktsis
 * @email  mpalaktsc@csd.auth.gr
 * @aem    3865
 */
public interface Commands extends Remote {
    String createAccount(String username) throws RemoteException;
    String showAccounts(int authToken) throws RemoteException;
    String sendMessage(int authToken, String receiver, String messageBody) throws RemoteException;
    String showInbox(int authToken) throws RemoteException;
    String readMessage(int authToken, int messageID) throws RemoteException;
    String deleteMessage(int authToken, int messageID) throws RemoteException;
}
