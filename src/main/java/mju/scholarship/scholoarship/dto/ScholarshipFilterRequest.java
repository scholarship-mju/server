package mju.scholarship.scholoarship.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ScholarshipFilterRequest {

    private Integer age;


    private String university;

    private String department;

    private String gender;

    private Integer incomeQuantile;

    private String scholarshipName;
}
