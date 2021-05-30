package com.learn.coe.service.edu.entity.vo;

import lombok.Data;

/**
 * @author coffee
 * @since 2021-05-21 15:57
 */
@Data
public class WebCourseQueryVo {

    private String subjectParentId;
    private String subjectId;
    private String buyCountSort;
    private String gmtCreateSort;
    private String priceSort;

    private Integer type;
}
