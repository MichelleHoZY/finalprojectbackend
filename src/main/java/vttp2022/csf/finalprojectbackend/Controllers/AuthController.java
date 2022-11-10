package vttp2022.csf.finalprojectbackend.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vttp2022.csf.finalprojectbackend.Models.LoginRequest;
import vttp2022.csf.finalprojectbackend.Services.FoodFormServices;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import net.bytebuddy.asm.Advice.Origin;
import vttp2022.csf.finalprojectbackend.Models.ApiModel;
import vttp2022.csf.finalprojectbackend.Models.Attendance;
import vttp2022.csf.finalprojectbackend.Models.FoodItems;
import vttp2022.csf.finalprojectbackend.Models.MealReview;
import vttp2022.csf.finalprojectbackend.Models.Menu;
import vttp2022.csf.finalprojectbackend.Models.MenuChange;
import vttp2022.csf.finalprojectbackend.Models.MenuItem;
import vttp2022.csf.finalprojectbackend.Models.NewUser;
import vttp2022.csf.finalprojectbackend.Models.Ratings;
import vttp2022.csf.finalprojectbackend.Models.Reviews;
import vttp2022.csf.finalprojectbackend.Models.Users;

import static vttp2022.csf.finalprojectbackend.Filters.JwtUtil.generateToken;

@RestController
@RequestMapping(path="/authorise")
public class AuthController {

    @Autowired
    private FoodFormServices foodFormSvc;

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    // private final TokenService tokenService;
    // private final AuthenticationManager authenticationManager;

    // // @Autowired
    // // private FoodFormServices foodFormSvc;

    // public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
    //     this.tokenService = tokenService;
    //     this.authenticationManager = authenticationManager;
    // }

    // @Bean
    // public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
    //         throws Exception {
    //     return authenticationConfiguration.getAuthenticationManager();
    // }

    // // @PostMapping("/token")
    // public String token(String loginId, String password) {

    //     Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginId, password));
    //     return tokenService.generateToken(authentication); 

    //     // Authentication authentication = authenticationManager.authenticate(authRequest);
    //     // LOG.debug("Token requested for user: '{}'", authentication.getName());
    //     // String token = tokenService.generateToken(authentication);
    //     // LOG.debug("Token granted: {}", token);
    //     // return token;
    // }

    @GetMapping(path="/login")
    public ResponseEntity<String> retrievePrincipal(@AuthenticationPrincipal Users user) {

        LOG.info(user.toString());
        JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

        String token = generateToken(user);
        NewUser userInfo = foodFormSvc.getUserInfo(user.getId());

        // if (token.length() > 0) {
            jsonResponseObject.add("id", user.getId());
            jsonResponseObject.add("username", user.getUsername());
            jsonResponseObject.add("token", token);
            jsonResponseObject.add("name", userInfo.getUser_name());
            jsonResponseObject.add("vegetarian", userInfo.isVegetarian());
            jsonResponseObject.add("endDate", userInfo.getEndDate().toString());

            System.out.println(userInfo);

            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(jsonResponseObject.build().toString());
        // }

        // if (foodFormSvc.login(user.getUsername(), user.getPassword()) == 0) {
        //     jsonResponseObject.add("result", "User does not exist");
        //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponseObject.build().toString());
        // }
        // jsonResponseObject.add("result", "Wrong password");
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponseObject.build().toString());

    }


    @GetMapping(path="/idAvailability")
    public ResponseEntity<String> checkIdAvailability(@RequestParam String loginId) {
        System.out.println(">>> Email: " + loginId);
        JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

        if (!foodFormSvc.checkIdAvailability(loginId)) {
            jsonResponseObject.add("result", "Email is available");
            return ResponseEntity.ok().body(jsonResponseObject.build().toString());
        }

        jsonResponseObject.add("result", "Email is not available");
        return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    }

    @PostMapping(path="/createUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@RequestBody String json) throws IOException, ParseException {
        NewUser newUser = NewUser.createUser(json);

        JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

        if (foodFormSvc.createUser(newUser)) {
            jsonResponseObject.add("result", "User created");
            return ResponseEntity.status(HttpStatus.CREATED).body(jsonResponseObject.build().toString());
        }

        jsonResponseObject.add("result", "User not created");
        return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    }

    // @PostMapping(path="/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<String> login(@RequestBody String json) throws IOException {
    //     String loginId;
    //     String password;

    //     try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
    //         JsonReader reader = Json.createReader(is);
    //         JsonObject object = reader.readObject();
    //         loginId = object.getString("loginId");
    //         password = object.getString("password");
    //     }

    //     JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

    //     if (foodFormSvc.login(loginId, password) == 1) {
    //         // String token = token(loginId, password);
    //         // Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginId, password));
    //         // String token = tokenService.generateToken(authentication); 
    //         jsonResponseObject.add("result", "YES");
    //         return ResponseEntity.ok().body(jsonResponseObject.build().toString());
    //     } else if (foodFormSvc.login(loginId, password) == 2) {
    //         jsonResponseObject.add("result", "Wrong password entered");
    //         return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    //     }

    //     jsonResponseObject.add("result", "User does not exist");
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponseObject.build().toString()) ;
    // }

}
