package mju.scholarship.review;

import mju.scholarship.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByScholarshipId(Long scholarshipId);
}
