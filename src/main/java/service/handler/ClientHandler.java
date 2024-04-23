package service.handler;

import builder.HttpRequestBuilder;
import builder.HttpResponseBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import objects.HttpRequest;
import objects.HttpResponse;
import service.dispatcher.RequestDispatcher;

public class ClientHandler implements Runnable {

  private final Socket clientSocket;
  private final Map<String, String> arguments;
  private final RequestDispatcher requestDispatcher;

  public ClientHandler(Socket clientSocket, Map<String, String> arguments) {
    this.clientSocket = clientSocket;
    this.arguments = arguments;
    this.requestDispatcher = new RequestDispatcher();
  }

  @Override
  public void run() {
    try {
      HttpRequest httpRequest =
          HttpRequestBuilder.parseFromInputStream(clientSocket.getInputStream());
      httpRequest.setArguments(arguments);
      System.out.println(httpRequest);

      HttpResponse response = requestDispatcher.dispatch(httpRequest);
      if (Objects.isNull(response)) {
        response =
            new HttpResponseBuilder()
                .version("HTTP/1.1")
                .status("Method Not Allowed")
                .statusCode(405)
                .addHeader("Content-Type", "text/plain")
                .addHeader("Content-Length", "0")
                .build();
      }
      OutputStream os = clientSocket.getOutputStream();
      os.write(response.parseResponse().getBytes());
      os.close();
    } catch (Exception e) {
      System.out.println("(service.handler.ClientHandler) IOException: " + e.getMessage());
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private boolean isFileInDirectory(String path) {
    if (!path.startsWith("/files/")) {
      return false;
    }
    if (!arguments.containsKey("directory")) {
      return false;
    }
    String directoryPath = arguments.get("directory");
    String filename = path.substring("/files/".length());
    System.out.println("Directory: " + directoryPath);
    System.out.println("File Name: " + filename);
    Path filePath = Paths.get(directoryPath, filename).normalize();
    return Files.exists(filePath) && Files.isRegularFile(filePath);
  }
}
