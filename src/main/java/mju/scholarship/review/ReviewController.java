package mju.scholarship.review;

import com.amazonaws.Response;
import lombok.RequiredArgsConstructor;
import mju.scholarship.result.ResultResponse;
import mju.scholarship.result.code.ResultCode;
import mju.scholarship.review.dto.AllReviewResponse;
import mju.scholarship.review.dto.ReviewRequest;
import mju.scholarship.review.dto.ReviewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
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

    @PostMapping("/{reviewId}/like")
    public ResponseEntity<ResultResponse> likeReview(@PathVariable Long reviewId){
        reviewService.likeReview(reviewId);
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.LikeReviewSuccess));
    }

    @PostMapping("/{reviewId}/like/cancel")
    public ResponseEntity<ResultResponse> cancelLikeReview(@PathVariable Long reviewId){
        reviewService.cancelLikeReview(reviewId);
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.CancelLikeReview));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ResultResponse> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.DeleteReviewSuccess));
    }





}
