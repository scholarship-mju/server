package mju.scholarship.scholoarship.dto;

import lombok.Getter;

@Getter
public class CreateScholarshipRequest {

    private Integer price;
    private String category;
    private String name;
    private String university;
    private Integer minAge;
    private Integer maxAge;
    private String gender; //enum 으로 할까
    private String province;
    private String city;
    private String department; // major -> 전공 (?)
    private Integer grade; // 1.0 - 4.5
    private Integer incomeQuantile; // 1-10
}
