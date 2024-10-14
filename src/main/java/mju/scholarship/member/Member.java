package mju.scholarship.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.scholarship.scholoarship.Scholarship;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 유저 관련 데이터
     */
    private String username;
    private String email;
    private String phone;
    private String password;

    /**
     * 장학금 조건
     */
    private String university;
    private Integer age;
    private String gender; //enum 으로 할까
    private String city;
    private String department; // major -> 전공 (?)
    private Integer grade; // 1.0 - 4.5
    private Integer incomeQuantile; // 1-10

    @OneToMany(fetch = FetchType.LAZY)
    private List<Scholarship> gotScholarships = new ArrayList<>();

    @Builder
    public Member(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public void addGotScholarship(Scholarship scholarship) {
        gotScholarships.add(scholarship);
    }

    public void createInfo(String university,
                           Integer age,
                           String gender,
                           String city,
                           String department,
                           Integer grade,
                           Integer incomeQuantile) {
        this.university = university;
        this.age = age;
        this.gender = gender;
        this.city = city;
        this.department = department;
        this.grade = grade;
        this.incomeQuantile = incomeQuantile;
    }

}
