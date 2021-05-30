package com.learn.coe.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.learn.coe.service.oss.service.FileService;
import com.learn.coe.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author coffee
 * @Date 2021-05-15 16:45
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String module, String originalFilename) {

        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String bucket = ossProperties.getBucket();

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        if (!ossClient.doesBucketExist(bucket)){
            ossClient.createBucket(bucket);
            ossClient.setBucketAcl(bucket, CannedAccessControlList.PublicRead);
        }

        //构建objectName:文件路径  avatar/2021/05/15/default.jpg
        String folder = new DateTime().toString("yyyy/MM/dd");
        String fileName = UUID.randomUUID().toString();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String key = module + "/" + folder + "/" + fileName + fileExtension;

        // 上传文件流
        ossClient.putObject(bucket, key, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        //返回url
        return "https://" + bucket + "." + endpoint + "/" + key;
    }

    @Override
    public void removeFile(String url) {

        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String bucket = ossProperties.getBucket();

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //删除文件
        String host = "http://" + bucket + "." + endpoint + "/";//主机名
        String objectName = url.substring(host.length());
        ossClient.deleteObject(bucket,objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }


}
