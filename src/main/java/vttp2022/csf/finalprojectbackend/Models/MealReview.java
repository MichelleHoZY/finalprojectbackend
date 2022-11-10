package vttp2022.csf.finalprojectbackend.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class MealReview {
    private Integer userId;
    private String postId;
    private String mealId;
    private Date postDate;
    private Integer rating;
    private String caption;
    private String pic;
    
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getPostId() {
        return postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }
    public String getMealId() {
        return mealId;
    }
    public void setMealId(String mealId) {
        this.mealId = mealId;
    }
    public Date getPostDate() {
        return postDate;
    }
    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }

    public static MealReview createReview(MultipartFile file, String caption, Integer rating,
        String mealId, Integer userId, String date) throws ParseException {
        MealReview review = new MealReview();

        review.setCaption(caption);
        review.setPostId(UUID.randomUUID().toString().substring(0,8));
        Date stringDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        review.setPostDate(stringDate);
        review.setMealId(mealId);
        review.setRating(rating);
        review.setUserId(userId);
        // need to set review pic upload to bucket

        // try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
        //     JsonReader reader = Json.createReader(is);
        //     JsonObject object = reader.readObject();

            // review.setMealId(object.getString("mealId"));
            // review.setPostId(UUID.randomUUID().toString().substring(0,8));
            // String jsonDate = object.getString("date");
            // Date date = new SimpleDateFormat("yyyy-MM-dd").parse(jsonDate);
            // review.setPostDate(date);
            // review.setCaption(object.getString("caption"));
            // review.setRating(object.getInt("rating"));
        // }

        return review;
    }

    public static MealReview updateReview(MultipartFile file, String caption, Integer rating,
    String mealId, Integer userId, String date, String postId) throws ParseException {
    MealReview review = new MealReview();

    review.setCaption(caption);
    Date stringDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
    review.setPostDate(stringDate);
    review.setMealId(mealId);
    review.setRating(rating);
    review.setUserId(userId);
    review.setPostId(postId);
    // need to set review pic upload to bucket

    // try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
    //     JsonReader reader = Json.createReader(is);
    //     JsonObject object = reader.readObject();

        // review.setMealId(object.getString("mealId"));
        // review.setPostId(UUID.randomUUID().toString().substring(0,8));
        // String jsonDate = object.getString("date");
        // Date date = new SimpleDateFormat("yyyy-MM-dd").parse(jsonDate);
        // review.setPostDate(date);
        // review.setCaption(object.getString("caption"));
        // review.setRating(object.getInt("rating"));
    // }

    return review;
}

    @Override
    public String toString() {
        return "MealReview [userId=" + userId + ", postId=" + postId + ", mealId=" + mealId + ", postDate=" + postDate
                + ", rating=" + rating + ", caption=" + caption + ", pic=" + pic + "]";
    }

}
