package pac1;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.nio.file.Paths;


//DownloadObjectの使い方
//
//1.インスタンス化
//DownloadObject doo = new DownloadObject();
//
//2.メソッド呼び出し
//doo.downloadObject("000001","/usr/local/tomcat/webapps/mystok/Picture/RyouriPIC/my-cloudstorage-download-test000001.jpg");
//doo.downloadObject("000001","/usr/local/tomcat/webapps/mystok/Downloaded/000001.jpg");
//第一引数は"ダウンロード対象ファイルの名前"
//第二引数は"ファイルのダウンロード先への絶対パス(ファイル名も含む)"


public class DownloadObject {
  public static void downloadObject(String objectName, String destFilePath) {
    // The ID of your GCP project
    String projectId = "my-kubernetes-test-20200822";

    // The ID of your GCS bucket
    String bucketName = "mystok-bucket";

    // The ID of your GCS object
    // String objectName = "your-object-name";

    // The path to which the file should be downloaded
    // String destFilePath = "/local/path/to/file.txt";

    Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

    Blob blob = storage.get(BlobId.of(bucketName, objectName));
    blob.downloadTo(Paths.get(destFilePath));

    System.out.println(
        "Downloaded object "
            + objectName
            + " from bucket name "
            + bucketName
            + " to "
            + destFilePath);
  }
}
