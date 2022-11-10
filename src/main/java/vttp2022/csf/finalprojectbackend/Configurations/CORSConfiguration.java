package vttp2022.csf.finalprojectbackend.Configurations;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CORSConfiguration implements WebMvcConfigurer{
    
    private String path;
    private String path2;
    private String path3;
    private String origins;

    public CORSConfiguration(String p, String p2, String p3, String o) {
        path = p; // /order/* 
        path2 = p2;
        path3 = p3;
        origins = o;
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping(path).allowedOrigins(origins).allowedMethods("GET", "POST", "PUT", "DELETE");
        corsRegistry.addMapping(path2).allowedOrigins(origins).allowedMethods("GET", "POST", "PUT", "DELETE");
        corsRegistry.addMapping(path3).allowedOrigins(origins).allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}