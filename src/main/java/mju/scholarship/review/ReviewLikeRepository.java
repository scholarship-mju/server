package mju.scholarship.review;

import mju.scholarship.member.entity.Member;
import mju.scholarship.review.entity.Review;
import mju.scholarship.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    boolean existsByMemberAndReview(Member member, Review review);

    ReviewLike findByMemberAndReview(Member member, Review review);
}
