package mju.scholarship.member.entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    /**
     * 유저 관련 데이터
     */
    private String username;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    private String phone;

    private String university;

    private Integer age;
    private String gender; //enum 으로 할까
    private String province;
    private String city;
    private String department; // major -> 전공 (?)
    private String grade; // 학년
    private Integer incomeQuantile; // 1-10
    private boolean isFirstLogin = true;
    private int total = 0;
    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    public enum Role{
        ROLE_USER, ROLE_ADMIN
    }

    // 이미 받은 장학금
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGot> gotScholarships = new ArrayList<>();

    // 찜한 장학금
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberInterest> interestScholarships = new ArrayList<>();

    @Builder
    public Member(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public void updateFirstLogin(){
        isFirstLogin = false;
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void addGotScholarship(Scholarship scholarship) {
        MemberGot memberGot = MemberGot.builder()
                .member(this)
                .scholarship(scholarship)
                .build();

        gotScholarships.add(memberGot);
    }

    public void addTotal(){
        this.total ++;
    }

    public void createInfo(String nickname,
                           String university,
                           Integer age,
                           String gender,
                           String city,
                           String department,
                           String grade,
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
                           String province,
                           String city,
                           String department,
                           String grade,
                           Integer incomeQuantile) {
        this.nickname = nickname;
        this.phone = phone;
        this.university = university;
        this.age = age;
        this.gender = gender;
        this.province = province;
        this.city = city;
        this.department = department;
        this.grade = grade;
        this.incomeQuantile = incomeQuantile;
    }

}
