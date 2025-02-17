package mju.scholarship.review.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import mju.scholarship.member.entity.Member;
import mju.scholarship.scholoarship.Scholarship;

@Getter
public class ReviewResponse {

    private Long id;

    private String memberName;

    private String content;

    private int likes;


    @Builder
    public ReviewResponse(Long id, String memberName, String content, int likes) {
        this.id = id;
        this.memberName = memberName;
        this.content = content;
        this.likes = likes;
    }
}
