package mju.scholarship.scholoarship.dto;

import lombok.Builder;
import lombok.Getter;
import mju.scholarship.scholoarship.ScholarshipProgressStatus;

@Builder
@Getter
public class UnivScholarshipResponse {

    private Long id;
    private String name; // 장학금 이름
    private String supportDetails; // 장학금 가격
    private Boolean isInterested; // 찜한 장학금인지
    private String organizationName;
    private String scholarshipImage;
    private ScholarshipProgressStatus progressStatus; // 장학금 진행 상태
    private int viewCount;
}
