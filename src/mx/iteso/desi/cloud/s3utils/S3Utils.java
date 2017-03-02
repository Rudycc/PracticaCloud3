/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.iteso.desi.cloud.s3utils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author parres
 */
public class S3Utils {
    AmazonS3 s3;
    String bucket;
    
    public S3Utils(String accessKey, String secretKey, Regions region, String bucket) {
        AWSCredentialsProvider credProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
        s3 = AmazonS3ClientBuilder.standard().withCredentials(credProvider).withRegion(region).build();
        
        this.bucket = bucket;
    }
    
    public void addFile(String key, InputStream in, long size) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(size);
        PutObjectRequest por = new PutObjectRequest(this.bucket,key,in,meta);
                
        s3.putObject(por);
    }
    
    public List<String> listFiles() {
        List<String> ret = new ArrayList<>();
        
        for(S3ObjectSummary o : this.s3.listObjects(this.bucket).getObjectSummaries()) {
            ret.add(o.getKey());
        }
        
        return ret;
    }
        
}
