package com.learn.coe.service.edu.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coffee
 * @since 2021-05-19 14:33
 */
@Data
public class ChapterVo {

    private String id;
    private String title;
    private String sort;
    private List<VideoVo> children = new ArrayList<>();
}
