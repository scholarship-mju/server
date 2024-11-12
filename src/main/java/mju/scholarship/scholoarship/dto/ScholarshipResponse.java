package mju.scholarship.scholoarship.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
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
    private Integer price;
    private String category;
    private String university;
    private Integer age;
    private String gender; //enum 으로 할까
    private String city;
    private String department; // major -> 전공 (?)
    private Integer grade; // 1.0 - 4.5
    private Integer incomeQuantile; // 1-10

    @Builder
    public ScholarshipResponse(Long id,
                               Integer price,
                               String category,
                               String name,
                               String description,
                               String university,
                               Integer age,
                               String gender,
                               String city,
                               String department,
                               Integer grade,
                               Integer incomeQuantile) {
        this.id = id;
        this.price = price;
        this.category = category;
        this.name = name;
        this.description = description;
        this.university = university;
        this.age = age;
        this.gender = gender;
        this.city = city;
        this.department = department;
        this.grade = grade;
        this.incomeQuantile = incomeQuantile;
    }
}
