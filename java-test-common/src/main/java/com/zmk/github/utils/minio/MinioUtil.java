package com.ctfo.os.cms.common.util.minio;

import com.ctfo.os.cms.common.exception.BusinessException;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MinioUtil {
    private static final Logger logger = LoggerFactory.getLogger(MinioUtil.class);
    @Value("${minio.server.host}")
    private String MINIO_HOST;
    @Value("${minio.server.accessKey}")
    private String MINIO_ACCESSKEY;
    @Value("${minio.server.secretKey}")
    private String MINIO_SECRETKEY;

    public MinioUtil() {
    }

    public MinioUtil(String host, String accessKey, String secretKey) {
        this.MINIO_HOST = host;
        this.MINIO_ACCESSKEY = accessKey;
        this.MINIO_SECRETKEY = secretKey;
    }

    /**
     * MinioClient.getObject() example.
     */
    public InputStream getStream(String bucketName, String minioPathName) {
        // TODO: 2020/11/26  返回流的形式，未完成
        /* play.min.io for test and development. */
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(MINIO_HOST)
                        .credentials(MINIO_ACCESSKEY, MINIO_SECRETKEY)
                        .build();

        // Get input stream to have content of 'my-objectname' from 'my-bucketname'
        try {

            InputStream stream =
                    minioClient.getObject(
                            GetObjectArgs.builder().bucket(bucketName).object(minioPathName).build());

            return stream;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[MinioUtil] download error: {}", ex.getMessage());
        }
        return null;

    }

    public File getFile(String tmpDir, String bucket, String fileName) {
        InputStream is = getStream(bucket, fileName);
        File dir = new File(tmpDir);
        if (!dir.exists() || dir.isFile()) {
            dir.mkdirs();
        }
        File file = new File(tmpDir + fileName + ".zip");
        try {
            FileUtils.copyToFile(is, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public String getDownloadUrl(String minioPath) {
        return String.format("%s%s", MINIO_HOST, minioPath);
    }


    public void removeObject(String bucketName, String objectName) {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(MINIO_HOST)
                        .credentials(MINIO_ACCESSKEY, MINIO_SECRETKEY)
                        .build();
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("移除minio制品异常! ex: {}", ex.getMessage());

        }
    }

    /**
     * 获取一个bucket 下的文件列表
     *
     * @param bucketName
     * @return
     */
    public List<Item> getBucketFilePathList(String bucketName, String prefix) {
        List resultList = new ArrayList();
        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(MINIO_HOST)
                            .credentials(MINIO_ACCESSKEY, MINIO_SECRETKEY)
                            .build();


            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
//                                    .region("test-zmk")
//                                        .startAfter("test-zmk")
                            .prefix(prefix)
                            .recursive(true)
//                                    .maxKeys(100)
                            .build());

            for (Result<Item> resultItem : results) {
                Item item = resultItem.get();
//                System.out.println(item.lastModified() + "\t" + item.size() + "\t" + item.objectName());
//                MinioFileItem fileItem = new MinioFileItem();
//                fileItem.setObjectName(item.objectName());
//                fileItem.setDir(item.isDir());
                resultList.add(item);
            }
            return resultList;
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
        return resultList;
    }

    public void putObject(InputStream inputStream, String bucketName, String filePath) {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(MINIO_HOST)
                        .credentials(MINIO_ACCESSKEY, MINIO_SECRETKEY)
                        .build();
        try {

            // Create object 'my-objectname' in 'my-bucketname' with content from the input stream.
            createBucketIfNotExist(bucketName);
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(filePath).stream(
                            inputStream, inputStream.available(), -1)
                            .build());
            logger.debug("bucketName: {},{} is uploaded successfully", bucketName, filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("上传二进制制品失败 ！ ex: {}", ex.getMessage());
            throw BusinessException.DialogException("上传失败!");
        }
    }

    public String putObject(InputStream inputStream, Long objectSize, String bucketName, String filePath, String contentType) {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(MINIO_HOST)
                        .credentials(MINIO_ACCESSKEY, MINIO_SECRETKEY)
                        .build();
        try {
            logger.info("bucketName: {},{} objectSize: {} start upload!!!", bucketName, filePath, objectSize);
            // Create object 'my-objectname' in 'my-bucketname' with content from the input stream.
            createBucketIfNotExist(bucketName);
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(filePath).contentType(contentType).stream(
                            inputStream, objectSize, -1)
                            .build());
            logger.info("bucketName: {},{} is uploaded successfully", bucketName, filePath);
            return MINIO_HOST + bucketName + "/" + filePath;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("上传二进制制品失败 ！ ex: {}", ex.getMessage());
            throw BusinessException.DialogException("上传失败!");
        }
    }

    public String putObjectNoPrefix(InputStream inputStream, Long objectSize, String bucketName, String filePath, String contentType) {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(MINIO_HOST)
                        .credentials(MINIO_ACCESSKEY, MINIO_SECRETKEY)
                        .build();
        try {
            logger.info("bucketName: {},{} objectSize: {} start upload!!!", bucketName, filePath, objectSize);
            // Create object 'my-objectname' in 'my-bucketname' with content from the input stream.
            createBucketIfNotExist(bucketName);
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(filePath).contentType(contentType).stream(
                            inputStream, objectSize, -1)
                            .build());
            logger.info("bucketName: {},{} is uploaded successfully", bucketName, filePath);
            return bucketName + "/" + filePath;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("上传二进制制品失败 ！ ex: {}", ex.getMessage());
            throw BusinessException.DialogException("上传失败!");
        }
    }

    public static void main(String[] args) {
        MinioUtil minioUtil = new MinioUtil("http://minio.dev.ctfo.com/", "zq025JHjgbyQF8MfLYKt", "XGCLDGDK0fYWBWZE84CUrxHGcnepg42huZH4KVwp");
//        minioUtil.getFile("P://test", "jenkins", "test-zmk");
//        String url = minioUtil.getDownloadUrl("jenkins/test-zmk/start-1.0.1-SNAPSHOT.jar");
        minioUtil.getBucketFilePathList("jenkins", "test-zmk/");
//        minioUtil.getBucketFilePathList("ctfo-os-cms",null);

//        System.out.printf(url);
//        minioUtil.test();
//        List result = _getFileNameList("jenkins", "test-zmk", null);
//        System.out.println(JSON.toJSONString(result));


    }


    public void createBucketIfNotExist(String bucketName) {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(MINIO_HOST)
                        .credentials(MINIO_ACCESSKEY, MINIO_SECRETKEY)
                        .build();
        try {
            // Create bucket 'my-bucketname' if it doesn`t exist.
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                logger.info("createBucket successfully : {}", bucketName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("createBucket error ！ ex: {}", ex.getMessage());
            throw BusinessException.MailException("minio bucket 查询失败!");
        }
    }

    public String getPresignedObjectUrl(String bucketName, String objectName) {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(MINIO_HOST)
                        .credentials(MINIO_ACCESSKEY, MINIO_SECRETKEY)
                        .build();
        try {

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(60 * 60 * 6)
                            .build());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getPresignedObjectUrl error ！ ex: {}", ex.getMessage());
            throw BusinessException.MailException("获取 url失败!");
        }
    }


}
