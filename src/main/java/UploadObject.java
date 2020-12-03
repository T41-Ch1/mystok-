package mystok;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UploadObject {
  public static void uploadObject() throws IOException {
    //String projectId, String bucketName, String objectName, String filePath
    // The ID of your GCP project
    String projectId = "my-kubernetes-test-20200822";

    // The ID of your GCS bucket
    String bucketName = "mystok-bucket";

    // The ID of your GCS object
    String objectName = "000001";

    // The path to your file to upload
    String filePath = "/Picture/RyouriPIC/ryouri000001.jpg";

    Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
    BlobId blobId = BlobId.of(bucketName, objectName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
    storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

    System.out.println(
        "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
  }
}
