import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Server {
    private static List<ServerUser> serverUserList = new ArrayList<>();


    public static void main(String[] args) {
        ServerSocket server = null;

        try {
            server = new ServerSocket(8080);
            server.setReuseAddress(true);

            while (true) {
                Socket client = server.accept();
                ClientHandler clientSock = new ClientHandler(client);

                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;
            try {

                out = new PrintWriter(
                        clientSocket.getOutputStream(), true);

                in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {

                    if (line.startsWith("username")) {
                        serverUserList.add(new ServerUser(line.split(" ")[1], clientSocket));
                        System.out.println("new user connected with username: " + line.split(" ")[1]);

                    } else if (line.split(" ")[1].equals("join")) {
                        for (ServerUser serverUser : serverUserList) {
                            if (serverUser.getUsername().equals(line.split(" ")[0])) {
                                if(serverUser.getGroupsId().contains(line.split(" ")[2])){
                                    out.println("u already are joined to group!");
                                    out.flush();
                                }else {
                                    serverUser.getGroupsId().add(line.split(" ")[2]);
                                    out.println("done successfully.");
                                    out.flush();
                                }
                                break;
                            }
                        }
                        System.out.println("join!");
//                        for (ServerUser serverUser : serverUserList) {
//                            if (serverUser.getGroupsId().contains(line.split(" ")[2])) {
////                                out = new PrintWriter(serverUser.getSocket().getOutputStream(), true);
////                                out.println(line.split(" ")[0] + " join to group with id " + line.split(" ")[2] + " say welcome plz!");
//                                System.out.println(line.split(" ")[0] + " join to group with id " + line.split(" ")[2] + " say welcome plz!");
//                                out.println("you are jointed to group.");
//                                out.flush();
//                            }
//                        }

                    } else if (line.split(" ")[1].equals("send")) {
                        String[] newline = line.split(" ");
                        boolean isNotInGroup = true;
                        for(ServerUser serverUser: serverUserList){
                            if(serverUser.getUsername().equals(newline[0])){
                                if(serverUser.getGroupsId().contains(newline[2])){
                                    isNotInGroup=false;
                                    break;
                                }
                            }
                        }
                        if(isNotInGroup){
                            out.println("at first u need to join to this group!");
                            out.flush();
                        }
                        else {
                            newline[0] = "";
                            newline[1] = "";
                            newline[2] = "";
                            String message = String.join(" ", newline);
                            System.out.println("user with username " + line.split(" ")[0] + ", send message to group with id " + line.split(" ")[2] + ": " + message);
//                            out.println(line.split(" ")[2] + " user with username " + line.split(" ")[0] + ", send message to group with id " + line.split(" ")[2] + ": " + message);
                            out.println("your message was send to group.");
                            out.flush();
                        }
                    } else if (line.split(" ")[1].equals("leave")) {
                        for (ServerUser serverUser : serverUserList) {
                            if (serverUser.getUsername().equals(line.split(" ")[0])) {
                                if(serverUser.getGroupsId().contains(line.split(" ")[2])){
                                    serverUser.getGroupsId().remove(line.split(" ")[2]);
                                    System.out.println("user with username "+ line.split(" ")[0]+ " leave from group with id "+line.split(" ")[2]);
                                    out.println("done successfully.");
                                    out.flush();
                                }else {
                                    out.println("u are not in this group that u wanna left!");
                                    out.flush();
                                }
                                break;
                            }
                        }
//                        System.out.println("leave!");
//                        out.println(line.split(" ")[0] + " leave group " + line.split(" ")[1] + " say bay plz!");
//                        out.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
