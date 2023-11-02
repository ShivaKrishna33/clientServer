import java.io.*;
import java.net.*;
import java.util.Date;

public class WebServer {
    public static void main(String[] args) {
        int port = 8081;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Web server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Read the HTTP request from the client
            String request = in.readLine();
            if (request != null && request.startsWith("GET")) {
                // Parse the request and extract the requested file
                String[] requestParts = request.split(" ");
                if (requestParts.length >= 2) {
                    String requestedFile = requestParts[1];
                    
                    // Serve a text file as the response (replace "mytextfile.txt" with your file's path)
                    File file = new File("C:\\Users\\Frank\\Desktop\\clientServer\\clientServer.txt");
                    if (file.exists() && !file.isDirectory()) {
                        out.println("HTTP/1.1 200 OK");
                        out.println("Date: " + new Date());
                        out.println("Content-Type: text"); // Adjust content type
                        out.println();

                        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                            String line;
                            while ((line = fileReader.readLine()) != null) {
                                out.println(line);
                            }
                        }
                    } else {
                        out.println("HTTP/1.1 404 Not Found");
                    }
                }
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
