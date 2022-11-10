package vttp2022.csf.finalprojectbackend.Models;

import java.util.Date;

public class MenuItem {

    private String mealId;
    private Boolean vegetarian;
    private String mealTime;
    private Date mealDate;
    private String main;
    private String drinks;
    private String dessert;
    private String remarks;
    private Integer attendance;
    private Boolean reviews;
    
    public String getMealId() {
        return mealId;
    }
    public void setMealId(String mealId) {
        this.mealId = mealId;
    }
    public Boolean getVegetarian() {
        return vegetarian;
    }
    public void setVegetarian(Boolean vegetarian) {
        this.vegetarian = vegetarian;
    }
    public String getMealTime() {
        return mealTime;
    }
    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }
    public Date getMealDate() {
        return mealDate;
    }
    public void setMealDate(Date mealDate) {
        this.mealDate = mealDate;
    }
    public String getMain() {
        return main;
    }
    public void setMain(String main) {
        this.main = main;
    }
    public String getDrinks() {
        return drinks;
    }
    public void setDrinks(String drinks) {
        this.drinks = drinks;
    }
    public String getDessert() {
        return dessert;
    }
    public void setDessert(String dessert) {
        this.dessert = dessert;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public Integer getAttendance() {
        return attendance;
    }
    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }
    public Boolean getReviews() {
        return reviews;
    }
    public void setReviews(Boolean reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "MenuItem [mealId=" + mealId + ", vegetarian=" + vegetarian + ", mealTime=" + mealTime + ", mealDate="
                + mealDate + ", main=" + main + ", drinks=" + drinks + ", dessert=" + dessert + ", remarks=" + remarks
                + ", attendance=" + attendance + ", reviews=" + reviews + "]";
    }

    


    
    // private String menuId;
    // private String mealDate;
    // private String mealTime;
    // private Boolean vegetarian;
    // private String main;
    // private String drinks;
    // private String dessert;
    // private String remarks;

    // public String getMenuId() {
    //     return menuId;
    // }
    // public void setMenuId(String menuId) {
    //     this.menuId = menuId;
    // }
    // public String getMealDate() {
    //     return mealDate;
    // }
    // public void setMealDate(String mealDate) {
    //     this.mealDate = mealDate;
    // }
    // public String getMealTime() {
    //     return mealTime;
    // }
    // public void setMealTime(String mealTime) {
    //     this.mealTime = mealTime;
    // }
    // public Boolean getVegetarian() {
    //     return vegetarian;
    // }
    // public void setVegetarian(Boolean vegetarian) {
    //     this.vegetarian = vegetarian;
    // }
    // public String getMain() {
    //     return main;
    // }
    // public void setMain(String main) {
    //     this.main = main;
    // }
    // public String getDrinks() {
    //     return drinks;
    // }
    // public void setDrinks(String drinks) {
    //     this.drinks = drinks;
    // }
    // public String getDessert() {
    //     return dessert;
    // }
    // public void setDessert(String dessert) {
    //     this.dessert = dessert;
    // }
    // public String getRemarks() {
    //     return remarks;
    // }
    // public void setRemarks(String remarks) {
    //     this.remarks = remarks;
    // }

    
    
}
