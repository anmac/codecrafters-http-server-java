package builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import objects.HttpRequest;

public class HttpRequestBuilder {

  private String method;
  private String path;
  private String version;
  private Map<String, String> headers;
  private String body;

  public HttpRequestBuilder() {
    this.headers = new HashMap<>();
  }

  public HttpRequestBuilder method(String method) {
    this.method = method;
    return this;
  }

  public HttpRequestBuilder path(String path) {
    this.path = path;
    return this;
  }

  public HttpRequestBuilder version(String version) {
    this.version = version;
    return this;
  }

  public HttpRequestBuilder header(String name, String value) {
    this.headers.put(name, value);
    return this;
  }

  public HttpRequestBuilder body(String content) {
    this.body = content;
    return this;
  }

  public HttpRequest build() {
    HttpRequest httpRequest = new HttpRequest(method, path);
    httpRequest.setVersion(version);
    httpRequest.setHeaders(headers);
    if (Objects.nonNull(body)) httpRequest.setBody(body);
    return httpRequest;
  }

  public static HttpRequest parseFromInputStream(InputStream inputStream) {
    HttpRequestBuilder builder = new HttpRequestBuilder();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    try {
      String requestLine = reader.readLine();
      if (requestLine == null) {
        throw new Exception("Request is Empty");
      }
      String[] requestParts = requestLine.split(" ");
      if (requestParts.length != 3) {
        throw new Exception("Method, Path or Version is missing");
      }
      builder.method(requestParts[0]);
      builder.path(requestParts[1]);
      builder.version(requestParts[2]);
      String headerLine;
      while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
        int colonIndex = headerLine.indexOf(':');
        if (colonIndex > 0) {
          String key = headerLine.substring(0, colonIndex).trim();
          String value = headerLine.substring(colonIndex + 1).trim();
          builder.header(key, value);
        }
      }

      int contentLength = Integer.parseInt(builder.headers.getOrDefault("Content-Length", "0"));
      if (contentLength > 0) {
        StringBuilder bodyBuilder = new StringBuilder();
        int c;
        for (int i = 0; i < contentLength && (c = reader.read()) != -1; i++) {
          bodyBuilder.append((char) c);
        }
        builder.body(bodyBuilder.toString());
      }
    } catch (Exception e) {
      System.out.println("(builder.HttpRequestBuilder) Exception: " + e.getMessage());
      throw new RuntimeException(e.getMessage());
    }

    return builder.build();
  }
}
