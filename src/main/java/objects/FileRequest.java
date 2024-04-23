package objects;

public class FileRequest {

  private String fileName;
  private String directoryPath;

  public FileRequest(String fileName, String directoryPath) {
    this.fileName = fileName;
    this.directoryPath = directoryPath;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getDirectoryPath() {
    return directoryPath;
  }

  public void setDirectoryPath(String directoryPath) {
    this.directoryPath = directoryPath;
  }
}
