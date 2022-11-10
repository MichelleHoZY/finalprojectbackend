package vttp2022.csf.finalprojectbackend.Models;

import java.util.List;

public class ApiModel {
    private String name;
    private String imageUrl;
    private String recipeUrl;
    private List<String> ingredients;
    private String calories;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getRecipeUrl() {
        return recipeUrl;
    }
    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }
    public String getCalories() {
        return calories;
    }
    public void setCalories(String calories) {
        this.calories = calories;
    }
    public List<String> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    
    @Override
    public String toString() {
        return "ApiModel [name=" + name + ", imageUrl=" + imageUrl + ", recipeUrl=" + recipeUrl + ", ingredients="
                + ingredients + ", calories=" + calories + "]";
    }
    
}
