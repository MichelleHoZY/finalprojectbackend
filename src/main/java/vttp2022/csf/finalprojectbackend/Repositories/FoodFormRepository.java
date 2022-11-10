package vttp2022.csf.finalprojectbackend.Repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.csf.finalprojectbackend.Models.Attendance;
import vttp2022.csf.finalprojectbackend.Models.MealReview;
import vttp2022.csf.finalprojectbackend.Models.Menu;
import vttp2022.csf.finalprojectbackend.Models.MenuChange;
import vttp2022.csf.finalprojectbackend.Models.MenuItem;
import vttp2022.csf.finalprojectbackend.Models.NewUser;
import vttp2022.csf.finalprojectbackend.Models.Reviews;

import static vttp2022.csf.finalprojectbackend.Repositories.Queries.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Repository
public class FoodFormRepository {

    @Autowired
    private JdbcTemplate template;

    // food menu repo methods

    public int saveMenu(Menu menu) {
        int totalAdded = 0;
        for (int i=0; i<menu.getMenuList().size(); i++) {
            int menuItemAdded = template.update(SQL_SAVE_MENU, menu.getMenuList().get(i).getMealId(), menu.getMenuList().get(i).getMealDate(),
                menu.getMenuList().get(i).getMealTime(), menu.getMenuList().get(i).getVegetarian(), menu.getMenuList().get(i).getMain(),
                menu.getMenuList().get(i).getDrinks(), menu.getMenuList().get(i).getDessert(), menu.getMenuList().get(i).getRemarks());
            totalAdded = totalAdded + menuItemAdded;
        }
        return totalAdded;
    }

    public Menu getAllMenus() {
        Menu menu = new Menu();
        List<MenuItem> menuList = new LinkedList<>();
        SqlRowSet rs = template.queryForRowSet(SQL_GET_ALL_MENUS);
        while (rs.next()) {
            MenuItem menuItem = new MenuItem();
            menuItem.setMealId(rs.getString("meal_id"));
            menuItem.setMealDate(rs.getDate("meal_date"));
            menuItem.setMealTime(rs.getString("meal_time"));
            menuItem.setVegetarian(rs.getBoolean("vegetarian"));
            menuItem.setMain(rs.getString("main"));
            menuItem.setDrinks(rs.getString("drinks"));
            menuItem.setDessert(rs.getString("dessert"));
            menuItem.setRemarks(rs.getString("remarks"));

            menuList.add(menuItem);
        }
        menu.setMenuList(menuList);

        for (int i=0; i < menuList.size(); i++) {
            if (checkMealReviews(menuList.get(i).getMealId())) {
                menuList.get(i).setReviews(true);
            } else {
                menuList.get(i).setReviews(false);
            }
        }
        return menu;
    }

    public boolean updateMenu(MenuChange menuChange) {
        int rowUpdated = template.update(
            SQL_UPDATE_MENU, menuChange.getMain(), menuChange.getDrinks(), menuChange.getDessert(), menuChange.getRemarks(), menuChange.getMealId()
        );

        if (rowUpdated == 1) {
            return true;
        }

        return false;
    }

    public boolean deleteMenu(String mealId) {
        int rowDeleted = template.update(
            SQL_DELETE_MENU, mealId
        );

        if (rowDeleted == 1) {
            return true;
        }

        return false;
    }

    public boolean getMenu(String mealId) {
        SqlRowSet rs = template.queryForRowSet(
            SQL_GET_MENU, mealId
        );

        if (rs.next()) {
            return true;
        }

        return false;
    }

    public List<MenuItem> getMenuByDateStudent(String date, Boolean vegetarian) {
        List<MenuItem> menuItemList = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(
            SQL_GET_MENU_BY_DATE_STUDENT, date, vegetarian
        );

        while (rs.next()) {
            MenuItem menuItem = new MenuItem();

            menuItem.setMealId(rs.getString("meal_id"));
            menuItem.setVegetarian(rs.getBoolean("vegetarian"));
            menuItem.setMealDate(rs.getDate("meal_date"));
            menuItem.setMealTime(rs.getString("meal_time"));
            menuItem.setMain(rs.getString("main"));
            menuItem.setDrinks(rs.getString("drinks"));
            menuItem.setDessert(rs.getString("dessert"));
            menuItem.setRemarks(rs.getString("remarks"));

            menuItemList.add(menuItem);
        }

        // System.out.println(menuItemList);
        // Collections.sort(menuItemList, (o1, o2) -> (o1.getMealTime().compareTo(o2.getMealTime())));
        // System.out.println(menuItemList);

        return menuItemList;
    }

