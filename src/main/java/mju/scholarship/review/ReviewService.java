package mju.scholarship.review;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.entity.Member;
import mju.scholarship.result.exception.AlreadyLikeReview;
import mju.scholarship.result.exception.NotFoundLikeReview;
import mju.scholarship.result.exception.ReviewNotFoundException;
import mju.scholarship.result.exception.ScholarshipNotFoundException;
import mju.scholarship.review.dto.AllReviewResponse;
import mju.scholarship.review.dto.ReviewRequest;
import mju.scholarship.review.dto.ReviewResponse;
import mju.scholarship.review.entity.Review;
import mju.scholarship.review.entity.ReviewLike;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final JwtUtil jwtUtil;
    private final ScholarShipRepository scholarShipRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;


    @Transactional
    public void createReview(ReviewRequest reviewRequest) {
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(reviewRequest.getScholarshipId())
                .orElseThrow(ScholarshipNotFoundException::new);

        Review review = Review.builder()
                .member(loginMember)
                .content(reviewRequest.getContent())
                .scholarship(scholarship)
                .build();

        reviewRepository.save(review);

    }

    public List<ReviewResponse> getScholarshipReview(Long scholarshipId) {

        List<Review> reviewList = reviewRepository.findAllByScholarshipId(scholarshipId);

        List<ReviewResponse> reviewResponseList = new ArrayList<>();

        for(Review review : reviewList) {
            ReviewResponse reviewResponse = ReviewResponse.builder()
                    .content(review.getContent())
                    .memberName(review.getMember().getNickname())
                    .id(review.getId())
                    .likes(review.getLikes())
                    .build();
            reviewResponseList.add(reviewResponse);
        }

        return reviewResponseList;
    }

    public List<AllReviewResponse> getAllReview() {

        List<Review> allReview = reviewRepository.findAll();

        List<AllReviewResponse> allReviewResponseList = new ArrayList<>();

        for (Review review : allReview) {
            AllReviewResponse response = AllReviewResponse.builder()
                    .content(review.getContent())
                    .price(review.getPrice())
                    .reviewId(review.getId())
                    .scholarshipName(review.getScholarship().getName())
                    .likes(review.getLikes())
                    .memberName(review.getMember().getNickname())
                    .build();
            allReviewResponseList.add(response);
        }

        return allReviewResponseList;
    }

    @Transactional
    public void likeReview(Long reviewId) {

        Member loginMember = jwtUtil.getLoginMember();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        boolean alreadyLike = reviewLikeRepository.existsByMemberAndReview(loginMember, review);

        if(alreadyLike) {
            throw new AlreadyLikeReview();
        }

        review.addLikes();
    }

    @Transactional
    public void cancelLikeReview(Long reviewId) {
        Member loginMember = jwtUtil.getLoginMember();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        boolean alreadyLike = reviewLikeRepository.existsByMemberAndReview(loginMember, review);

        ReviewLike like = reviewLikeRepository.findByMemberAndReview(loginMember,review);

        if(!alreadyLike) {
            throw new NotFoundLikeReview();
        }

        review.minusLikes();

        reviewLikeRepository.delete(like);
    }

    @Transactional
    public void deleteReview(Long reviewId) {

        reviewRepository.deleteById(reviewId);
    }
}
