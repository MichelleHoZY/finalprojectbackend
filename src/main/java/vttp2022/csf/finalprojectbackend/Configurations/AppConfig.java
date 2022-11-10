package vttp2022.csf.finalprojectbackend.Configurations;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AppConfig {

    private Logger logger = Logger.getLogger(AppConfig.class.getName());

    @Value("${cors.pathMapping}")
    String pathMapping;

    @Value("${cors.origins}")
    String origins;

    @Value("${cors.pathMapping2}")
    String pathMapping2;

    @Value("${cors.pathMapping3}")
    String pathMapping3;

    @Value("${spaces.endpoint}")
    private String endpoint;

    @Value("${spaces.region}")
    private String region;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        logger.info("CORS: pathMapping=%s, pathMapping2=%s, origins=%s".formatted(pathMapping, pathMapping2, origins));
        return new CORSConfiguration(pathMapping, pathMapping2, pathMapping3, origins);
    }

    @Bean
    AmazonS3 createS3Client() {
        final String accessKey = System.getenv("S3_ACCESS_KEY");
        final String secretKey = System.getenv("S3_SECRET_KEY");

        // S3 credentials
        final BasicAWSCredentials basicCred = new BasicAWSCredentials(accessKey, secretKey);
        final AWSStaticCredentialsProvider credProv = new AWSStaticCredentialsProvider(basicCred);

        final EndpointConfiguration endpointConfig = new EndpointConfiguration(endpoint, region);

        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(endpointConfig)
            .withCredentials(credProv) // can't log in without credentials
            .build();
    }
    
}