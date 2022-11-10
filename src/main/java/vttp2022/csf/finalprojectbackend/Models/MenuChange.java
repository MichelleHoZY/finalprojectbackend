package vttp2022.csf.finalprojectbackend.Models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class MenuChange {
    private String mealId;
    private String main;
    private String drinks;
    private String dessert;
    private String remarks;
    
    public String getMealId() {
        return mealId;
    }
    public void setMealId(String mealId) {
        this.mealId = mealId;
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

    public static MenuChange createMenuChange(String json) throws IOException {
        MenuChange menuChange = new MenuChange();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject object = reader.readObject();
            menuChange.setMealId(object.getString("mealId"));
            menuChange.setMain(object.getString("main"));
            menuChange.setDrinks(object.getString("drinks"));
            menuChange.setDessert(object.getString("dessert"));
            menuChange.setRemarks(object.getString("remarks"));
        }

        return menuChange;
    }

    @Override
    public String toString() {
        return "MenuChange [mealId=" + mealId + ", main=" + main + ", drinks=" + drinks + ", dessert=" + dessert
                + ", remarks=" + remarks + "]";
    }

}
