import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerUser {
    private String username;
    private List<String> groupsId;
    private Socket socket;

    public ServerUser(String username, Socket socket) {
        this.username = username;
        this.groupsId = new ArrayList<>();
        this.socket = socket;
    }

    public List<String> getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(List<String> groupsId) {
        this.groupsId = groupsId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