    public List<MenuItem> getMenuByDate(String date) {
        List<MenuItem> menuItemList = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(
            SQL_GET_MENU_BY_DATE, date
        );

        while (rs.next()) {
            MenuItem menuItem = new MenuItem();

            menuItem.setMealId(rs.getString("meal_id"));
            menuItem.setVegetarian(rs.getBoolean("vegetarian"));
            menuItem.setMealDate(rs.getDate("meal_date"));
            menuItem.setMealTime(rs.getString("meal_time"));
            menuItem.setMain(rs.getString("main"));
            menuItem.setDrinks(rs.getString("drinks"));
            menuItem.setDessert(rs.getString("dessert"));
            menuItem.setRemarks(rs.getString("remarks"));

            menuItemList.add(menuItem);
        }

        // System.out.println(menuItemList);
        // Collections.sort(menuItemList, (o1, o2) -> (o1.getMealTime().compareTo(o2.getMealTime())));
        // System.out.println(menuItemList);

        return menuItemList;
    }

    // food reviews repo methods

    public Reviews getReviews(String mealId) {
        Reviews reviews = new Reviews();
        List<MealReview> reviewList = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(
            SQL_GET_REVIEWS, mealId
        );

        while (rs.next()) {
            MealReview mealReview = new MealReview();
            mealReview.setUserId(rs.getInt("user_id"));
            mealReview.setPostId(rs.getString("post_id"));
            mealReview.setPostDate(rs.getDate("post_date"));
            mealReview.setCaption(rs.getString("caption"));
            mealReview.setRating(rs.getInt("rating"));
            mealReview.setPic(rs.getString("pic"));

            reviewList.add(mealReview);
        }

        reviews.setReviewList(reviewList);
        return reviews;
    }

    public boolean checkMealReviews(String mealId) {
        SqlRowSet rs = template.queryForRowSet(
            SQL_GET_REVIEWS, mealId
        );

        if (rs.next()) {
            return true;
        }

        return false;
    }

    public List<String> getUserReviews(String userId) {
        List<String> reviewsList = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(
            SQL_GET_USER_REVIEWS, userId
        );

        while (rs.next()) {

            // mealReview.setPostId(rs.getString("post_id"));
            // mealReview.setPostDate(rs.getDate("post_date"));
            // mealReview.setCaption(rs.getString("caption"));
            // mealReview.setRating(rs.getInt("rating"));
            // mealReview.setPic(rs.getString("pic"));

            reviewsList.add(rs.getString("meal_id"));
        }
        return reviewsList;
    }

    public Menu getUserMeals(String userId) {
        Menu menu = new Menu();
        List<MenuItem> menuItemList = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(
            SQL_GET_USER_MEALS, userId
        );
        while (rs.next()) {
            MenuItem menuItem = new MenuItem();

            menuItem.setMealId(rs.getString("meal_id"));
            menuItem.setMealDate(rs.getDate("meal_date"));
            menuItem.setMealTime(rs.getString("meal_time"));
            menuItem.setMain(rs.getString("main"));
            menuItem.setDrinks(rs.getString("drinks"));
            menuItem.setDessert(rs.getString("dessert"));
            menuItem.setRemarks(rs.getString("remarks"));

            menuItemList.add(menuItem);
        }
        menu.setMenuList(menuItemList);
        return menu;

    }

    public MealReview getUserMealReview(String userId, String mealId) {
        MealReview mealReview = new MealReview();

        SqlRowSet rs = template.queryForRowSet(
            SQL_GET_USER_MEAL_REVIEW, userId, mealId
        );
        while (rs.next()) {
            mealReview.setPostId(rs.getString("post_id"));
            mealReview.setRating(rs.getInt("rating"));
            if (rs.getString("caption") == null) {
                System.out.println(">>> Null caption");
                mealReview.setCaption(""); 
            } else {
                mealReview.setCaption(rs.getString("caption"));
            }

            if (rs.getString("pic") == null) {
                System.out.println(">>> Null pic");
                mealReview.setPic("");
            } else {
                mealReview.setPic(rs.getString("pic"));
            }
        }

        System.out.println(mealReview);
        return mealReview;
    }

    public boolean saveReview(MealReview mealReview) {
        int reviewSaved = template.update(
            SQL_SAVE_USER_REVIEWS, mealReview.getUserId(), mealReview.getPostId(), mealReview.getMealId(),
            mealReview.getPostDate(), mealReview.getRating(), mealReview.getCaption(), mealReview.getPic()
        );
        if (reviewSaved == 1) {
            return true;
        }

        return false;
    }

