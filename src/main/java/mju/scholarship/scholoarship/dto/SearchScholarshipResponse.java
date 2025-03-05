package mju.scholarship.scholoarship.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchScholarshipResponse {

    private Long scholarshipId;

    private String scholarshipName;

    @Builder
    public SearchScholarshipResponse(Long scholarshipId, String scholarshipName) {
        this.scholarshipId = scholarshipId;
        this.scholarshipName = scholarshipName;
    }
}
