package vttp2022.csf.finalprojectbackend.Models;

public class Attendance {

    private String mealId;
    private String userId;
    
    public String getMealId() {
        return mealId;
    }
    public void setMealId(String mealId) {
        this.mealId = mealId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Attendance [mealId=" + mealId + ", userId=" + userId + "]";
    }

}
