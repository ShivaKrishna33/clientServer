import java.io.*;
import java.net.Socket;
import java.util.Date;

public class WebClient {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change to the actual server address
        int port = 8081; // Change to the server's port
        String requestedFile = "/clientServer.txt"; // Change to the desired file path

        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send an HTTP GET request to the server
            out.println("GET " + requestedFile + " HTTP/1.1");
            out.println("Host: " + serverAddress);
            out.println();

            long startTime = System.currentTimeMillis(); // Measure the start time

            // Receive and display the response
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

            long endTime = System.currentTimeMillis(); // Measure the end time
            long rtt = endTime - startTime; // Calculate Round Trip Time (RTT)
            System.out.println("Round Trip Time (RTT): " + rtt + " ms");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
