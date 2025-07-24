package com.example.photographmanger.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BorrowRecordDTO {
    @NotNull(message = "设备ID不能为空")
    private Long equipmentId;

    @NotNull(message = "员工ID不能为空")
    private Long staffId;

    @NotBlank(message = "借用目的不能为空")
    @Size(max = 500, message = "借用目的长度不能超过500个字符")
    private String purpose;
}