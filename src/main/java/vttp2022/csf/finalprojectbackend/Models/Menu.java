package vttp2022.csf.finalprojectbackend.Models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Menu {

    private List<MenuItem> menuList;

    public List<MenuItem> getMenuList() {
        return menuList;
    }
    public void setMenuList(List<MenuItem> menuList) {
        this.menuList = menuList;
    }
    public void addMenuItem(MenuItem menuItem) {
        this.menuList.add(menuItem);
    }    

    public static Menu createMenu(String json) throws IOException, ParseException {
        Menu menu = new Menu();
        List<MenuItem> menuList = new LinkedList<>();

        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject object = reader.readObject();
            String jsonDate = object.getString("date");
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(jsonDate);
            System.out.println(">>> Date: " + date);

            JsonObject innerObject = object.getJsonObject("menuItems");
            Set<String> keySet = innerObject.keySet();
            System.out.println(">>> Keyset: " + keySet);

            MenuItem breakfastItem = new MenuItem();
            MenuItem lunchItem = new MenuItem();
            MenuItem teaItem = new MenuItem();

            // create breakfast item

            breakfastItem.setMealDate(date);
            breakfastItem.setMealId(UUID.randomUUID().toString().substring(0,8));
            breakfastItem.setMealTime("B");
            breakfastItem.setVegetarian(innerObject.getBoolean("vegetarian"));
            breakfastItem.setMain(innerObject.getString("breakfast_main"));
            breakfastItem.setDrinks(innerObject.getString("breakfast_drinks"));
            breakfastItem.setDessert(innerObject.getString("breakfast_dessert"));
            breakfastItem.setRemarks(innerObject.getString("breakfast_remarks"));
            
            // create breakfast item

            lunchItem.setMealDate(date);
            lunchItem.setMealId(UUID.randomUUID().toString().substring(0,8));
            lunchItem.setMealTime("L");
            lunchItem.setVegetarian(innerObject.getBoolean("vegetarian"));
            lunchItem.setMain(innerObject.getString("lunch_main"));
            lunchItem.setDrinks(innerObject.getString("lunch_drinks"));
            lunchItem.setDessert(innerObject.getString("lunch_dessert"));
            lunchItem.setRemarks(innerObject.getString("lunch_remarks"));

            // create breakfast item

            teaItem.setMealDate(date);
            teaItem.setMealId(UUID.randomUUID().toString().substring(0,8));
            teaItem.setMealTime("T");
            teaItem.setVegetarian(innerObject.getBoolean("vegetarian"));
            teaItem.setMain(innerObject.getString("tea_main"));
            teaItem.setDrinks(innerObject.getString("tea_drinks"));
            teaItem.setDessert(innerObject.getString("tea_dessert"));
            teaItem.setRemarks(innerObject.getString("tea_remarks"));

            menuList.add(breakfastItem);
            menuList.add(lunchItem);
            menuList.add(teaItem);
        }
        menu.setMenuList(menuList);

        System.out.println(">>> Menu in models: " + menu);
        return menu;       
    }

    @Override
    public String toString() {
        return "Menu [menuList=" + menuList + "]";
    }  
    
}
