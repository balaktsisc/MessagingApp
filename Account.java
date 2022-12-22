import java.util.ArrayList;
import java.util.List;

public class Account {
    private final String username;
    private final int authToken;
    private final List<Message> messageBox;
    public int messageCounter;

    public Account(String username, int authToken) {
        this.username = username;
        this.authToken = authToken;
        this.messageBox = new ArrayList<>();
        this.messageCounter = 0;
    }

    public String getUsername() {
        return username;
    }

    public int getAuthToken() {
        return authToken;
    }

    public List<Message> getMessageBox() {
        return messageBox;
    }
}
