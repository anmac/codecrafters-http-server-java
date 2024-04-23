package service.handler.impl;

import builder.HttpResponseBuilder;
import objects.FileUploadRequest;
import objects.FileUploadResponse;
import objects.HttpRequest;
import objects.HttpResponse;
import service.handler.FileHandler;
import service.handler.RequestHandler;

public class PostRequestHandler implements RequestHandler {

  @Override
  public HttpResponse handle(HttpRequest request) {
    String path = request.getPath();
    HttpResponse response = null;
    if (path.startsWith("/files/")) {
      String fileName = path.substring("/files/".length());
      System.out.println(fileName);
      String directory = request.getArguments().get("directory");
      FileUploadRequest uploadRequest = new FileUploadRequest(fileName, directory);
      uploadRequest.setFileContent(request.getBody());
      FileUploadResponse uploadResponse = FileHandler.uploadFile(uploadRequest);
      if (uploadResponse.isUploadSuccess()) {
        response =
            new HttpResponseBuilder().version("HTTP/1.1").status("OK").statusCode(201).build();
      }
    }
    return response;
  }
}
