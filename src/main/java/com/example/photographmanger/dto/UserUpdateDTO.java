// UserUpdateDTO.java
package com.example.photographmanger.dto;

import lombok.Data;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

@Data
public class UserUpdateDTO {
    @Size(min = 4, max = 50, message = "用户名长度必须在4到50个字符之间")
    private String username;

    @Size(min = 6, max = 100, message = "密码长度必须在6到100个字符之间")
    private String password;

    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;

    private boolean enabled = true;

    @Pattern(regexp = "^[0-9]{11}$", message = "手机号必须是11位数字")
    private String phone;

    private String role;  // ADMIN, PHOTOGRAPHER, CUSTOMER_SERVICE
}