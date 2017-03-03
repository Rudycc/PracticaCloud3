/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.iteso.desi.cloud.hw3;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import java.nio.ByteBuffer;

/**
 *
 * @author parres
 */
public class AWSFaceCompare {

    String srcBucket;
    AmazonRekognition arek;
    String accessKey;
    String secretKey;
    Regions region;

    public AWSFaceCompare(String accessKey, String secretKey, Regions region,String srcBucket) {
        this.srcBucket = srcBucket;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        
        AWSCredentialsProvider credProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
        arek = AmazonRekognitionClientBuilder.standard().withCredentials(credProvider).withRegion(region).build();
    }

    public Face compare(ByteBuffer imageBuffer) {
        // TODO
        return null;
    }

}
