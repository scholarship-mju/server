package mju.scholarship.member.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UpdateMemberInfoRequest {

    private String nickname;
    private String phone;
    /**
     * 장학금 조건
     */
    private String university;
    private Integer age;
    private String gender; //enum 으로 할까
    private String province;
    private String city;
    private String department; // major -> 전공 (?)
    private String grade;
    private Integer incomeQuantile; // 1-10
}
