package com.learn.coe.service.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.coe.service.edu.entity.CourseCollect;
import com.learn.coe.service.edu.entity.vo.CourseCollectVo;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
public interface CourseCollectService extends IService<CourseCollect> {

    /**
     * 判断是否收藏
     * @param courseId 课程id
     * @param memberId 用户id
     */
    boolean isCollect(String courseId, String memberId);

    /**
     * 收藏课程
     * @param courseId 课程id
     * @param memberId 用户id
     */
    void saveCourseCollect(String courseId, String memberId);

    /**
     * 课程列表
     * @param memberId 用户id
     * @return 课程列表
     */
    List<CourseCollectVo> selectListByMemberId(String memberId);

    /**
     * 取消收藏
     * @param courseId 课程id
     * @param memberId 用户id
     * @return 删除是否成功
     */
    boolean removeCourseCollect(String courseId, String memberId);
}
