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
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import java.nio.ByteBuffer;
import java.util.List;
import mx.iteso.desi.cloud.s3utils.S3Utils;

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
        Face bestFace = new Face("",0F);
        
        S3Utils s3 = new S3Utils(accessKey,secretKey,region,srcBucket);
        List<String> names = s3.listFiles();
        for(String name : names) {
            
            if(!name.endsWith(".jpg")) {
                continue;
            }
            
            System.out.println("Comparing with: "+name);
            
            CompareFacesRequest compareFaceReq = new CompareFacesRequest()
                .withSourceImage(new Image().withBytes(imageBuffer))
                .withTargetImage(new Image().withS3Object(
                    new S3Object().withBucket(srcBucket).withName(name)))
                .withSimilarityThreshold(75F);
            
            CompareFacesResult result = arek.compareFaces(compareFaceReq);
            for(CompareFacesMatch match : result.getFaceMatches()) {
                System.out.println("Compare with "+name+" "+match.getSimilarity());
                if(match.getSimilarity() > bestFace.getCofidence()) {
                    bestFace.name = name;
                    bestFace.cofidence = match.getSimilarity();
                }
            }
        }
                
        return bestFace;
    }

}
