package com.learn.coe.service.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.coe.service.edu.entity.Chapter;
import com.learn.coe.service.edu.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
public interface ChapterService extends IService<Chapter> {

    boolean removeChapterById(String id);

    List<ChapterVo> nestedList(String courseId);
}
