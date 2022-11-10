package vttp2022.csf.finalprojectbackend.Controllers;

import java.security.Principal;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vttp2022.csf.finalprojectbackend.Models.JwtRequest;
import vttp2022.csf.finalprojectbackend.Models.JwtResponse;
// import vttp2022.csf.finalprojectbackend.Services.JwtUserDetailsService;

// @RestController
// @CrossOrigin
// @RequestMapping(path="/check")
// public class JwtAuthenticationController {

// 	@Autowired
// 	private AuthenticationManager authenticationManager;

// 	@Autowired
// 	private JwtTokenUtil jwtTokenUtil;

// 	@Autowired
// 	private JwtUserDetailsService userDetailsService;

// 	@PostMapping(path = "/authenticate")
// 	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

//         System.out.println(">>> Before authenticate"); 
// 		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

// 		final UserDetails userDetails = userDetailsService
// 				.loadUserByUsername(authenticationRequest.getUsername());

// 		final String token = jwtTokenUtil.generateToken(userDetails);

// 		return ResponseEntity.ok(new JwtResponse(token));
// 	}

// 	private void authenticate(String username, String password) throws Exception {
// 		try {
// 			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//             System.out.println("Username: " + username + " Password: " + password);
// 		} catch (DisabledException e) {
// 			throw new Exception("USER_DISABLED", e);
// 		} catch (BadCredentialsException e) {
// 			throw new Exception("INVALID_CREDENTIALS", e);
// 		}
// 	}
// }

@RestController
public class JwtAuthenticationController {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationController.class);

	@GetMapping
    public String home(Principal principal) {
        return "Hello, " + principal.getName();
    }
}
