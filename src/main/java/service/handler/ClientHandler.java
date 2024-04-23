package service.handler;

import builder.HttpRequestBuilder;
import builder.HttpResponseBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import objects.HttpRequest;
import objects.HttpResponse;

public class ClientHandler implements Runnable {

  private final Socket clientSocket;
  private final Map<String, String> arguments;

  public ClientHandler(Socket clientSocket, Map<String, String> arguments) {
    this.clientSocket = clientSocket;
    this.arguments = arguments;
  }

  @Override
  public void run() {
    try {
      HttpRequest httpRequest =
          HttpRequestBuilder.parseFromInputStream(clientSocket.getInputStream());
      System.out.println(httpRequest.toString());
      System.out.println("Arguments: " + arguments);

      HttpResponse response = null;
      String body = null;
      String path = httpRequest.getPath();

      if (path.equals("/")) {
        response =
            new HttpResponseBuilder()
                .version("HTTP/1.1")
                .status("OK")
                .statusCode(200)
                .method("GET")
                .addHeader("Content-Length", String.valueOf(0))
                .addHeader("Content-Type", "text/plain")
                .build();
      } else if (path.startsWith("/echo")) {
        body = httpRequest.getPath().substring("/echo".length() + 1);
        response =
            new HttpResponseBuilder()
                .version("HTTP/1.1")
                .status("OK")
                .statusCode(200)
                .method("GET")
                .addHeader("Content-Length", String.valueOf(body.length()))
                .addHeader("Content-Type", "text/plain")
                .body(body)
                .build();
      } else if (path.equals("/user-agent")) {
        body = httpRequest.getHeaders().getOrDefault("User-Agent", "");
        response =
            new HttpResponseBuilder()
                .version("HTTP/1.1")
                .status("OK")
                .statusCode(200)
                .method("GET")
                .addHeader("Content-Length", String.valueOf(body.length()))
                .addHeader("Content-Type", "text/plain")
                .body(body)
                .build();
      } else if (isFileInDirectory(path)) {
        String directoryPath = arguments.get("directory");
        String filename = path.substring("/files/".length());
        Path filePath = Paths.get(directoryPath, filename).normalize();
        body = new String(Files.readAllBytes(filePath));
        response =
            new HttpResponseBuilder()
                .version("HTTP/1.1")
                .status("OK")
                .statusCode(200)
                .method("GET")
                .addHeader("Content-Length", String.valueOf(body.length()))
                .addHeader("Content-Type", "application/octet-stream")
                .body(body)
                .build();
      } else {
        response =
            new HttpResponseBuilder()
                .version("HTTP/1.1")
                .status("NOT FOUND")
                .statusCode(404)
                .method("GET")
                .addHeader("Content-Length", String.valueOf(0))
                .addHeader("Content-Type", "text/plain")
                .build();
      }

      OutputStream os = clientSocket.getOutputStream();
      os.write(response.parseResponse().getBytes(StandardCharsets.UTF_8));
      os.close();
      clientSocket.close();
    } catch (IOException e) {
      System.out.println("(service.handler.ClientHandler) IOException: " + e.getMessage());
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
