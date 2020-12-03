package mystok;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UploadObject {
  public static void uploadObject(
      String projectId, String bucketName, String objectName, String filePath) throws IOException {
    // The ID of your GCP project
    String projectId = this.projectId;

    // The ID of your GCS bucket
    String bucketName = this.bucketName;

    // The ID of your GCS object
    String objectName = this.objectName;

    // The path to your file to upload
    String filePath = this.filePath;

    Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
    BlobId blobId = BlobId.of(bucketName, objectName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
    storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

    System.out.println(
        "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
  }
}
