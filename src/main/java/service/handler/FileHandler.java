package service.handler;

import exception.FileOperationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import objects.FileFetchResponse;
import objects.FileRequest;
import objects.FileUploadRequest;
import objects.FileUploadResponse;

public class FileHandler {

  public static FileFetchResponse fetchFile(FileRequest fileFetchRequest)
      throws FileOperationException {
    if (Objects.isNull(fileFetchRequest.getFileName())) {
      throw new FileOperationException("File Name is invalid: " + fileFetchRequest.getFileName());
    }
    if (Objects.isNull(fileFetchRequest.getDirectoryPath())) {
      throw new FileOperationException(
          "Directory Path is invalid: " + fileFetchRequest.getDirectoryPath());
    }
    Path filePath =
        Paths.get(fileFetchRequest.getDirectoryPath(), fileFetchRequest.getFileName()).normalize();
    FileFetchResponse fileFetchResponse = new FileFetchResponse();
    if (!Files.exists(filePath)) {
      fileFetchResponse.setFileExists(false);
      return fileFetchResponse;
    }
    try {
      fileFetchResponse.setFileExists(true);
      fileFetchResponse.setContent(Files.readAllBytes(filePath));
    } catch (Exception ex) {
      throw new FileOperationException("File content access error: " + filePath);
    }
    return fileFetchResponse;
  }

  public static FileUploadResponse uploadFile(FileUploadRequest request) {
    File file = new File(request.getDirectoryPath() + File.separator + request.getFileName());
    FileUploadResponse response = null;
    try {
      FileWriter myWriter = new FileWriter(file);
      myWriter.write(request.getFileContent());
      myWriter.close();
      response = new FileUploadResponse();
      response.setUploadSuccess(true);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return response;
  }
}
