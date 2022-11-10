package vttp2022.csf.finalprojectbackend.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import vttp2022.csf.finalprojectbackend.Filters.JWTAuthFilter;
import vttp2022.csf.finalprojectbackend.Repositories.FoodFormRepository;
import vttp2022.csf.finalprojectbackend.Services.UserService;
import vttp2022.csf.finalprojectbackend.Filters.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // private final RsaKeyProperties rsaKeys;

    // public SecurityConfig(RsaKeyProperties rsaKeys) {
    //     this.rsaKeys = rsaKeys;
    // }

    // @Bean
    // public UserDetailsService users() {
    //     return new User.withUsername
    //     // return new InMemoryUserDetailsManager(
    //     //         User.withUsername("dvega")
    //     //                 .password("{noop}password")
    //     //                 .authorities("read")
    //     //                 .build()
    //     // );
    // }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    @Bean
    public AuthenticationManager authManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    } 

    // @Autowired
    // private DataSource dataSource;

    // @Resource
    // private UserDetailsService userDetailsService;

    // @Bean
    // public DaoAuthenticationProvider authProvider() {
    //     DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    //     authProvider.setUserDetailsService(userDetailsService);
    //     authProvider.setPasswordEncoder(passwordEncoder());

    //     return authProvider;
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

    // @Bean
    // JwtDecoder jwtDecoder() {
    //     return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    // }

    // @Bean
    // JwtEncoder jwtEncoder() {
    //     JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
    //     JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    //     return new NimbusJwtEncoder(jwks);
    // }

    JWTAuthFilter getJWTAuthFilter() {
        final JWTAuthFilter jwtAuthFilter = new JWTAuthFilter(userDetailsService, jwtUtil);
        return jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and()
                .csrf(csrf -> csrf.disable())
                .authenticationManager(authManager())
                .addFilterBefore(getJWTAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests( auth -> auth
                        .antMatchers("/api/**").permitAll()//hasAnyRole("ADMIN", "USER")
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/authorise/**").permitAll()
                        // .antMatchers("/createUser").permitAll()
                        // .antMatchers("/idAvailability").permitAll()
                        .anyRequest().authenticated()
                )
                // .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(Arrays.asList("PUT", "POST", "GET", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Origin"));
        src.registerCorsConfiguration("/**", config.applyPermitDefaultValues());
        return src;
    }

}
