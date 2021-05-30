package com.learn.coe.service.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.coe.service.edu.entity.Video;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
public interface VideoService extends IService<Video> {

    void removeMediaVideoById(String id);

    void removeMediaVideoByChapterId(String chapterId);

    void removeMediaVideoByCourseId(String courseId);
}
