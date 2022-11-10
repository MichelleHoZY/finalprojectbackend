package vttp2022.csf.finalprojectbackend.Controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpsRedirectSpec;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.Response;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2022.csf.finalprojectbackend.Models.ApiModel;
import vttp2022.csf.finalprojectbackend.Models.MealReview;
import vttp2022.csf.finalprojectbackend.Models.Menu;
import vttp2022.csf.finalprojectbackend.Models.NewUser;
import vttp2022.csf.finalprojectbackend.Models.Ratings;
import vttp2022.csf.finalprojectbackend.Models.Reviews;
import vttp2022.csf.finalprojectbackend.Services.FoodFormServices;

@RestController
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class FoodFormRestController {

    @Autowired
    private FoodFormServices foodFormSvc;

    // // food forms

    // @PostMapping(path="/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<String> saveFoodForm(@RequestBody String json) throws IOException {
    //     FoodItems foodItem = FoodItems.createFoodItems(json);

    //     JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
    //     jsonResponseObject.add("userId", foodItem.getUserId());

    //     return ResponseEntity.ok(jsonResponseObject.build().toString());
    // }

    // @PostMapping(path="/setMenu", produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<String> saveMenu(@RequestBody String json) throws IOException, ParseException {
    //     Menu menu = Menu.createMenu(json);
    //     JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

    //     System.out.println(">>> Controller create menu: " + menu);

    //     int menuItemsAdded = foodFormSvc.saveMenu(menu);
    //     if (menuItemsAdded > 0) {
    //         jsonResponseObject.add("menuList", menuItemsAdded);
    //         return ResponseEntity.ok(jsonResponseObject.build().toString());
    //     }
    //     jsonResponseObject.add("menuList", "not created");

    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponseObject.build().toString());
    // }

    // @GetMapping(path="/getAllMenus")
    // public ResponseEntity<String> getAllMenus() {
    //     Optional<Menu> optMenu = foodFormSvc.getAllMenus();

    //     if (optMenu.isEmpty()) {
    //         return ResponseEntity.notFound().build();
    //     }

    //     Menu menu = optMenu.get();

    //     JsonArrayBuilder jsonResponseArray = Json.createArrayBuilder();

    //     for (int i=0; i<menu.getMenuList().size(); i++) {
    //         JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
    //         jsonResponseObject.add("Id", menu.getMenuList().get(i).getMealId());
    //         jsonResponseObject.add("Date", menu.getMenuList().get(i).getMealDate().toString());

    //         if (menu.getMenuList().get(i).getMealTime().equals("B")) {
    //             jsonResponseObject.add("Time", "Breakfast");
    //         } else if (menu.getMenuList().get(i).getMealTime().equals("L")) {
    //             jsonResponseObject.add("Time", "Lunch");
    //         } else {
    //             jsonResponseObject.add("Time", "Tea Break");
    //         }

    //         if (menu.getMenuList().get(i).getVegetarian() == true) {
    //             jsonResponseObject.add("Vegetarian", "Yes");
    //         } else {
    //             jsonResponseObject.add("Vegetarian", "No");
    //         }
    //         jsonResponseObject.add("Main", menu.getMenuList().get(i).getMain());
    //         jsonResponseObject.add("Drinks", menu.getMenuList().get(i).getDrinks());
    //         jsonResponseObject.add("Dessert", menu.getMenuList().get(i).getDessert());
    //         jsonResponseObject.add("Remarks", menu.getMenuList().get(i).getRemarks());

    //         jsonResponseArray.add(jsonResponseObject);
    //     }

    //     return ResponseEntity.ok(jsonResponseArray.build().toString());
    // }

    // @PutMapping(path="/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<String> changeMenu(@RequestBody String json) throws IOException {
    //     MenuChange menuChange = MenuChange.createMenuChange(json);
    //     JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

    //     if (foodFormSvc.updateMenu(menuChange)) {
    //         jsonResponseObject.add("result", "Successfully updated");
    //         return ResponseEntity.ok().body(jsonResponseObject.build().toString());
    //     }

    //     jsonResponseObject.add("result", "An error occurred while updating");
    //     return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    // }

    // @DeleteMapping(path="/delete")
    // public ResponseEntity<String> deleteMenu(@RequestParam String mealId) {
    //     JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
        
    //     if (foodFormSvc.deleteMenu(mealId) == 0) {
    //         jsonResponseObject.add("result", "Successfully deleted");
    //         return ResponseEntity.ok().body(jsonResponseObject.build().toString());
    //     } else if (foodFormSvc.deleteMenu(mealId) == 1) {
    //         jsonResponseObject.add("result", "An error occurred while deleting");
    //         return ResponseEntity.ok().body(jsonResponseObject.build().toString());
    //     }
        
    //     jsonResponseObject.add("result", "Meal does not exist");
    //     return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    // }

    @PostMapping(path="/saveAttendance")
    public ResponseEntity<Integer> saveAttendance(@RequestBody String json) throws IOException {
        System.out.println(json);

        int savedRows = foodFormSvc.saveAttendance(json);
        
        return ResponseEntity.ok().body(savedRows);
    }

    @PostMapping(path="/updateAttendance")
    public ResponseEntity<Integer> updateAttendance(@RequestBody String json) throws IOException {
        int updated = foodFormSvc.updateAttendance(json);

        if (updated == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(updated);
        }

        return ResponseEntity.ok().body(updated);
    }

    @PostMapping(path="/preselected")
    public ResponseEntity<String> getPreselected(@RequestBody String json) throws IOException, ParseException {
        System.out.println(">>> Preselected json: " + json);
        List<String> dateList = foodFormSvc.getPreselected(json);
        JsonArrayBuilder jsonResponseArray = Json.createArrayBuilder();

        if (dateList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponseArray.build().toString());
        }

        for (int i=0; i<dateList.size(); i++) {
            jsonResponseArray.add(dateList.get(i));
        }

        return ResponseEntity.ok().body(jsonResponseArray.build().toString());
    }

    // food reviews

    // @GetMapping(path="/reviews/{mealId}")
    // public ResponseEntity<String> getReviews(@PathVariable String mealId) {
    //     Optional<Reviews> optReviews = foodFormSvc.getReviews(mealId);
    //     JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
    //     JsonArrayBuilder jsonArray = Json.createArrayBuilder();

    //     if (optReviews.isEmpty()) {
    //         jsonResponseObject.add("result", "No reviews were found");
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponseObject.build().toString());
    //     }

    //     Reviews reviews = optReviews.get();
        
    //     for (int i=0; i<reviews.getReviewList().size(); i++) {
    //         JsonObjectBuilder innerObject = Json.createObjectBuilder();
    //         innerObject.add("PostId", reviews.getReviewList().get(i).getPostId());
    //         innerObject.add("User", reviews.getReviewList().get(i).getUserId());
    //         innerObject.add("Date", reviews.getReviewList().get(i).getPostDate().toString());
    //         innerObject.add("Caption", reviews.getReviewList().get(i).getCaption());
    //         innerObject.add("Rating", reviews.getReviewList().get(i).getRating());
    //         innerObject.add("Pic", reviews.getReviewList().get(i).getPic());

    //         jsonArray.add(innerObject);
    //     }
    //     // jsonResponseObject.add(jsonArray);

    //     return ResponseEntity.ok(jsonArray.build().toString());
    // }

    @PostMapping(path="/saveReview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveReview(@RequestParam(name="file", required = false) MultipartFile file, @RequestParam(name="caption", required = false) String caption,
        @RequestParam Integer rating, @RequestParam String mealId, @RequestParam Integer userId, @RequestParam String date) throws ParseException, IOException {
            System.out.println(file + " " + caption + " " + rating  + " " + mealId  + " " + userId  + " " + date);
            MealReview mealReview = MealReview.createReview(file, caption, rating, mealId, userId, date);
            JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

            if (foodFormSvc.saveReview(mealReview, file)) {
                jsonResponseObject.add("result", mealId);
                return ResponseEntity.ok(jsonResponseObject.build().toString());
            }

        jsonResponseObject.add("result", "");
        return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    }

    @GetMapping(path="/userReviews")
    public ResponseEntity<String> getUserReviews(@RequestParam String userId) {
        List<String> reviewsList = foodFormSvc.getUserReviews(userId);
        JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();

        if (reviewsList.size() > 0) {
            for (int i=0; i<reviewsList.size(); i++) {
                // innerObject.add("PostId", reviews.getReviewList().get(i).getPostId());
                jsonArray.add(reviewsList.get(i));
                // innerObject.add("Date", reviews.getReviewList().get(i).getPostDate().toString());
                // innerObject.add("Caption", reviews.getReviewList().get(i).getCaption());
                // innerObject.add("Rating", reviews.getReviewList().get(i).getRating());
                // innerObject.add("Pic", reviews.getReviewList().get(i).getPic());
            }
            jsonResponseObject.add("mealId", jsonArray);
    
            return ResponseEntity.ok(jsonResponseObject.build().toString());
        }

        jsonResponseObject.add("result", "No reviews were found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponseObject.build().toString());
    }

    @GetMapping(path="/userMeals")
    public ResponseEntity<String> getUserMeals(@RequestParam String userId) {
        Optional<Menu> optMenu = foodFormSvc.getUserMeals(userId);
        JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();

        if (optMenu.isEmpty()) {
            jsonResponseObject.add("result", "User has not eaten any meals");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponseObject.build().toString());
        }

        Menu menu = optMenu.get();
        
        for (int i=0; i<menu.getMenuList().size(); i++) {
            JsonObjectBuilder innerObject = Json.createObjectBuilder();
            innerObject.add("Date", menu.getMenuList().get(i).getMealDate().toString());

            if (menu.getMenuList().get(i).getMealTime().equals("B")) {
                innerObject.add("Time", "Breakfast");
            } else if (menu.getMenuList().get(i).getMealTime().equals("L")) {
                innerObject.add("Time", "Lunch");
            } else {
                innerObject.add("Time", "Tea Break");
            }

            // innerObject.add("Time", menu.getMenuList().get(i).getMealTime().toString());
            innerObject.add("Id", menu.getMenuList().get(i).getMealId().toString());
            innerObject.add("Main", menu.getMenuList().get(i).getMain().toString());
            innerObject.add("Drinks", menu.getMenuList().get(i).getDrinks().toString());
            innerObject.add("Dessert", menu.getMenuList().get(i).getDessert().toString());
            innerObject.add("Remarks", menu.getMenuList().get(i).getRemarks().toString());

            jsonArray.add(innerObject);
        }
        // jsonResponseObject.add(jsonArray);

        return ResponseEntity.ok(jsonArray.build().toString());
    }

    @GetMapping(path="/userMealReview")
    public ResponseEntity<String> getUserMealReview(@RequestParam String userId, @RequestParam String mealId) {
        MealReview review = foodFormSvc.getUserReview(userId, mealId);
        System.out.println(">>> Controller: " + review);
        JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
        if (!review.getPostId().isBlank()) {
            jsonResponseObject.add("postId", review.getPostId());
            jsonResponseObject.add("rating", review.getRating());
            jsonResponseObject.add("caption", review.getCaption());
            jsonResponseObject.add("pic", "https://michbucket.sgp1.digitaloceanspaces.com/images/%s".formatted(review.getPic()));

            return ResponseEntity.ok().body(jsonResponseObject.build().toString());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponseObject.build().toString());
    }

    @PutMapping(path="/updateReview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateReview(@RequestParam(name="file", required = false) MultipartFile file, @RequestParam(name="caption", required = false) String caption,
        @RequestParam Integer rating, @RequestParam String mealId, @RequestParam Integer userId, @RequestParam String date, @RequestParam String postId) throws ParseException, IOException {
            System.out.println(file + " " + caption + " " + rating  + " " + mealId  + " " + userId  + " " + date + " " + postId);
            MealReview mealReview = MealReview.updateReview(file, caption, rating, mealId, userId, date, postId);
            JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

            if (foodFormSvc.updateUserReview(mealReview, file)) {
                jsonResponseObject.add("result", mealReview.getPostId());
                return ResponseEntity.ok(jsonResponseObject.build().toString());
            }

        jsonResponseObject.add("result", "");
        return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    }

    // external api

    @GetMapping(path="/nutrition")
    public ResponseEntity<String> getNutritionInfo(@RequestParam String food) throws IOException {
        Optional<List<ApiModel>> optList = foodFormSvc.getNutritionValue(food);

        if (optList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ApiModel> list = optList.get();
        JsonArrayBuilder jsonResponseArray = Json.createArrayBuilder();
        
        for (int i=0; i<list.size(); i++) {
            JsonObjectBuilder innerResponseObject = Json.createObjectBuilder();
            innerResponseObject.add("Name", list.get(i).getName());
            innerResponseObject.add("imageUrl", list.get(i).getImageUrl());
            innerResponseObject.add("recipeUrl", list.get(i).getRecipeUrl());

            JsonArrayBuilder innerResponseArray = Json.createArrayBuilder();
            for (int x=0; x<list.get(i).getIngredients().size(); x++) {
                innerResponseArray.add(list.get(i).getIngredients().get(x));
            }
            innerResponseObject.add("ingredients", innerResponseArray);
            innerResponseObject.add("calories", list.get(i).getCalories());
            
            jsonResponseArray.add(innerResponseObject);
        }

        return ResponseEntity.ok(jsonResponseArray.build().toString());

    }

    // food menu date

    @PostMapping(path="/menuDatesStudent", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMenuBetweenDatesStudent(@RequestBody String json) throws IOException {
        List<String> dateArray = new LinkedList<>();
        Boolean vegetarian;

        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject object = reader.readObject();
            vegetarian = object.getBoolean("vegetarian");
            JsonArray array = object.getJsonArray("dates");

            for (int i=0; i<array.size(); i++) {
                dateArray.add(array.getString(i));
            }
        }

        if (dateArray.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        JsonArray jsonResponseArray = foodFormSvc.getMenuByDateStudent(dateArray, vegetarian);
        return ResponseEntity.ok().body(jsonResponseArray.toString());
    }

    @PostMapping(path="/menuDates", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMenuBetweenDates(@RequestBody String json) throws IOException {
        List<String> dateArray = new LinkedList<>();

        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject object = reader.readObject();
            JsonArray array = object.getJsonArray("dates");

            for (int i=0; i<array.size(); i++) {
                dateArray.add(array.getString(i));
            }
        }

        if (dateArray.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        JsonArray jsonResponseArray = foodFormSvc.getMenuByDate(dateArray);
        return ResponseEntity.ok().body(jsonResponseArray.toString());
    }

    // login/create user

    // @GetMapping(path="/idAvailability")
    // public ResponseEntity<String> checkIdAvailability(@RequestParam String loginId) {
    //     JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

    //     if (!foodFormSvc.checkIdAvailability(loginId)) {
    //         jsonResponseObject.add("result", "Login ID available");
    //         return ResponseEntity.ok().body(jsonResponseObject.build().toString());
    //     }

    //     jsonResponseObject.add("result", "Login ID not available");
    //     return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    // }

    // @PostMapping(path="/createUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<String> createUser(@RequestBody String json) throws IOException, ParseException {
    //     NewUser newUser = NewUser.createUser(json);

    //     JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

    //     if (foodFormSvc.createUser(newUser)) {
    //         jsonResponseObject.add("result", "User created");
    //         return ResponseEntity.status(HttpStatus.CREATED).body(jsonResponseObject.build().toString());
    //     }

    //     jsonResponseObject.add("result", "User not created");
    //     return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    // }

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
    //         String token = authController.token(loginId, password);
    //         jsonResponseObject.add("result", token.toString());
    //         return ResponseEntity.ok().body(jsonResponseObject.build().toString());
    //     } else if (foodFormSvc.login(loginId, password) == 2) {
    //         jsonResponseObject.add("result", "Wrong password entered");
    //         return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    //     }

    //     jsonResponseObject.add("result", "User does not exist");
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponseObject.build().toString()) ;
    // }

    // statistics

    @PostMapping(path="/thisWeek") 
    public ResponseEntity<String> getMealAttendance(@RequestBody String json) throws IOException {
        System.out.println(">>> This week dates: " + json);

        JsonArray jsonResponseArray = foodFormSvc.getMealAttendance(json);
        return ResponseEntity.ok().body(jsonResponseArray.toString());

        // Map<String, MenuItem> resultMap = new LinkedHashMap<>();
        // resultMap = foodFormSvc.getMealAttendance(json);
        // JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

        // if (!resultMap.isEmpty()) {
        //     JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        //     System.out.println(resultMap);
        //     resultMap.entrySet().stream()
        //         .map(e -> {
        //             JsonObjectBuilder attendance = Json.createObjectBuilder();
        //             JsonObjectBuilder innerObject = Json.createObjectBuilder();
        //             innerObject.add("Date", e.getValue().getMealDate().toString());
        //             innerObject.add("Id", e.getValue().getMealId());
        //             innerObject.add("Time", e.getValue().getMealTime());
        //             innerObject.add("Vegetarian", e.getValue().getVegetarian());
        //             innerObject.add("Main", e.getValue().getMain());
        //             innerObject.add("Drinks", e.getValue().getDrinks());
        //             innerObject.add("Dessert", e.getValue().getDessert());
        //             innerObject.add("Remarks", e.getValue().getRemarks());
        //             innerObject.add("Attendance", e.getValue().getAttendance());
        //             attendance.add(e.getKey(), innerObject);
        //             System.out.println(e.getKey());
        //             return attendance;
        //         })
        //         .forEach(jsonArray::add);
        //         jsonResponseObject.add("attendance", jsonArray);



        //         return ResponseEntity.ok(jsonResponseObject.build().toString());
        // }
        
        // jsonResponseObject.add("attendance", 0);
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponseObject.build().toString());
    }

    // @GetMapping(path="/stats")
    // public ResponseEntity<String> getStats(@RequestParam Integer month) {

    //     Date date= new Date();
    //     Calendar cal = Calendar.getInstance();
    //     cal.setTime(date);
    //     // int month = cal.get(Calendar.MONTH);
    //     System.out.println(">>> Month: " + month);

    //     List<String> thisMonthDates = foodFormSvc.getDatesForThisMonthStats(month);
    //     // List<String> lastMonthDates = foodFormSvc.getDatesForLastMonthStats(month-1);

    //     List<Ratings> thisMonthRatings = foodFormSvc.getStats(thisMonthDates);
    //     // List<Ratings> lastMonthRatings = foodFormSvc.getStats(lastMonthDates);

    //     System.out.println(">>> This month ratings: " + thisMonthRatings);
    //     // System.out.println(">>> Last month ratings: " + lastMonthRatings);

    //     JsonArrayBuilder jsonResponseArray = Json.createArrayBuilder();
    //     JsonObjectBuilder breakfastObject = Json.createObjectBuilder();
    //     JsonObjectBuilder lunchObject = Json.createObjectBuilder();
    //     JsonObjectBuilder teaObject = Json.createObjectBuilder();

    //     JsonArrayBuilder breakfastArray = Json.createArrayBuilder();
    //     JsonArrayBuilder lunchArray = Json.createArrayBuilder();
    //     JsonArrayBuilder teaArray = Json.createArrayBuilder();

    //     breakfastObject.add("name", "Breakfast");
    //     lunchObject.add("name", "Lunch");
    //     teaObject.add("name", "Tea Break");

    //     for (int i=0; i<thisMonthRatings.size(); i++) {
    //         if (thisMonthRatings.get(i).getMealTime().equals("B")) {
    //             JsonObjectBuilder innerObject = Json.createObjectBuilder();
    //             innerObject.add("name", thisMonthRatings.get(i).getDate());
    //             innerObject.add("value", thisMonthRatings.get(i).getAverageRating());
    //             innerObject.add("Meal", thisMonthRatings.get(i).getMain());
    //             innerObject.add("Id", thisMonthRatings.get(i).getMealId());
    //             breakfastArray.add(innerObject);
    //         } else if (thisMonthRatings.get(i).getMealTime().equals("L")) {
    //             JsonObjectBuilder innerObject = Json.createObjectBuilder();
    //             innerObject.add("name", thisMonthRatings.get(i).getDate());
    //             innerObject.add("value", thisMonthRatings.get(i).getAverageRating());
    //             innerObject.add("Meal", thisMonthRatings.get(i).getMain());
    //             innerObject.add("Id", thisMonthRatings.get(i).getMealId());
    //             lunchArray.add(innerObject);
    //         } else {
    //             JsonObjectBuilder innerObject = Json.createObjectBuilder();
    //             innerObject.add("name", thisMonthRatings.get(i).getDate());
    //             innerObject.add("value", thisMonthRatings.get(i).getAverageRating());
    //             innerObject.add("Meal", thisMonthRatings.get(i).getMain());
    //             innerObject.add("Id", thisMonthRatings.get(i).getMealId());
    //             teaArray.add(innerObject);
    //         }
    //     }

    //     breakfastObject.add("series", breakfastArray);
    //     lunchObject.add("series", lunchArray);
    //     teaObject.add("series", teaArray);

    //     jsonResponseArray.add(breakfastObject);
    //     jsonResponseArray.add(lunchObject);
    //     jsonResponseArray.add(teaObject);

    //     return ResponseEntity.ok(jsonResponseArray.build().toString());
    // }

    // @GetMapping(path="/pax")
    // public ResponseEntity<String> getPax(@RequestParam Integer month) {

    //     Date date= new Date();
    //     Calendar cal = Calendar.getInstance();
    //     cal.setTime(date);
    //     // int month = cal.get(Calendar.MONTH);
    //     System.out.println(">>> Month: " + month);

    //     List<String> thisMonthDates = foodFormSvc.getDatesForThisMonthStats(month);
    //     // List<String> lastMonthDates = foodFormSvc.getDatesForLastMonthStats(month-1);

    //     List<Ratings> thisMonthRatings = foodFormSvc.getStats(thisMonthDates);
    //     // List<Ratings> lastMonthRatings = foodFormSvc.getStats(lastMonthDates);

    //     System.out.println(">>> This month ratings: " + thisMonthRatings);
    //     // System.out.println(">>> Last month ratings: " + lastMonthRatings);

    //     JsonArrayBuilder jsonResponseArray = Json.createArrayBuilder();
    //     JsonObjectBuilder breakfastObject = Json.createObjectBuilder();
    //     JsonObjectBuilder lunchObject = Json.createObjectBuilder();
    //     JsonObjectBuilder teaObject = Json.createObjectBuilder();

    //     JsonArrayBuilder breakfastArray = Json.createArrayBuilder();
    //     JsonArrayBuilder lunchArray = Json.createArrayBuilder();
    //     JsonArrayBuilder teaArray = Json.createArrayBuilder();

    //     breakfastObject.add("name", "Breakfast");
    //     lunchObject.add("name", "Lunch");
    //     teaObject.add("name", "Tea Break");

    //     for (int i=0; i<thisMonthRatings.size(); i++) {
    //         if (thisMonthRatings.get(i).getMealTime().equals("B")) {
    //             JsonObjectBuilder innerObject = Json.createObjectBuilder();
    //             innerObject.add("name", thisMonthRatings.get(i).getDate());
    //             innerObject.add("value", thisMonthRatings.get(i).getTotalPax());
    //             innerObject.add("Meal", thisMonthRatings.get(i).getMain());
    //             innerObject.add("Id", thisMonthRatings.get(i).getMealId());
    //             breakfastArray.add(innerObject);
    //         } else if (thisMonthRatings.get(i).getMealTime().equals("L")) {
    //             JsonObjectBuilder innerObject = Json.createObjectBuilder();
    //             innerObject.add("name", thisMonthRatings.get(i).getDate());
    //             innerObject.add("value", thisMonthRatings.get(i).getTotalPax());
    //             innerObject.add("Meal", thisMonthRatings.get(i).getMain());
    //             innerObject.add("Id", thisMonthRatings.get(i).getMealId());
    //             lunchArray.add(innerObject);
    //         } else {
    //             JsonObjectBuilder innerObject = Json.createObjectBuilder();
    //             innerObject.add("name", thisMonthRatings.get(i).getDate());
    //             innerObject.add("value", thisMonthRatings.get(i).getTotalPax());
    //             innerObject.add("Meal", thisMonthRatings.get(i).getMain());
    //             innerObject.add("Id", thisMonthRatings.get(i).getMealId());
    //             teaArray.add(innerObject);
    //         }
    //     }

    //     breakfastObject.add("series", breakfastArray);
    //     lunchObject.add("series", lunchArray);
    //     teaObject.add("series", teaArray);

    //     jsonResponseArray.add(breakfastObject);
    //     jsonResponseArray.add(lunchObject);
    //     jsonResponseArray.add(teaObject);

    //     return ResponseEntity.ok(jsonResponseArray.build().toString());
    // }
    
}
