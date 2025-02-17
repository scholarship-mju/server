package mju.scholarship.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AllReviewResponse {

    private Long reviewId;

    private String content;

    private String scholarshipName;

    private String price;

    private int likes;

    private String memberName;

    @Builder
    public AllReviewResponse(Long reviewId, String content, String scholarshipName, String price, int likes, String memberName) {
        this.reviewId = reviewId;
        this.content = content;
        this.scholarshipName = scholarshipName;
        this.price = price;
        this.likes = likes;
        this.memberName = memberName;
    }
}
