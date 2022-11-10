package vttp2022.csf.finalprojectbackend.Models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class FoodItems {
    private Boolean vegetarian;
    private Boolean mondayBreakfast;
    private Boolean mondayLunch;
    private Boolean mondayTea;
    private Boolean tuesdayBreakfast;
    private Boolean tuesdayLunch;
    private Boolean tuesdayTea;
    private Boolean wednesdayBreakfast;
    private Boolean wednesdayLunch;
    private Boolean wednesdayTea;
    private Boolean thursdayBreakfast;
    private Boolean thursdayLunch;
    private Boolean thursdayTea;
    private Boolean fridayBreakfast;
    private Boolean fridayLunch;
    private Boolean fridayTea;
    private String userId;

    public Boolean getVegetarian() {
        return vegetarian;
    }
    public void setVegetarian(Boolean vegetarian) {
        this.vegetarian = vegetarian;
    }
    public Boolean getMondayBreakfast() {
        return mondayBreakfast;
    }
    public void setMondayBreakfast(Boolean mondayBreakfast) {
        this.mondayBreakfast = mondayBreakfast;
    }
    public Boolean getMondayLunch() {
        return mondayLunch;
    }
    public void setMondayLunch(Boolean mondayLunch) {
        this.mondayLunch = mondayLunch;
    }
    public Boolean getMondayTea() {
        return mondayTea;
    }
    public void setMondayTea(Boolean mondayTea) {
        this.mondayTea = mondayTea;
    }
    public Boolean getTuesdayBreakfast() {
        return tuesdayBreakfast;
    }
    public void setTuesdayBreakfast(Boolean tuesdayBreakfast) {
        this.tuesdayBreakfast = tuesdayBreakfast;
    }
    public Boolean getTuesdayLunch() {
        return tuesdayLunch;
    }
    public void setTuesdayLunch(Boolean tuesdayLunch) {
        this.tuesdayLunch = tuesdayLunch;
    }
    public Boolean getTuesdayTea() {
        return tuesdayTea;
    }
    public void setTuesdayTea(Boolean tuesdayTea) {
        this.tuesdayTea = tuesdayTea;
    }
    public Boolean getWednesdayBreakfast() {
        return wednesdayBreakfast;
    }
    public void setWednesdayBreakfast(Boolean wednesdayBreakfast) {
        this.wednesdayBreakfast = wednesdayBreakfast;
    }
    public Boolean getWednesdayLunch() {
        return wednesdayLunch;
    }
    public void setWednesdayLunch(Boolean wednesdayLunch) {
        this.wednesdayLunch = wednesdayLunch;
    }
    public Boolean getWednesdayTea() {
        return wednesdayTea;
    }
    public void setWednesdayTea(Boolean wednesdayTea) {
        this.wednesdayTea = wednesdayTea;
    }
    public Boolean getThursdayBreakfast() {
        return thursdayBreakfast;
    }
    public void setThursdayBreakfast(Boolean thursdayBreakfast) {
        this.thursdayBreakfast = thursdayBreakfast;
    }
    public Boolean getThursdayLunch() {
        return thursdayLunch;
    }
    public void setThursdayLunch(Boolean thursdayLunch) {
        this.thursdayLunch = thursdayLunch;
    }
    public Boolean getThursdayTea() {
        return thursdayTea;
    }
    public void setThursdayTea(Boolean thursdayTea) {
        this.thursdayTea = thursdayTea;
    }
    public Boolean getFridayBreakfast() {
        return fridayBreakfast;
    }
    public void setFridayBreakfast(Boolean fridayBreakfast) {
        this.fridayBreakfast = fridayBreakfast;
    }
    public Boolean getFridayLunch() {
        return fridayLunch;
    }
    public void setFridayLunch(Boolean fridayLunch) {
        this.fridayLunch = fridayLunch;
    }
    public Boolean getFridayTea() {
        return fridayTea;
    }
    public void setFridayTea(Boolean fridayTea) {
        this.fridayTea = fridayTea;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static FoodItems createFoodItems(String json) throws IOException {
        FoodItems foodItem = new FoodItems();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject object = reader.readObject();

            foodItem.setVegetarian(object.getBoolean("vegetarian"));
            foodItem.setMondayBreakfast(object.getBoolean("monday_breakfast"));
            foodItem.setMondayLunch(object.getBoolean("monday_lunch"));
            foodItem.setMondayTea(object.getBoolean("monday_tea"));
            foodItem.setTuesdayBreakfast(object.getBoolean("tuesday_breakfast"));
            foodItem.setTuesdayLunch(object.getBoolean("tuesday_lunch"));
            foodItem.setTuesdayTea(object.getBoolean("tuesday_tea"));
            foodItem.setWednesdayBreakfast(object.getBoolean("wednesday_breakfast"));
            foodItem.setWednesdayLunch(object.getBoolean("wednesday_lunch"));
            foodItem.setWednesdayTea(object.getBoolean("wednesday_tea"));
            foodItem.setThursdayBreakfast(object.getBoolean("thursday_breakfast"));
            foodItem.setThursdayLunch(object.getBoolean("thursday_lunch"));
            foodItem.setThursdayBreakfast(object.getBoolean("thursday_tea"));
            foodItem.setFridayBreakfast(object.getBoolean("friday_breakfast"));
            foodItem.setFridayBreakfast(object.getBoolean("friday_lunch"));
            foodItem.setFridayBreakfast(object.getBoolean("friday_tea"));
            foodItem.setUserId(object.getString("user_id"));
        }

        return foodItem;
    }
    
}
