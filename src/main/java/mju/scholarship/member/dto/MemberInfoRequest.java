package mju.scholarship.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoRequest {

    private String username;
    private String university;
    private Integer age;
    private String gender; //enum 으로 할까
    private String city;
    private String department; // major -> 전공 (?)
    private Integer grade; // 1.0 - 4.5
    private Integer incomeQuantile; // 1-10

}