    public String getPicForUpdatedReview(String postId) {
        String pic = new String();
        SqlRowSet rs = template.queryForRowSet(
            SQL_DELETE_PIC_FOR_UPDATE, postId
        );

        if (rs.next()) {
            pic = rs.getString("pic");
        }

        return pic;
    }

    public boolean updateReview(MealReview mealReview) {
        int reviewUpdated = template.update(
            SQL_UPDATE_USER_MEAL_REVIEW, mealReview.getRating(), mealReview.getCaption(), mealReview.getPic(), mealReview.getPostDate(), mealReview.getPostId()
        );
        if (reviewUpdated == 1) {
            return true;
        }

        return false;
    }

    // meal attendance repo methods

    public List<String> getPreselected(List<String> date, String userId) {
        List<String> preselected = new LinkedList<>();
        for (int i=0; i<date.size(); i++) {
            SqlRowSet rs = template.queryForRowSet(
                SQL_GET_PRESELECTED_MENUS, date.get(i), userId
            );
            
            while (rs.next()) {
                preselected.add(rs.getString("meal_id"));
            }
        }

        return preselected;
    }

    public int deleteAttendance(String userId, List<String> mealsList) {
        int rowsDeleted = 0;
        for (int i=0; i<mealsList.size(); i++) {
            int internalCount = template.update(
                SQL_DELETE_MENU_ATTENDANCE, userId, mealsList.get(i)
            );
            rowsDeleted = rowsDeleted + internalCount;
        }
        return rowsDeleted;
    }

    public int saveAttendance(String userId, List<String> mealsList) {
        int totalRowsSaved = 0;
        for (int i=0; i<mealsList.size(); i++) {
            int internalCount = template.update(
                SQL_SAVE_USER_MEAL_ATTENDANCE, userId, mealsList.get(i)
            );
            totalRowsSaved = totalRowsSaved + internalCount;
        }
        return totalRowsSaved;
    }

    // login/create user page

    public boolean userLogin(String loginId, String password) {
        SqlRowSet rs = template.queryForRowSet(
            SQL_LOGIN, loginId, password
        );

        if (rs.next()) {
            return true;
        }

        return false;
    }

    public boolean createUser(NewUser newUser) {
        int userCreated = template.update(
            SQL_CREATE_NEW_USER, newUser.getUsername(), newUser.getPassword(), newUser.getUser_name(), 
            newUser.isVegetarian(), newUser.getStartDate(), newUser.getEndDate(), newUser.getUserType());

        if (userCreated == 1) {
            return true;
        }

        return false;
    }

    public boolean checkIdAvailability(String loginId) {
        SqlRowSet rs = template.queryForRowSet(
            SQL_CHECK_LOGIN_ID_EXISTS, loginId
        );

        if (rs.next()) {
            return true; // user exists
        }

        return false; // user does not exist
    }

    public NewUser getUserInfo(Integer userId) {
        SqlRowSet rs = template.queryForRowSet(
            SQL_GET_USER_INFO, userId
        );

        NewUser user = new NewUser();

        while (rs.next()) {
            if (userId == 1) {
                user.setUser_name(rs.getString("user_name"));
                user.setVegetarian(false);
                user.setEndDate(rs.getDate("end_date"));
            } else {
                user.setUser_name(rs.getString("user_name"));
                user.setVegetarian(rs.getBoolean("vegetarian"));
                user.setEndDate(rs.getDate("end_date"));
            }
        }

        return user;
    }

    // statistics

    public int getMealAttendance(String mealId) {
        SqlRowSet rs = template.queryForRowSet(
            SQL_COUNT_MEAL_ATTENDANCE, mealId
        );

        if (rs.next()) {
            int attendance = rs.getInt("count(meal_id)");
            System.out.println(">>> Attendance: " + attendance);
            return attendance;
        }

        return 0;
    }

    public List<Integer> getMealRatings(String mealId, String time) { // total ratings of a meal
        List<Integer> mealRatings = new LinkedList<>();
        SqlRowSet rs = template.queryForRowSet(
            SQL_GET_MEAL_RATINGS, mealId, time
        );

        while(rs.next()) {
            mealRatings.add(rs.getInt("rating"));
        }

        return mealRatings;
    }

    public Integer getNoOfMealRatings(String mealId, String time) { // number of ratings of a meal
        SqlRowSet rs = template.queryForRowSet(
            SQL_GET_MEAL_NO_OF_RATINGS, mealId, time
        );
        
        while (rs.next()) {
            return rs.getInt("count(r.rating)");
        }

        return 0;
    }
    
}
