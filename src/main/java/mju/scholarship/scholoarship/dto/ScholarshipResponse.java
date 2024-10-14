package mju.scholarship.scholoarship.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class ScholarshipResponse {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 장학금 관련 데이터
     */
    private String name;
    private String description;

    /**
     * 장학금 조건 데이터
     */
    private String university;
    private Integer age;
    private String gender; //enum 으로 할까
    private String city;
    private String department; // major -> 전공 (?)
    private Integer grade; // 1.0 - 4.5
    private Integer incomeQuantile; // 1-10
}
