package objects;

public class FileUploadRequest extends FileRequest {

  private String fileContent;

  public FileUploadRequest(String fileName, String directoryPath) {
    super(fileName, directoryPath);
  }

  public FileUploadRequest(String fileName, String directoryPath, String fileContent) {
    super(fileName, directoryPath);
    this.fileContent = fileContent;
  }

  public String getFileContent() {
    return fileContent;
  }

  public void setFileContent(String fileContent) {
    this.fileContent = fileContent;
  }
}
