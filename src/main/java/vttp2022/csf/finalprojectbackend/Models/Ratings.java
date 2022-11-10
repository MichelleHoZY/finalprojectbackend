package vttp2022.csf.finalprojectbackend.Models;

public class Ratings {
    private Double averageRating;
    private Integer totalPax;
    private String mealId;
    private String mealTime;
    private String main;
    private String drinks;
    private String dessert;
    private String date;
    
    public Double getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
    public String getMealId() {
        return mealId;
    }
    public void setMealId(String mealId) {
        this.mealId = mealId;
    }
    public String getMealTime() {
        return mealTime;
    }
    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
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
    public Integer getTotalPax() {
        return totalPax;
    }
    public void setTotalPax(Integer totalPax) {
        this.totalPax = totalPax;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Ratings [averageRating=" + averageRating + ", totalPax=" + totalPax + ", mealId=" + mealId
                + ", mealTime=" + mealTime + ", main=" + main + ", drinks=" + drinks + ", dessert=" + dessert
                + ", date=" + date + "]";
    }
    
}
