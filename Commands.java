import java.rmi.*;
import java.util.ArrayList;

public interface Commands extends Remote {
    String createAccount(String username) throws RemoteException;
    String showAccounts(int authToken) throws RemoteException;
    String sendMessage(int authToken, String receiver, String messageBody) throws RemoteException;
    String showInbox(int authToken) throws RemoteException;
    String readMessage(int authToken, int messageID) throws RemoteException;
    String deleteMessage(int authToken, int messageID) throws RemoteException;
}
