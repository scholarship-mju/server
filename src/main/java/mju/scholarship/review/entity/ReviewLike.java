package mju.scholarship.review.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.scholarship.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor
public class ReviewLike {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Review review;

    @ManyToOne
    private Member member;

    @Builder
    public ReviewLike(Review review, Member member) {
        this.review = review;
        this.member = member;
    }
}
