package mju.scholarship.scholoarship.dto;

import lombok.Getter;

@Getter
public class CreateScholarshipRequest {

    private String name;
    private String university;
    private Integer age;
    private String gender; //enum 으로 할까
    private String city;
    private String department; // major -> 전공 (?)
    private Integer grade; // 1.0 - 4.5
    private Integer incomeQuantile; // 1-10
}
