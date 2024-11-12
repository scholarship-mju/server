package mju.scholarship.member;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "member_id")
    private Long id;

    /**
     * 유저 관련 데이터
     */
    private String username;
    private String nickname;

    @Column(unique = true)
    private String email;

    private String phone;

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
    private boolean isFirstLogin = true;

    // 이미 받은 장학금
    @OneToMany(fetch = FetchType.LAZY)
    private List<Scholarship> gotScholarships = new ArrayList<>();

    // 찜한 장학금
    @OneToMany(fetch = FetchType.LAZY)
    private List<Scholarship> interestScholarships = new ArrayList<>();

    @Builder
    public Member(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public void updateFirstLogin(){
        isFirstLogin = false;
    }


    public void addInterestScholarship(Scholarship scholarship) {
        interestScholarships.add(scholarship);
    }

    public void deleteInterestScholarship(Scholarship scholarship) {
        interestScholarships.remove(scholarship);
    }

    public void addGotScholarship(Scholarship scholarship) {
        gotScholarships.add(scholarship);
    }

    public void deleteGotScholarship(Scholarship scholarship) {
        gotScholarships.remove(scholarship);
    }

    public void createInfo(String nickname,
                           String university,
                           Integer age,
                           String gender,
                           String city,
                           String department,
                           Integer grade,
                           Integer incomeQuantile) {
        this.nickname = nickname;
        this.university = university;
        this.age = age;
        this.gender = gender;
        this.city = city;
        this.department = department;
        this.grade = grade;
        this.incomeQuantile = incomeQuantile;
    }

    public void updateInfo(String nickname,
                           String phone,
                           String university,
                           Integer age,
                           String gender,
                           String city,
                           String department,
                           Integer grade,
                           Integer incomeQuantile) {
        this.nickname = nickname;
        this.phone = phone;
        this.university = university;
        this.age = age;
        this.gender = gender;
        this.city = city;
        this.department = department;
        this.grade = grade;
        this.incomeQuantile = incomeQuantile;
    }

}
