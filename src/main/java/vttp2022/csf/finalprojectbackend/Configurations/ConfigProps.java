package vttp2022.csf.finalprojectbackend.Configurations;

import org.springframework.stereotype.Component;

@Component("configProps")
public class ConfigProps {
    public static final String AUTH_SECRET = "thisismysecret";
    public static final long AUTH_DURATION_VALID = 900000;
    
}
