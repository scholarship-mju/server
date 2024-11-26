package mju.scholarship.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.scholarship.scholoarship.Scholarship;

@Entity
@NoArgsConstructor
@Getter
public class MemberInterest {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scholarship_id")
    private Scholarship scholarship;

    @Builder
    public MemberInterest(Member member, Scholarship scholarship) {
        this.member = member;
        this.scholarship = scholarship;
    }
}
