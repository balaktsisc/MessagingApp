import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.*;
import java.util.List;

public class CommandExecution extends UnicastRemoteObject implements Commands {
    private List<Account> accountList;
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
        for(Account account : this.accountList) {
            if(account.getUsername().equals(username))
                return "Sorry, the user already exists";
        }
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
        if (getUserByToken(authToken) != null) {
            StringBuilder result = new StringBuilder();
            int i = 0;
            for (Account account : this.accountList)
                result.append(++i).append(". ").append(account.getUsername()).append("\n");
            return result.toString();
        } else {
            return "Invalid authToken";
        }
    }

    @Override
    public String sendMessage(int authToken, String receiver, String messageBody) throws RemoteException {
        Account s = getUserByToken(authToken);
        if (s != null) {
            Account r = getUserByUsername(receiver);
            if(r != null) {
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
        Account user = getUserByToken(authToken);
        if(user != null) {
            StringBuilder result = new StringBuilder();
            for (Message message : user.getMessageBox())
                result.append(message.getId()).append(". ").append("from: ").append(message.getSender()).append((!message.getStatus() ? "*" : "")).append("\n");
            return result.toString();
        } else {
            return "Invalid authToken";
        }
    }

    @Override
    public String readMessage(int authToken, int messageID) throws RemoteException {
        Account user = getUserByToken(authToken);
        if(user != null) {
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
        Account user = getUserByToken(authToken);
        if(user != null) {
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

    private static boolean isValidUsername(String name) {
        String regex = "^[A-Za-z]\\w*";
        Pattern p = Pattern.compile(regex);
        if(name == null) { return false; }
        Matcher m = p.matcher(name);
        return m.matches();
    }

    private Account getUserByToken(int authToken) {
        for(Account account : this.accountList)
            if(account.getAuthToken() == authToken)
                return account;
        return null;
    }

    private Account getUserByUsername(String username) {
        for(Account account : this.accountList)
            if(Objects.equals(account.getUsername(), username))
                return account;
        return null;
    }

    private Message getMessageById(Account a, int id) {
        if(a == null)
            return null;
        for(Message message : a.getMessageBox())
            if(message.getId() == id)
                return message;
        return null;
    }
}