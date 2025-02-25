package mju.scholarship.review;

import mju.scholarship.member.entity.Member;
import mju.scholarship.review.entity.Like;
import mju.scholarship.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByMemberAndReview(Member member, Review review);
}
