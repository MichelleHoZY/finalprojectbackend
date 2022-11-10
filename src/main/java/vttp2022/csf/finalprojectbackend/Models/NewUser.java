package vttp2022.csf.finalprojectbackend.Models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.xml.sax.InputSource;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class NewUser {
    private String username;
    private String password;
    private String user_name;
    private Date startDate;
    private Date endDate;
    private boolean vegetarian;
    private String userType;
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public boolean isVegetarian() {
        return vegetarian;
    }
    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public static NewUser createUser(String json) throws IOException, ParseException {
        NewUser newUser = new NewUser();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject object = reader.readObject();
            newUser.setUser_name(object.getString("user_name"));
            newUser.setUsername(object.getString("username"));
            newUser.setPassword(new BCryptPasswordEncoder().encode(object.getString("password")));
            newUser.setVegetarian(object.getBoolean("vegetarian"));
            newUser.setUserType("ROLE_USER");
            String stringStartDate = object.getString("startDate");
            String stringEndDate = object.getString("endDate");
            
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringStartDate);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringEndDate);

            newUser.setStartDate(startDate);
            newUser.setEndDate(endDate);
        }
        return newUser;
    }
    
    @Override
    public String toString() {
        return "NewUser [username=" + username + ", password=" + password + ", user_name=" + user_name + ", startDate="
                + startDate + ", endDate=" + endDate + ", vegetarian=" + vegetarian + ", userType=" + userType + "]";
    }


    
}
