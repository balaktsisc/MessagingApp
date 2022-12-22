public class Message {
    private boolean isRead;
    private String sender;
    private String receiver;
    private String body;
    private int id;

    public Message(String sender, String receiver, String body, int id) {
        this.isRead = false;
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
        this.id = id;
    }

    public void setStatus(boolean read) {
        this.isRead = read;
    }

    public boolean getStatus() {
        return this.isRead;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return this.sender;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
