package mju.scholarship.scholoarship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.scholarship.scholoarship.ScholarshipProgressStatus;

@Getter
@NoArgsConstructor
public class InterestedScholarshipResponse {

    private Long id;
    private String name; // 장학금 이름
    private String supportDetails;
    private ScholarshipProgressStatus progressStatus;

    @Builder
    public InterestedScholarshipResponse(Long id, String name, String supportDetails, ScholarshipProgressStatus progressStatus) {
        this.id = id;
        this.name = name;
        this.supportDetails = supportDetails;
        this.progressStatus = progressStatus;
    }
}
