package service.handler;

import exception.FileOperationException;
import objects.HttpRequest;
import objects.HttpResponse;

public interface RequestHandler {
  HttpResponse handle(HttpRequest request) throws FileOperationException;
}
