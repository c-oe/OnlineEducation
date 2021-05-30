package com.learn.coe.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author coffee
 * @Date 2021-05-18 15:50
 */
@Data
public class CourseQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String teacherId;
    private String subjectParentId;
    private String subjectId;
}
