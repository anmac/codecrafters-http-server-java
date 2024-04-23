package objects;

import java.util.Map;

public class HttpRequest {

  private String method;
  private String path;
  private String version;
  private String body;
  private Map<String, String> headers;
  private Map<String, String> arguments;

  public HttpRequest(String method, String path) {
    this.method = method;
    this.path = path;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public Map<String, String> getArguments() {
    return arguments;
  }

  public void setArguments(Map<String, String> arguments) {
    this.arguments = arguments;
  }

  @Override
  public String toString() {
    return "objects.HttpRequest{"
        + "method='"
        + method
        + '\''
        + ", path='"
        + path
        + '\''
        + ", version='"
        + version
        + '\''
        + ", headers="
        + headers
        + '\''
        + ", body="
        + body
        + '}';
  }
}
