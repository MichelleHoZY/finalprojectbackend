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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2022.csf.finalprojectbackend.Models.ApiModel;
import vttp2022.csf.finalprojectbackend.Models.FoodItems;
import vttp2022.csf.finalprojectbackend.Models.MealReview;
import vttp2022.csf.finalprojectbackend.Models.Menu;
import vttp2022.csf.finalprojectbackend.Models.MenuChange;
import vttp2022.csf.finalprojectbackend.Models.NewUser;
import vttp2022.csf.finalprojectbackend.Models.Ratings;
import vttp2022.csf.finalprojectbackend.Models.Reviews;
import vttp2022.csf.finalprojectbackend.Services.FoodFormServices;

@RestController
@RequestMapping(path="admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    @Autowired
    private FoodFormServices foodFormSvc;

    // food forms

    @PostMapping(path="/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveFoodForm(@RequestBody String json) throws IOException {
        FoodItems foodItem = FoodItems.createFoodItems(json);

        JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
        jsonResponseObject.add("userId", foodItem.getUserId());

        return ResponseEntity.ok(jsonResponseObject.build().toString());
    }

    @PostMapping(path="/setMenu", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveMenu(@RequestBody String json) throws IOException, ParseException {
        Menu menu = Menu.createMenu(json);
        JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

        System.out.println(">>> Controller create menu: " + menu);

        int menuItemsAdded = foodFormSvc.saveMenu(menu);
        if (menuItemsAdded > 0) {
            jsonResponseObject.add("menuList", menuItemsAdded);
            return ResponseEntity.ok(jsonResponseObject.build().toString());
        }
        jsonResponseObject.add("menuList", "not created");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponseObject.build().toString());
    }

    @GetMapping(path="/getAllMenus")
    public ResponseEntity<String> getAllMenus() {
        Optional<Menu> optMenu = foodFormSvc.getAllMenus();

        if (optMenu.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Menu menu = optMenu.get();

        JsonArrayBuilder jsonResponseArray = Json.createArrayBuilder();

        for (int i=0; i<menu.getMenuList().size(); i++) {
            JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
            jsonResponseObject.add("Id", menu.getMenuList().get(i).getMealId());
            jsonResponseObject.add("Date", menu.getMenuList().get(i).getMealDate().toString());

            if (menu.getMenuList().get(i).getMealTime().equals("B")) {
                jsonResponseObject.add("Time", "Breakfast");
            } else if (menu.getMenuList().get(i).getMealTime().equals("L")) {
                jsonResponseObject.add("Time", "Lunch");
            } else {
                jsonResponseObject.add("Time", "Tea Break");
            }

            if (menu.getMenuList().get(i).getVegetarian() == true) {
                jsonResponseObject.add("Vegetarian", "Yes");
            } else {
                jsonResponseObject.add("Vegetarian", "No");
            }
            jsonResponseObject.add("Main", menu.getMenuList().get(i).getMain());
            jsonResponseObject.add("Drinks", menu.getMenuList().get(i).getDrinks());
            jsonResponseObject.add("Dessert", menu.getMenuList().get(i).getDessert());
            jsonResponseObject.add("Remarks", menu.getMenuList().get(i).getRemarks());
            jsonResponseObject.add("Reviews", menu.getMenuList().get(i).getReviews());

            jsonResponseArray.add(jsonResponseObject);
        }

        return ResponseEntity.ok(jsonResponseArray.build().toString());
    }

    @PutMapping(path="/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changeMenu(@RequestBody String json) throws IOException {
        MenuChange menuChange = MenuChange.createMenuChange(json);
        JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();

        if (foodFormSvc.updateMenu(menuChange)) {
            jsonResponseObject.add("result", "Successfully updated");
            return ResponseEntity.ok().body(jsonResponseObject.build().toString());
        }

        jsonResponseObject.add("result", "An error occurred while updating");
        return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    }

    @DeleteMapping(path="/delete")
    public ResponseEntity<String> deleteMenu(@RequestParam String mealId) {
        JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
        
        if (foodFormSvc.deleteMenu(mealId) == 0) {
            jsonResponseObject.add("result", "Successfully deleted");
            return ResponseEntity.ok().body(jsonResponseObject.build().toString());
        } else if (foodFormSvc.deleteMenu(mealId) == 1) {
            jsonResponseObject.add("result", "An error occurred while deleting");
            return ResponseEntity.ok().body(jsonResponseObject.build().toString());
        }
        
        jsonResponseObject.add("result", "Meal does not exist");
        return ResponseEntity.badRequest().body(jsonResponseObject.build().toString());
    }

    // food reviews

    @GetMapping(path="/reviews/{mealId}")
    public ResponseEntity<String> getReviews(@PathVariable String mealId) {
        Optional<Reviews> optReviews = foodFormSvc.getReviews(mealId);
        JsonObjectBuilder jsonResponseObject = Json.createObjectBuilder();
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();

        if (optReviews.isEmpty()) {
            jsonResponseObject.add("result", "No reviews were found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponseObject.build().toString());
        }

        Reviews reviews = optReviews.get();
        
        for (int i=0; i<reviews.getReviewList().size(); i++) {
            JsonObjectBuilder innerObject = Json.createObjectBuilder();
            innerObject.add("PostId", reviews.getReviewList().get(i).getPostId());
            innerObject.add("User", reviews.getReviewList().get(i).getUserId());
            innerObject.add("Date", reviews.getReviewList().get(i).getPostDate().toString());
            innerObject.add("Caption", reviews.getReviewList().get(i).getCaption());
            innerObject.add("Rating", reviews.getReviewList().get(i).getRating());
            if (reviews.getReviewList().get(i).getPic() != null) {
                innerObject.add("Pic", reviews.getReviewList().get(i).getPic());
            }

            jsonArray.add(innerObject);
        }
        // jsonResponseObject.add(jsonArray);

        return ResponseEntity.ok(jsonArray.build().toString());
    }

    // statistics

    @GetMapping(path="/stats")
    public ResponseEntity<String> getStats(@RequestParam Integer month) {

        // Date date= new Date();
        // Calendar cal = Calendar.getInstance();
        // cal.setTime(date);
        // int month = cal.get(Calendar.MONTH);
        System.out.println(">>> Month: " + month);

        List<String> thisMonthDates = foodFormSvc.getDatesForThisMonthStats(month);
        // List<String> lastMonthDates = foodFormSvc.getDatesForLastMonthStats(month-1);

        List<Ratings> thisMonthRatings = foodFormSvc.getStats(thisMonthDates);
        // List<Ratings> lastMonthRatings = foodFormSvc.getStats(lastMonthDates);

        System.out.println(">>> This month ratings: " + thisMonthRatings);
        // System.out.println(">>> Last month ratings: " + lastMonthRatings);

        JsonArrayBuilder jsonResponseArray = Json.createArrayBuilder();
        JsonObjectBuilder breakfastObject = Json.createObjectBuilder();
        JsonObjectBuilder lunchObject = Json.createObjectBuilder();
        JsonObjectBuilder teaObject = Json.createObjectBuilder();

        JsonArrayBuilder breakfastArray = Json.createArrayBuilder();
        JsonArrayBuilder lunchArray = Json.createArrayBuilder();
        JsonArrayBuilder teaArray = Json.createArrayBuilder();

        breakfastObject.add("name", "Breakfast");
        lunchObject.add("name", "Lunch");
        teaObject.add("name", "Tea Break");

        for (int i=0; i<thisMonthRatings.size(); i++) {
            if (thisMonthRatings.get(i).getMealTime().equals("B")) {
                JsonObjectBuilder innerObject = Json.createObjectBuilder();
                innerObject.add("name", thisMonthRatings.get(i).getDate());
                innerObject.add("value", thisMonthRatings.get(i).getAverageRating());
                innerObject.add("Meal", thisMonthRatings.get(i).getMain());
                innerObject.add("Id", thisMonthRatings.get(i).getMealId());
                breakfastArray.add(innerObject);
            } else if (thisMonthRatings.get(i).getMealTime().equals("L")) {
                JsonObjectBuilder innerObject = Json.createObjectBuilder();
                innerObject.add("name", thisMonthRatings.get(i).getDate());
                innerObject.add("value", thisMonthRatings.get(i).getAverageRating());
                innerObject.add("Meal", thisMonthRatings.get(i).getMain());
                innerObject.add("Id", thisMonthRatings.get(i).getMealId());
                lunchArray.add(innerObject);
            } else {
                JsonObjectBuilder innerObject = Json.createObjectBuilder();
                innerObject.add("name", thisMonthRatings.get(i).getDate());
                innerObject.add("value", thisMonthRatings.get(i).getAverageRating());
                innerObject.add("Meal", thisMonthRatings.get(i).getMain());
                innerObject.add("Id", thisMonthRatings.get(i).getMealId());
                teaArray.add(innerObject);
            }
        }

        breakfastObject.add("series", breakfastArray);
        lunchObject.add("series", lunchArray);
        teaObject.add("series", teaArray);

        jsonResponseArray.add(breakfastObject);
        jsonResponseArray.add(lunchObject);
        jsonResponseArray.add(teaObject);

        return ResponseEntity.ok(jsonResponseArray.build().toString());
    }

    @GetMapping(path="/pax")
    public ResponseEntity<String> getPax(@RequestParam Integer month) {

        // Date date= new Date();
        // Calendar cal = Calendar.getInstance();
        // cal.setTime(date);
        // int month = cal.get(Calendar.MONTH);
        System.out.println(">>> Month: " + month);

        List<String> thisMonthDates = foodFormSvc.getDatesForThisMonthStats(month);
        System.out.println(">>> THIS MONTH DATES!: " + thisMonthDates);
        // List<String> lastMonthDates = foodFormSvc.getDatesForLastMonthStats(month-1);

        List<Ratings> thisMonthRatings = foodFormSvc.getStats(thisMonthDates);
        // List<Ratings> lastMonthRatings = foodFormSvc.getStats(lastMonthDates);

        System.out.println(">>> This month ratings: " + thisMonthRatings);
        // System.out.println(">>> Last month ratings: " + lastMonthRatings);

        JsonArrayBuilder jsonResponseArray = Json.createArrayBuilder();
        JsonObjectBuilder breakfastObject = Json.createObjectBuilder();
        JsonObjectBuilder lunchObject = Json.createObjectBuilder();
        JsonObjectBuilder teaObject = Json.createObjectBuilder();

        JsonArrayBuilder breakfastArray = Json.createArrayBuilder();
        JsonArrayBuilder lunchArray = Json.createArrayBuilder();
        JsonArrayBuilder teaArray = Json.createArrayBuilder();

        breakfastObject.add("name", "Breakfast");
        lunchObject.add("name", "Lunch");
        teaObject.add("name", "Tea Break");

        for (int i=0; i<thisMonthRatings.size(); i++) {
            if (thisMonthRatings.get(i).getMealTime().equals("B")) {
                JsonObjectBuilder innerObject = Json.createObjectBuilder();
                innerObject.add("name", thisMonthRatings.get(i).getDate());
                innerObject.add("value", thisMonthRatings.get(i).getTotalPax());
                innerObject.add("Meal", thisMonthRatings.get(i).getMain());
                innerObject.add("Id", thisMonthRatings.get(i).getMealId());
                breakfastArray.add(innerObject);
            } else if (thisMonthRatings.get(i).getMealTime().equals("L")) {
                JsonObjectBuilder innerObject = Json.createObjectBuilder();
                innerObject.add("name", thisMonthRatings.get(i).getDate());
                innerObject.add("value", thisMonthRatings.get(i).getTotalPax());
                innerObject.add("Meal", thisMonthRatings.get(i).getMain());
                innerObject.add("Id", thisMonthRatings.get(i).getMealId());
                lunchArray.add(innerObject);
            } else {
                JsonObjectBuilder innerObject = Json.createObjectBuilder();
                innerObject.add("name", thisMonthRatings.get(i).getDate());
                innerObject.add("value", thisMonthRatings.get(i).getTotalPax());
                innerObject.add("Meal", thisMonthRatings.get(i).getMain());
                innerObject.add("Id", thisMonthRatings.get(i).getMealId());
                teaArray.add(innerObject);
            }
        }

        breakfastObject.add("series", breakfastArray);
        lunchObject.add("series", lunchArray);
        teaObject.add("series", teaArray);

        jsonResponseArray.add(breakfastObject);
        jsonResponseArray.add(lunchObject);
        jsonResponseArray.add(teaObject);

        return ResponseEntity.ok(jsonResponseArray.build().toString());
    }
    
}
