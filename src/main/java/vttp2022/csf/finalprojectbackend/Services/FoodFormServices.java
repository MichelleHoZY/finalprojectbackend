package vttp2022.csf.finalprojectbackend.Services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2022.csf.finalprojectbackend.Models.ApiModel;
import vttp2022.csf.finalprojectbackend.Models.Attendance;
import vttp2022.csf.finalprojectbackend.Models.MealReview;
import vttp2022.csf.finalprojectbackend.Models.Menu;
import vttp2022.csf.finalprojectbackend.Models.MenuChange;
import vttp2022.csf.finalprojectbackend.Models.MenuItem;
import vttp2022.csf.finalprojectbackend.Models.NewUser;
import vttp2022.csf.finalprojectbackend.Models.Ratings;
import vttp2022.csf.finalprojectbackend.Models.Reviews;
import vttp2022.csf.finalprojectbackend.Repositories.FoodFormRepository;

@Service
public class FoodFormServices {

    @Value("${rapid.api.key}")
    private String rapidApiKey;

    @Autowired
    private FoodFormRepository foodFormRepo;

    @Autowired
    private AmazonS3 s3;

    public static final String baseURL = "https://edamam-recipe-search.p.rapidapi.com/search?";

    // food forms

    public int saveMenu(Menu menu) {
        return foodFormRepo.saveMenu(menu);
    }

    public Optional<Menu> getAllMenus() {
        if (!foodFormRepo.getAllMenus().getMenuList().isEmpty()) {
            return Optional.of(foodFormRepo.getAllMenus());
        }

        return Optional.empty();
    }

    public boolean updateMenu(MenuChange menuChange) {
        return foodFormRepo.updateMenu(menuChange);
    }

    public int deleteMenu(String mealId) {
        if (foodFormRepo.getMenu(mealId)) { // meal exists
            if (foodFormRepo.deleteMenu(mealId)) {
                return 0; // meal exists, successfully deleted
            } else {
                return 1; // meal exists, not deleted
            }
        }

        return 2; // meal does not exist
    }

    public JsonArray getMenuByDateStudent(List<String> dateArray, Boolean vegetarian) {
        JsonArrayBuilder jsonResponseArray = Json.createArrayBuilder();

        for (int i=0; i<dateArray.size(); i++) {
            List<MenuItem> menuList = foodFormRepo.getMenuByDateStudent(dateArray.get(i), vegetarian);

            if (!menuList.isEmpty()) {
                for (int t=0; t< menuList.size(); t++) {
                    JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
                    jsonResponseObject.add("Date", menuList.get(t).getMealDate().toString());
                    jsonResponseObject.add("Id", menuList.get(t).getMealId());
                    jsonResponseObject.add("Time", menuList.get(t).getMealTime());
                    jsonResponseObject.add("Vegetarian", menuList.get(t).getVegetarian());
                    jsonResponseObject.add("Main", menuList.get(t).getMain());
                    jsonResponseObject.add("Drinks", menuList.get(t).getDrinks());
                    jsonResponseObject.add("Dessert", menuList.get(t).getDessert());
                    jsonResponseObject.add("Remarks", menuList.get(t).getRemarks());
                    jsonResponseArray.add(jsonResponseObject);
                }
            }
        }
        return jsonResponseArray.build();
    }

    public JsonArray getMenuByDate(List<String> dateArray) {
        JsonArrayBuilder jsonResponseArray = Json.createArrayBuilder();

        for (int i=0; i<dateArray.size(); i++) {
            List<MenuItem> menuList = foodFormRepo.getMenuByDate(dateArray.get(i));

            if (!menuList.isEmpty()) {
                for (int t=0; t< menuList.size(); t++) {
                    JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
                    jsonResponseObject.add("Date", menuList.get(t).getMealDate().toString());
                    jsonResponseObject.add("Id", menuList.get(t).getMealId());
                    jsonResponseObject.add("Time", menuList.get(t).getMealTime());
                    jsonResponseObject.add("Vegetarian", menuList.get(t).getVegetarian());
                    jsonResponseObject.add("Main", menuList.get(t).getMain());
                    jsonResponseObject.add("Drinks", menuList.get(t).getDrinks());
                    jsonResponseObject.add("Dessert", menuList.get(t).getDessert());
                    jsonResponseObject.add("Remarks", menuList.get(t).getRemarks());
                    jsonResponseArray.add(jsonResponseObject);
                }
            }
        }
        return jsonResponseArray.build();
    }

    // food reviews

