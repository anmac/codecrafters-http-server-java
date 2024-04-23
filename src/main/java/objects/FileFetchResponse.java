package objects;

public class FileFetchResponse {

  private boolean fileExists;
  private byte[] content;

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public boolean isFileExists() {
    return fileExists;
  }

  public void setFileExists(boolean fileExists) {
    this.fileExists = fileExists;
  }
}
