package mju.scholarship.review;

import lombok.RequiredArgsConstructor;
import mju.scholarship.result.ResultResponse;
import mju.scholarship.result.code.ResultCode;
import mju.scholarship.review.dto.AllReviewResponse;
import mju.scholarship.review.dto.ReviewRequest;
import mju.scholarship.review.dto.ReviewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("")
    public ResponseEntity<ResultResponse> createReview(@RequestBody ReviewRequest reviewRequest) {
        reviewService.createReview(reviewRequest);
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.CreateReviewSuccess));
    }

    @GetMapping("/{scholarshipId}")
    public ResponseEntity<List<ReviewResponse>> getScholarshipReview(@PathVariable Long scholarshipId) {
        return ResponseEntity.ok().body(reviewService.getScholarshipReview(scholarshipId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AllReviewResponse>> getAllReviews() {
        return ResponseEntity.ok().body(reviewService.getAllReview());
    }



}
