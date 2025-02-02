package mju.scholarship.scholoarship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.scholarship.scholoarship.ScholarshipProgressStatus;

@Getter
@NoArgsConstructor
public class AllScholarshipResponse {

    private Long id;
    private String name; // 장학금 이름
    private String price; // 장학금 가격
    private Boolean isInterested; // 찜한 장학금인지
    private ScholarshipProgressStatus progressStatus; // 장학금 진행 상태

    @Builder
    public AllScholarshipResponse(Long id, String name, String price, Boolean isInterested, ScholarshipProgressStatus progressStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isInterested = isInterested;
        this.progressStatus = progressStatus;
    }
}
