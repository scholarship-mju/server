package mju.scholarship.review;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.scholarship.member.entity.Member;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.util.BaseTimeEntity;

@Entity
@NoArgsConstructor
@Getter
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Scholarship scholarship;

    @OneToOne
    private Member member;

    private String content;

    private int likes = 0;

    public void addLikes(){
        this.likes++;
    }

    @Builder
    public Review(Scholarship scholarship, Member member, String content) {
        this.scholarship = scholarship;
        this.member = member;
        this.content = content;
    }
}
