import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Application {
    private static Logger LOG = LoggerFactory.getLogger(Application.class);
    private static AWSCredentials CREDENTIALS;
    private static  AmazonS3 S3_CLIENT;

    public static void main(String[] args) {
        CREDENTIALS = new BasicAWSCredentials(
                "AKIAJGXGP67IDEQPLHHQ", "wf0eSkGqfwq3JsQor6tN+Inolq+tvkU+5x1h1/u2");

        S3_CLIENT = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(CREDENTIALS))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();

        handleBuckets();
        handleObjects();
    }

    private static void handleBuckets() {
        //createBucket("empeti-aws-bucket3");
        //createBucket("empeti-aws-bucket4");
        //createBucket("empeti-aws-bucket5");
        //deleteBucket("empeti-aws-bucket2");
        listBuckets();
    }

    private static void handleObjects(){
        //uploadObject("empeti-aws-bucket3","hello.txt", "hello.txt");
        listObjects("empeti-aws-bucket3");
    }

    private static void listObjects(String bucketName) {
        ObjectListing objectListing = S3_CLIENT.listObjects(bucketName);
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
            System.out.println(os.getKey());
        }
    }

    private static void uploadObject(String bucketName, String AWSFilePath, String filePath) {
        ClassLoader classloader = new Application().getClass().getClassLoader();
        S3_CLIENT.putObject(bucketName, AWSFilePath, new File(classloader.getResource(filePath).getFile()));
    }

    private static void listBuckets() {
        S3_CLIENT.listBuckets().stream()
                .map(bucket -> bucket.getName()).forEach(System.out::println);
    }

    private static void createBucket(String bucketName) {
        if (S3_CLIENT.doesBucketExist(bucketName)){
            LOG.info("Bucket exists already with name" + bucketName + ". Choose different name!");
        }
        S3_CLIENT.createBucket(bucketName);
    }

    private static void deleteBucket(String bukcetName){
        try{
            S3_CLIENT.deleteBucket(bukcetName);
        }
        catch (AmazonServiceException e){
            System.err.println(e.getErrorMessage());
        }
    }
}
