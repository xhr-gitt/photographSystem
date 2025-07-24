package com.example.photographmanger.dto;


import lombok.Data;

@Data
public class PageParamDTO {
    // 页面大小
    Integer pageSize;

    // 页码
    Integer pageNum;

    // 根据什么进行排序
    String sortBy;

    // 排序规则 AES DESC
    String order;
}
