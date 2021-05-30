package com.learn.coe.service.cms.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author coffee
 * @since 2021-05-23 11:36
 */
@Data
public class AdVo implements Serializable {

    private static final long serialVersionUID=1L;
    private String id;
    private String title;//广告标题
    private Integer sort;//广告排序
    private String type;//广告位
}
