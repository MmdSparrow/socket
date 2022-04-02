import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Client extends Thread {

    public static void main(String[] args) {
        String username;
        List<String> groupsId = new ArrayList<>();

        try (Socket socket = new Socket("localhost", 8080)) {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);


            BufferedReader in
                    = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            Scanner sc = new Scanner(System.in);

            System.out.print("username: ");
            username = sc.nextLine();
            out.println("username " + username);
            out.flush();

            Thread readMessage = new Thread(() -> {
                BufferedReader FromServer =
                        null;
                try {
                    FromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DataOutputStream outToServer = null;
                try {
                    outToServer = new DataOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader inFromUser =
                        new BufferedReader(new InputStreamReader(System.in));

                while (true) {
                    while (true) {
                        try {
                            assert FromServer != null;
                            if (!FromServer.ready()) break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            System.out.println(FromServer.readLine());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        if (inFromUser.ready()) {
                            int ch = inFromUser.read();

                            assert outToServer != null;
                            outToServer.writeChar(ch);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });
            readMessage.start();

            String line = null;

            while (!"quit".equalsIgnoreCase(line)) {

                Scanner scanner = new Scanner(System.in);
                line = scanner.nextLine();

                switch (line.split(" ")[0]) {
                    case "join" -> {
                        groupsId.add(line.split(" ")[1]);
                        out.println(username + " " + line);
                        out.flush();

                        try {
                            System.out.println(in.readLine());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    case "send" -> {
                        try {
                            String s = in.readLine();
                            if (groupsId.contains(s.split(" ")[0])) {
                                System.out.println(String.join(" ", s.split(" ")[0] = ""));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    case "leave" -> {
                        groupsId.remove(line.split(" ")[1]);
                        out.println(username + " " + line);
                        out.flush();

                        try {
                            System.out.println(in.readLine());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
