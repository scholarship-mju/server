package mju.scholarship.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupDto {

    private String username;
    private String password;
    private String email;
    private String phone;

}
