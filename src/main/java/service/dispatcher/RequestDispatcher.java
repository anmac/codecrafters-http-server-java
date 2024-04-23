package service.dispatcher;

import builder.HttpResponseBuilder;
import exception.FileOperationException;
import objects.HttpRequest;
import objects.HttpResponse;
import service.handler.RequestHandler;
import service.handler.impl.GetRequestHandler;
import service.handler.impl.PostRequestHandler;

public class RequestDispatcher {

  private final RequestHandler getRequestHandler;
  private final RequestHandler postRequestHandler;

  public RequestDispatcher() {
    getRequestHandler = new GetRequestHandler();
    postRequestHandler = new PostRequestHandler();
  }

  public HttpResponse dispatch(HttpRequest httpRequest) throws FileOperationException {
    String method = httpRequest.getMethod();
    return switch (method) {
      case "GET" -> getRequestHandler.handle(httpRequest);
      case "POST" -> postRequestHandler.handle(httpRequest);
      default -> new HttpResponseBuilder()
          .version("HTTP/1.1")
          .status("Method Not Allowed")
          .statusCode(405)
          .addHeader("Content-Type", "text/plain")
          .addHeader("Content-Length", "0")
          .build();
    };
  }
}
