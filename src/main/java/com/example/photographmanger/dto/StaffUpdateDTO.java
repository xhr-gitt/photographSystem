package com.example.photographmanger.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class StaffUpdateDTO {
    @NotBlank(message = "角色不能为空")
    @Size(max = 20, message = "角色长度不能超过20个字符")
    private String role;

    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;

    @Size(max = 500, message = "员工信息长度不能超过500个字符")
    private String staffInformation;

    @Size(max = 20, message = "用户名长度不能超过20个字符")
    private String username;

}