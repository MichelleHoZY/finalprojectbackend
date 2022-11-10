package vttp2022.csf.finalprojectbackend.Filters;

import org.jboss.logging.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import static vttp2022.csf.finalprojectbackend.Configurations.ConfigProps.AUTH_SECRET;

import java.util.Date;
import java.util.Map;

import static vttp2022.csf.finalprojectbackend.Configurations.ConfigProps.AUTH_DURATION_VALID;

@Component("jwtUtil")
public class JwtUtil {
    private static final Logger logger = Logger.getLogger(JwtUtil.class);

    private static final ObjectMapper mapper = new ObjectMapper();
    private JWTVerifier verifier = JWT.require(Algorithm.HMAC512(AUTH_SECRET)).build();

    public static String generateToken(UserDetails user) {
        return "Bearer " + JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + AUTH_DURATION_VALID))
            .sign(Algorithm.HMAC512(AUTH_SECRET.getBytes()));
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJwt = verifier.verify(token);
            if (null != decodedJwt.getExpiresAt() && new Date().before(decodedJwt.getExpiresAt())) {
                logger.debug("TOKEN NOT EXPIRED");
                return true;
            }
        } catch (JWTVerificationException ex) {
            throw new RuntimeException("Error: ", ex);
        }

        return false;
    }

    public String getUsername(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
        return claims.get("sub").asString();
    }
    
}
