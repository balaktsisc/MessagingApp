import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.*;
import java.util.List;

/**
 * This class represents the implementation of interface Commands and
 * consists of the methods that the server points to the clients' requests
 * to run.
 * @author Christos Balaktsis
 * @email  mpalaktsc@csd.auth.gr
 * @aem    3865
 */
public class CommandExecution extends UnicastRemoteObject implements Commands {
    // A list of all created/existing accounts on the system.
    private List<Account> accountList;
    // A counter for token initiation of every new account creation.
    private int tokenCounter;

    protected CommandExecution() throws RemoteException {
        super();
        this.accountList = new ArrayList<>();
        tokenCounter = 1000;
    }

    private List<Account> getAccountList() {
        return this.accountList;
    }

    private void setAccountList(List<Account> list) {
        this.accountList = list;
    }

    @Override
    public String createAccount(String username) throws RemoteException {
        // Check if the requested username already exists.
        for(Account account : this.accountList) {
            if(account.getUsername().equals(username))
                return "Sorry, the user already exists";
        }

        /* Check if the requested username fulfills specific (literal) prerequisites
           and creates the new account.
        */
        if(isValidUsername(username)) {
            Account newUser = new Account(username, ++this.tokenCounter);
            this.accountList.add(newUser);
            return Integer.toString(tokenCounter);
        } else {
            return "Invalid Username";
        }
    }

    @Override
    public String showAccounts(int authToken) throws RemoteException {
        // Check if the requesting user exists.
        if (getUserByToken(authToken) != null) {
            StringBuilder result = new StringBuilder();
            int i = 0;

            // Building the account list as a string.
            for (Account account : this.accountList)
                result.append(++i).append(". ").append(account.getUsername()).append("\n");
            return result.toString();
        } else {
            return "Invalid authToken";
        }
    }

    @Override
    public String sendMessage(int authToken, String receiver, String messageBody) throws RemoteException {
        // Check if the requested user exists.
        Account s = getUserByToken(authToken);
        if (s != null) {
            // Check if the requested message-receiver exists.
            Account r = getUserByUsername(receiver);
            if(r != null) {
                // Add message to the receivers' inbox.
                Message message = new Message(s.getUsername(),r.getUsername(),messageBody,++r.messageCounter);
                r.getMessageBox().add(message);
                return "OK";
            } else {
                return "User does not exist";
            }
        } else {
            return "Invalid authToken";
        }
    }

    @Override
    public String showInbox(int authToken) throws RemoteException {
        // Check if the requested user exists.
        Account user = getUserByToken(authToken);
        if(user != null) {
            StringBuilder result = new StringBuilder();

            // Building the inbox message list as a string.
            for (Message message : user.getMessageBox())
                result.append(message.getId()).append(". ").append("from: ").append(message.getSender()).append((!message.getStatus() ? "*" : "")).append("\n");
            return result.toString();
        } else {
            return "Invalid authToken";
        }
    }

    @Override
    public String readMessage(int authToken, int messageID) throws RemoteException {
        // Check if the requested user exists.
        Account user = getUserByToken(authToken);
        if(user != null) {
            // Check if the requested message to read exists.
            Message message = getMessageById(user,messageID);
            if(message != null) {
                message.setStatus(true);
                return "(" + message.getSender() + ") " + message.getBody() + "\n";
            } else {
                return "Message ID does not exist";
            }
        } else {
            return "Invalid authToken";
        }
    }

    @Override
    public String deleteMessage(int authToken, int messageID) throws RemoteException {
        // Check if the requested user exists.
        Account user = getUserByToken(authToken);
        if(user != null) {
            // Check if the requested message to delete exists.
            Message message = getMessageById(user,messageID);
            if(message != null) {
                user.getMessageBox().remove(message);
                return "OK";
            } else {
                return "Message does not exist";
            }
        } else {
            return "Invalid authToken";
        }
    }

    /**
     * Check if a string (name) is fulfills the prerequisites for standing
     * as a username in the system.
     * @param name The requested username
     * @return true, if it can stand as a username.
     */
    private static boolean isValidUsername(String name) {
        String regex = "^[A-Za-z]\\w*";
        Pattern p = Pattern.compile(regex);
        if(name == null) { return false; }
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * @param authToken The authToken of an account
     * @return the account with this authToken or null
     */
    private Account getUserByToken(int authToken) {
        for(Account account : this.accountList)
            if(account.getAuthToken() == authToken)
                return account;
        return null;
    }

    /**
     * @param username The username of an account
     * @return the account with this username or null
     */
    private Account getUserByUsername(String username) {
        for(Account account : this.accountList)
            if(Objects.equals(account.getUsername(), username))
                return account;
        return null;
    }

    /**
     * @param a The account that its inbox contains a message
     * @param id The id of the message
     * @return the requested message or null
     */
    private Message getMessageById(Account a, int id) {
        if(a == null)
            return null;
        for(Message message : a.getMessageBox())
            if(message.getId() == id)
                return message;
        return null;
    }
}