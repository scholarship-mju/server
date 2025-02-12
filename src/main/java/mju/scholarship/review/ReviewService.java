package mju.scholarship.review;

import lombok.RequiredArgsConstructor;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.entity.Member;
import mju.scholarship.result.exception.ScholarshipNotFoundException;
import mju.scholarship.review.dto.ReviewRequest;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final JwtUtil jwtUtil;
    private final ScholarShipRepository scholarShipRepository;


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
}