    public Optional<Reviews> getReviews(String mealId) {
        if (foodFormRepo.getReviews(mealId).getReviewList().size() > 0) {
            return Optional.of(foodFormRepo.getReviews(mealId));
        }

        return Optional.empty();
    }

    public List<String> getUserReviews(String userId) {
        return foodFormRepo.getUserReviews(userId);
    }

    public Optional<Menu> getUserMeals(String userId) {
        if (foodFormRepo.getUserMeals(userId).getMenuList().size() > 0) {
            return Optional.of(foodFormRepo.getUserMeals(userId));
        }

        return Optional.empty();
    }

    public boolean saveReview(MealReview mealReview, MultipartFile file) throws IOException {
        if (file != null) {
            String imageType = file.getContentType();
            String userId = mealReview.getUserId().toString();
            byte[] buff = new byte[0];
    
            try {
                buff = file.getBytes();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            String uuid = UUID.randomUUID().toString().substring(0,8);
            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentType(imageType);
            HashMap<String, String> userCustomerMetadata = new HashMap<>();
            userCustomerMetadata.put("uploader", userId);
            userCustomerMetadata.put("fileSize", String.valueOf(buff.length));
            metaData.setUserMetadata(userCustomerMetadata);
    
            try {
                PutObjectRequest putReq = new PutObjectRequest("michbucket", "images/%s".formatted(uuid), file.getInputStream(), metaData);
                putReq.setCannedAcl(CannedAccessControlList.PublicRead);
                s3.putObject(putReq);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    
            mealReview.setPic(uuid);
        }

        return foodFormRepo.saveReview(mealReview);
    }

    public MealReview getUserReview(String userId, String mealId) {
        return foodFormRepo.getUserMealReview(userId, mealId);
    }

    public boolean updateUserReview(MealReview mealReview, MultipartFile file) throws IOException {
        if (file != null) {
            String oldPic = foodFormRepo.getPicForUpdatedReview(mealReview.getPostId());
            System.out.println(">>> Old pic: " + oldPic);
            if (oldPic.isEmpty()) {
                DeleteObjectRequest deleteReq = new DeleteObjectRequest("michbucket", "images/%s".formatted(oldPic));
                s3.deleteObject(deleteReq);
            }
    
            String imageType = file.getContentType();
            String userId = mealReview.getUserId().toString();
            byte[] buff = new byte[0];
    
            try {
                buff = file.getBytes();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            String uuid = UUID.randomUUID().toString().substring(0,8);
            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentType(imageType);
            HashMap<String, String> userCustomerMetadata = new HashMap<>();
            userCustomerMetadata.put("uploader", userId);
            userCustomerMetadata.put("fileSize", String.valueOf(buff.length));
            metaData.setUserMetadata(userCustomerMetadata);
    
            try {
                PutObjectRequest putReq = new PutObjectRequest("michbucket", "images/%s".formatted(uuid), file.getInputStream(), metaData);
                putReq.setCannedAcl(CannedAccessControlList.PublicRead);
                s3.putObject(putReq);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    
            mealReview.setPic(uuid);
        }

        return foodFormRepo.updateReview(mealReview);
    }

    // food nutrition api

    public Optional<List<ApiModel>> getNutritionValue(String food) throws IOException {
        String url = UriComponentsBuilder
            .fromUriString(baseURL)
            .queryParam("q", food)
            .toUriString();

        String decodedUrl = UriUtils.decode(url, "utf-8");

        RequestEntity<Void> req = RequestEntity
            .get(decodedUrl)
            .header("X-RapidAPI-Host", "edamam-recipe-search.p.rapidapi.com")
            .header("X-RapidAPI-Key", rapidApiKey)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);

        List<ApiModel> apiModelList = new LinkedList<>();
    
        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject object = reader.readObject();
            System.out.println(object);
            // JsonObject hitsObject = object.getJsonObject("hits");
            JsonArray array = object.getJsonArray("hits");
            
            for (int i=0; i<10; i++) {
                ApiModel apiModel = new ApiModel();
                JsonObject innerObject = array.getJsonObject(i);
                System.out.println(innerObject);
                JsonObject recipeObject = innerObject.getJsonObject("recipe");
                apiModel.setName(recipeObject.getString("label"));
                apiModel.setImageUrl(recipeObject.getString("image"));
                apiModel.setRecipeUrl(recipeObject.getString("url"));

                JsonArray ingredientsArray = recipeObject.getJsonArray("ingredientLines");
                List<String> ingredientsList = new LinkedList<>();
                for (int x = 0; x < ingredientsArray.size(); x++) {
                    String ingredient = ingredientsArray.getString(x);
                    ingredientsList.add(ingredient);
                } 
                apiModel.setIngredients(ingredientsList);

                // JsonArray ingredientsObject = innerObject.getJsonArray("ingredients");
                String calories = recipeObject.get("calories").toString();
                apiModel.setCalories(calories);

                apiModelList.add(apiModel);
            }
        }

        // System.out.println(">>> Api model: " + apiModel.toString());

        if (apiModelList.size()==0) {
            return Optional.empty();
        }

        return Optional.of(apiModelList);
    }

    // food meal attendance

    public List<String> getPreselected(String json) throws IOException, ParseException{
        List<String> dates = new LinkedList<>();
        String userId;
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject object = reader.readObject();
            userId = object.getString("userId");
            JsonArray dateArray = object.getJsonArray("dates");

            for (int i=0; i<dateArray.size(); i++) {
                dates.add(dateArray.getString(i));
            }
        }
        System.out.println(">>> Dates: " + dates);
        return foodFormRepo.getPreselected(dates, userId);
    }

    public Integer updateAttendance(String json) throws IOException {
        System.out.println(">>> Update attendance json: " + json);
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject object = reader.readObject();
            String userId = object.getString("userId");
            JsonArray mealArray = object.getJsonArray("updated");
            List<String> mealsList = new LinkedList<>();

            for (int i=0; i<mealArray.size(); i++) {
                mealsList.add(mealArray.getString(i));
            }

            JsonArray preselectedArr = object.getJsonArray("preselected");
            List<String> preselected = new LinkedList<>();

            for (int i=0; i<preselectedArr.size(); i++) {
                preselected.add(preselectedArr.getString(i));
            }

            System.out.println(">>> Preselected list: " + preselected);

            int deletedRows = foodFormRepo.deleteAttendance(userId, preselected);
            if (deletedRows == preselected.size()) {
                return foodFormRepo.saveAttendance(userId, mealsList);
            }

            return 0;
        }
    }

