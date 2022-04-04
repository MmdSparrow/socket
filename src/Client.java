import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

//            Thread readMessage = new Thread(() -> {
//                try {
//                    System.out.println(in.readLine());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            });
//            readMessage.start();

            String line = null;

            while (!"quit".equalsIgnoreCase(line)) {

                Scanner scanner = new Scanner(System.in);
                line = scanner.nextLine();
//                System.out.println(line);

                out.println(username + " " + line);
                out.flush();


                System.out.println(in.readLine());

            }

            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
