package mju.scholarship.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponse {

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


}