    public int saveAttendance(String json) throws IOException {
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject object = reader.readObject();
            Integer userIdString = object.getInt("userId");
            String userId = userIdString.toString();
            JsonArray mealArray = object.getJsonArray("meals");
            List<String> mealsList = new LinkedList<>();

            for (int i=0; i<mealArray.size(); i++) {
                mealsList.add(mealArray.getString(i));
            }

            System.out.println(">>> Meals list: " + mealsList);

            return foodFormRepo.saveAttendance(userId, mealsList);
        }
    }

    // login/create user

    public boolean createUser(NewUser newUser) {
        return foodFormRepo.createUser(newUser); // still need to check for existing username
    }

    public boolean checkIdAvailability(String loginId) {
        return foodFormRepo.checkIdAvailability(loginId);
    }

    public int login(String loginId, String password) {
        if (foodFormRepo.userLogin(loginId, password)) {
            System.out.println(">>> Login successful");
            return 1; // successful login
        } 

        if (!foodFormRepo.checkIdAvailability(loginId)) {
            System.out.println(">>> No such user exists");
            return 0; // user does not exist
        }

        System.out.println(">>> Wrong password entered");
        return 2; // wrong password
        
    }

    public NewUser getUserInfo(Integer userId) {
        return foodFormRepo.getUserInfo(userId);
    }

    // statistics

    public JsonArray getMealAttendance(String json) throws IOException {
        System.out.println(json);
        List<String> dateList = new LinkedList<>();
        // Map<String, MenuItem> menuIdMap = new LinkedHashMap<>();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonArray array = reader.readArray();

            System.out.println(">>> Array size: " + array.size());
            for (int i=0; i<array.size(); i++) {
                dateList.add(array.getString(i));
                System.out.println(">>> String: " + array.getString(i));
            }
        }

        System.out.println(">>> Date list: " + dateList);

        JsonArrayBuilder jsonResponseArray = Json.createArrayBuilder();

        for (int i=0; i< dateList.size(); i++) { // for each date in the array 
            List<MenuItem> menuList = foodFormRepo.getMenuByDate(dateList.get(i)); // get all 3 meals in the day
            System.out.println(">>> Menu Id List: " + menuList); // print the list of all 3 meals

            for (int x=0; x<menuList.size(); x++) {
                String id = menuList.get(x).getMealId();
                int attending = foodFormRepo.getMealAttendance(id);
                menuList.get(x).setAttendance(attending);
                // menuIdMap.put(id, menuIdList.get(x));
            }

            for (int t=0; t<menuList.size(); t++) {
                JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
                jsonResponseObject.add("Date", menuList.get(t).getMealDate().toString());
                jsonResponseObject.add("Id", menuList.get(t).getMealId());
                jsonResponseObject.add("Time", menuList.get(t).getMealTime());

                if (menuList.get(t).getMealTime().equals("B")) {
                    jsonResponseObject.add("Time", "Breakfast");
                } else if (menuList.get(t).getMealTime().equals("L")) {
                    jsonResponseObject.add("Time", "Lunch");
                } else {
                    jsonResponseObject.add("Time", "Tea Break");
                }

                jsonResponseObject.add("Vegetarian", menuList.get(t).getVegetarian());
                jsonResponseObject.add("Main", menuList.get(t).getMain());
                jsonResponseObject.add("Drinks", menuList.get(t).getDrinks());
                jsonResponseObject.add("Dessert", menuList.get(t).getDessert());
                jsonResponseObject.add("Remarks", menuList.get(t).getRemarks());
                jsonResponseObject.add("Attendance", menuList.get(t).getAttendance());
                jsonResponseArray.add(jsonResponseObject);
            }

        }


        return jsonResponseArray.build();

    }

    public List<Ratings> getStats(List<String> datesList) {
        // Date date= new Date();
        // Calendar cal = Calendar.getInstance();
        // cal.setTime(date);
        // int month = cal.get(Calendar.MONTH);
        // // System.out.println(">>> Month: " + month);

        // List<String> thisMonthDates = getDatesForThisMonthStats(cal, month);
        // List<String> lastMonthDates = getDatesForLastMonthStats(month-1);
        // System.out.println(thisMonthDates);
        // System.out.println(lastMonthDates);

        // List<MenuItem> lastMonthMenu = new LinkedList<>();
        List<MenuItem> thisMonthMenu = new LinkedList<>();

        // for (int i=0; i<lastMonthDates.size(); i++) {
        //     List<MenuItem> lastMonth = foodFormRepo.getMenuByDate(lastMonthDates.get(i));
        //     for (MenuItem item: lastMonth) {
        //         lastMonthMenu.add(item);
        //     }
        // }

        for (int i=0; i<datesList.size(); i++) {
            List<MenuItem> thisMonth = foodFormRepo.getMenuByDate(datesList.get(i));
            for (MenuItem item: thisMonth) {
                thisMonthMenu.add(item);
            }
        }

        // System.out.println(">>> Last menu: " + lastMonthMenu);
        System.out.println(">>> This menu: " + thisMonthMenu);

        List<Ratings> thisMonthRating = new LinkedList<>();

        for (int i=0; i<thisMonthMenu.size(); i++) {
            List<Integer> thisMonthRatings = new LinkedList<>();
            Integer thisMonthNoOfRatings = 0;
            thisMonthRatings = foodFormRepo.getMealRatings(thisMonthMenu.get(i).getMealId(), thisMonthMenu.get(i).getMealTime());
            thisMonthNoOfRatings = foodFormRepo.getNoOfMealRatings(thisMonthMenu.get(i).getMealId(), thisMonthMenu.get(i).getMealTime());

            thisMonthMenu.get(i).setAttendance(foodFormRepo.getMealAttendance(thisMonthMenu.get(i).getMealId()));
            
            Ratings rating = new Ratings();

            if (thisMonthNoOfRatings == 0) {
                rating.setAverageRating(0.0);
            } else {
                Integer sumOfRatings = 0;
                for (int x=0; x<thisMonthRatings.size(); x++) {
                    sumOfRatings = sumOfRatings + thisMonthRatings.get(x);
                }
    
                Double averageRatings = (double) (sumOfRatings/thisMonthNoOfRatings);
                rating.setAverageRating(averageRatings);
            }
            rating.setMealId(thisMonthMenu.get(i).getMealId());
            rating.setMealTime(thisMonthMenu.get(i).getMealTime());
            rating.setMain(thisMonthMenu.get(i).getMain());
            rating.setDrinks(thisMonthMenu.get(i).getDrinks());
            rating.setDessert(thisMonthMenu.get(i).getDessert());
            rating.setDate(thisMonthMenu.get(i).getMealDate().toString());
            rating.setTotalPax(thisMonthMenu.get(i).getAttendance());

            thisMonthRating.add(rating);
        }

        return thisMonthRating;

    }

    public List<String> getDatesForThisMonthStats(int month) {
        List<String> dates = new LinkedList<>();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        cal.set(year, month, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(">>> Max day: " + maxDay);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        for (int i=0; i<maxDay; i++) {
            cal.set(Calendar.DAY_OF_MONTH, i + 1);
            dates.add(df.format(cal.getTime()));
        }

        System.out.println(">>> This month dates:" + dates);

        return dates;
    }

}
