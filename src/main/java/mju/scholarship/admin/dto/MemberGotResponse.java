package mju.scholarship.admin.dto;

import lombok.Builder;
import lombok.Getter;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.scholoarship.Scholarship;

@Getter
public class MemberGotResponse {

    private Long memberGotId;

    private Long memberId;

    private String memberName;

    private Long scholarshipId;

    private String scholarshipName;

    private ScholarshipStatus status;


    @Builder
    public MemberGotResponse(Long memberGotId, Long memberId, String memberName, Long scholarshipId, String scholarshipName, ScholarshipStatus status) {
        this.memberGotId = memberGotId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.scholarshipId = scholarshipId;
        this.scholarshipName = scholarshipName;
        this.status = status;
    }
}
