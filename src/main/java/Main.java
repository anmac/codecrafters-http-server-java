import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import service.handler.ClientHandler;

public class Main {

  private static final boolean running = true;

  public static void main(String[] args) {
    Map<String, String> arguments = commandLineArguments(args);

    try (ServerSocket serverSocket = new ServerSocket(4221)) {
      serverSocket.setReuseAddress(true);

      while (running) {
        Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
        System.out.println("accepted new connection");

        ClientHandler clientHandler = new ClientHandler(clientSocket, arguments);
        Thread thread = new Thread(clientHandler);
        thread.start();
      }
    } catch (IOException e) {
      System.out.println("(Main) IOException: " + e.getMessage());
    }
  }

  private static Map<String, String> commandLineArguments(String[] args) {
    Map<String, String> arguments = new HashMap<>();
    for (int i = 0; i < args.length; i++) {
      if (args[i].startsWith("--") && i + 1 < args.length) {
        String key = args[i].substring(args[i].lastIndexOf('-') + 1);
        String value = args[i + 1];
        arguments.put(key, value);
        i++;
      }
    }
    return arguments;
  }
}
