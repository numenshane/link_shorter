package com.linkshorter.demo.repository;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.linkshorter.demo.DB.SourceUrl;
import com.linkshorter.demo.DemoApplication;
import org.springframework.stereotype.Repository;

@Repository
public class DynamoDBRepository {

    //用户凭证对象
    private static AWSCredentialsProvider awsCredentialsProvider = new AWSCredentialsProvider() {

        public void refresh() {
        }

        public AWSCredentials getCredentials() {
            String AWSAccessKeyId = DemoApplication.getArgsByName("key");
            String AWSSecretKey = DemoApplication.getArgsByName("secret");
            return new BasicAWSCredentials(AWSAccessKeyId, AWSSecretKey);
        }
    };
    private static DynamoDBMapper dbMapper = null;


    DynamoDBRepository() {
        AmazonDynamoDB amazonDynamoDBClient = AmazonDynamoDBClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion(Regions.AP_SOUTHEAST_1).build();
        dbMapper = new DynamoDBMapper(amazonDynamoDBClient);
    }

    public SourceUrl getItemById(String rootUrl, String suffix) {
        return dbMapper.load(SourceUrl.class, rootUrl, suffix);
    }

    public void save(SourceUrl sourceUrl) {
        dbMapper.save(sourceUrl);
    }
}
