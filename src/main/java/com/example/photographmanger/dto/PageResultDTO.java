package com.example.photographmanger.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "分页查询结果")
public class PageResultDTO<T> {
    @Schema(description = "当前页码", example = "1")
    private Integer number;
    @Schema(description = "每页的数量", example = "10")
    private Integer size;
    @Schema(description = "总记录数", example = "100")
    private Long totalElements;
    @Schema(description = "总页数", example = "10")
    private Integer totalPages;
    @Schema(description = "结果集")
    private List<T> content;
}
