package mju.scholarship.review;

import lombok.RequiredArgsConstructor;
import mju.scholarship.result.ResultResponse;
import mju.scholarship.result.code.ResultCode;
import mju.scholarship.review.dto.ReviewRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("")
    public ResponseEntity<ResultResponse> createReview(ReviewRequest reviewRequest) {
        reviewService.createReview(reviewRequest);
        ResponseEntity.ok().body(ResultResponse.of(ResultCode.CreateReviewSuccess));
    }

}
