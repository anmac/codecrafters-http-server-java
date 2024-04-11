import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args) {
    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept(); // Wait for connection from client.
      System.out.println("accepted new connection");

      InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
      BufferedReader br = new BufferedReader(isr);
      String[] parts = br.readLine().split(" ");

      if (parts[1].equals("/")) {
        clientSocket
            .getOutputStream()
            .write("HTTP/1.1 200 OK\r\n\r\n".getBytes(StandardCharsets.UTF_8));
      } else {
        clientSocket
            .getOutputStream()
            .write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes(StandardCharsets.UTF_8));
      }

    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
