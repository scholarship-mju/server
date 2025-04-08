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
    private String supportDetails; // 장학금 가격
    private Boolean isInterested; // 찜한 장학금인지
    private String organizationName;
    private ScholarshipProgressStatus progressStatus; // 장학금 진행 상태
    private int viewCount;

    @Builder
    public AllScholarshipResponse(Long id, String name, String organizationName, String supportDetails, Boolean isInterested, ScholarshipProgressStatus progressStatus, int viewCount) {
        this.id = id;
        this.name = name;
        this.supportDetails = supportDetails;
        this.isInterested = isInterested;
        this.progressStatus = progressStatus;
        this.viewCount = viewCount;
        this.organizationName = organizationName;
    }
}
