package mju.scholarship.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateNewUserRequest {

    private String nickname;
    private String phone;
    /**
     * 장학금 조건
     */
    private String university;
    private Integer age;
    private String gender; //enum 으로 할까
    private String city;
    private String department; // major -> 전공 (?)
    private Double grade; // 1.0 - 4.5
    private Integer incomeQuantile; // 1-10
}
