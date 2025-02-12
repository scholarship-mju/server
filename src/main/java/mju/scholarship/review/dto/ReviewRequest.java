package mju.scholarship.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewRequest {

    private String content;

    private Long scholarshipId;
}
