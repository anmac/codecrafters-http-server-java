package service.handler.impl;

import builder.HttpResponseBuilder;
import exception.FileOperationException;
import java.util.Map;
import objects.FileFetchResponse;
import objects.FileRequest;
import objects.HttpRequest;
import objects.HttpResponse;
import service.handler.FileHandler;
import service.handler.RequestHandler;

public class GetRequestHandler implements RequestHandler {

  @Override
  public HttpResponse handle(HttpRequest request) throws FileOperationException {
    HttpResponse response = null;
    String body = null;
    String path = request.getPath();
    FileFetchResponse fileFetchResponse = null;
    Map<String, String> arguments = request.getArguments();
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
      body = request.getPath().substring("/echo".length() + 1);
      response =
          new HttpResponseBuilder()
              .version("HTTP/1.1")
              .status("OK")
              .statusCode(200)
              .method("GET")
              .body(body)
              .addHeader("Content-Length", String.valueOf(body.length()))
              .addHeader("Content-Type", "text/plain")
              .build();
    } else if (path.equals("/user-agent")) {
      body = request.getHeaders().getOrDefault("User-Agent", "");
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
    } else if (path.startsWith("/files/")
        && (fileFetchResponse =
                FileHandler.fetchFile(
                    new FileRequest(
                        path.substring("/files/".length()), arguments.get("directory"))))
            != null
        && fileFetchResponse.isFileExists()) {
      body = new String(fileFetchResponse.getContent());
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
    return response;
  }
}
