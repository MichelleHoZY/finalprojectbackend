package vttp2022.csf.finalprojectbackend.Filters;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTAuthFilter.class);
    private final RequestMatcher ignoredPaths = new AntPathRequestMatcher("/authorise/**");

    private JwtUtil jwtUtil;
    private UserDetailsService udSvc;

    public JWTAuthFilter(UserDetailsService udSvc, JwtUtil jwtUtil) {
        this.udSvc = udSvc;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        final String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (this.ignoredPaths.matches(req)) {
            filterChain.doFilter(req, resp);
            return;
        }
        if (header.equals("") || !header.startsWith("Bearer ")) {
            filterChain.doFilter(req, resp);
            return;
        }

        final String token = header.split(" ")[1].trim();
        logger.debug(token);

        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(req, resp);
            return;
        }

        UserDetails user = udSvc.loadUserByUsername(jwtUtil.getUsername(token));

        AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            user, null, user == null ? Collections.emptyList() : user.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(req, resp);
    }
    
}
