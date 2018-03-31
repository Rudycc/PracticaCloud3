/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.iteso.desi.cloud.hw3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.S3Object;

import java.util.ArrayList;
import java.util.List;

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
        this.region = region;
        
        /*
         * @TODO
         * Build AmazonRekognition Object.
        */
        arek = AmazonRekognitionClientBuilder
        .standard()
        .withRegion(region)
        .build();
    }

    public Face compare(ByteBuffer imageBuffer) {
        Image source = new Image().withBytes(imageBuffer);
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(this.region).build();
        ListObjectsV2Result result = s3.listObjectsV2(this.srcBucket);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        List<Face> faces = new ArrayList<>();

        for (S3ObjectSummary os: objects) {
            CompareFacesRequest request = new CompareFacesRequest()
               .withSourceImage(source)
               .withTargetImage(new Image()
               .withS3Object(new S3Object()
               .withName(os.getKey()).withBucket(this.srcBucket)));

               CompareFacesResult compareFacesResult = arek.compareFaces(request);
               List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
               for (CompareFacesMatch match: faceDetails){
                   faces.add(new Face(os.getKey(), match.getFace().getConfidence()));
                   System.out.println("Name: " + os.getKey() + " Confidence: " + match.getFace().getConfidence());
                }
        }
        Face closest = null;
        if (faces.size() > 0){
            closest = faces.get(0);
        }
        for (Face f: faces) {
            if (f.getCofidence() > closest.getCofidence()) {
                closest = f;
            }
        }

        return closest;
    }

}
