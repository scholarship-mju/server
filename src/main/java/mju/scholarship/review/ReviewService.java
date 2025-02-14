package mju.scholarship.review;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.entity.Member;
import mju.scholarship.result.exception.ScholarshipNotFoundException;
import mju.scholarship.review.dto.ReviewRequest;
import mju.scholarship.review.dto.ReviewResponse;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.hibernate.query.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final JwtUtil jwtUtil;
    private final ScholarShipRepository scholarShipRepository;
    private final ReviewRepository reviewRepository;


    @Transactional
    public void createReview(ReviewRequest reviewRequest) {
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(reviewRequest.getScholarshipId())
                .orElseThrow(ScholarshipNotFoundException::new);

        Review.builder()
                .member(loginMember)
                .content(reviewRequest.getContent())
                .scholarship(scholarship)
                .build();

    }

    public List<ReviewResponse> getScholarshipReview(Long scholarshipId) {

        List<Review> reviewList = reviewRepository.findAllByScholarshipId(scholarshipId);

        List<ReviewResponse> reviewResponseList = new ArrayList<>();

        for(Review review : reviewList) {
            ReviewResponse reviewResponse = ReviewResponse.builder()
                    .content(review.getContent())
                    .member(review.getMember())
                    .likes(review.getLikes())
                    .build();
            reviewResponseList.add(reviewResponse);
        }

        return reviewResponseList;
    }
}
