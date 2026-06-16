package com.example.blogserver.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 统一分页结构
 *
 * @param <T> 列表元素类型
 */
@Data
@Schema(description = "分页结果")
public class PageResult<T> implements Serializable {

    @Schema(description = "当前页数据列表")
    private List<T> records;

    @Schema(description = "总记录数", example = "100")
    private Long total;

    @Schema(description = "当前页码", example = "1")
    private Long current;

    @Schema(description = "每页大小", example = "10")
    private Long size;

    @Schema(description = "总页数", example = "10")
    private Long pages;

    public PageResult() {
    }

    public PageResult(List<T> records, Long total, Long current, Long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = size == 0 ? 0 : (total + size - 1) / size;
    }

    /**
     * 由 MyBatis-Plus 的 IPage 构建
     */
    public static <T> PageResult<T> of(IPage<T> page) {
        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 由 IPage 构建并转换记录类型
     */
    public static <T> PageResult<T> of(IPage<?> page, List<T> records) {
        return new PageResult<>(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    public static <T> PageResult<T> empty(Long current, Long size) {
        return new PageResult<>(Collections.emptyList(), 0L, current, size);
    }
}
