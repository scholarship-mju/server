package mju.scholarship.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.scholarship.scholoarship.Scholarship;

@Entity
@NoArgsConstructor
@Getter
public class MemberGot {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scholarship_id")
    private Scholarship scholarship;

    @Enumerated(EnumType.ORDINAL) // Enum 값을 정수로 저장
    @Column(nullable = false)
    private ScholarshipStatus status = ScholarshipStatus.NOT_VERIFIED;

    @Builder
    public MemberGot(Member member, Scholarship scholarship, ScholarshipStatus status) {
        this.member = member;
        this.scholarship = scholarship;
        this.status = status != null ? status : ScholarshipStatus.NOT_VERIFIED;
    }

    // 상태 변경 메서드
    public void changeStatus(ScholarshipStatus newStatus) {
        this.status = newStatus;
    }
}
