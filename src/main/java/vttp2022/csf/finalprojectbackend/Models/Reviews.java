package vttp2022.csf.finalprojectbackend.Models;

import java.util.List;

public class Reviews {
    private List<MealReview> reviewList;

    public List<MealReview> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<MealReview> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public String toString() {
        return "Reviews [reviewList=" + reviewList + "]";
    }
    
}
