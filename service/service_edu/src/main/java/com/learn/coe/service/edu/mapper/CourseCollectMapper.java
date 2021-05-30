package com.learn.coe.service.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.coe.service.edu.entity.CourseCollect;
import com.learn.coe.service.edu.entity.vo.CourseCollectVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程收藏 Mapper 接口
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
@Repository
public interface CourseCollectMapper extends BaseMapper<CourseCollect> {

    List<CourseCollectVo> selectPageByMemberId(String memberId);
}
