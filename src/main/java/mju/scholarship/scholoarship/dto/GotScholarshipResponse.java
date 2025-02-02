package mju.scholarship.scholoarship.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.scholoarship.ScholarshipProgressStatus;

@Getter
@NoArgsConstructor
public class GotScholarshipResponse {

    private Long id;
    private String name; // 장학금 이름
    private String price; // 장학금 가격
    private ScholarshipStatus status; // 받은 장학금 인증 상태

    @Builder
    public GotScholarshipResponse(Long id, String name, String price, ScholarshipStatus status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
    }
}
