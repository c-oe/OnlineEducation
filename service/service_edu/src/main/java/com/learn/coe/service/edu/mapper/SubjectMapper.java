package com.learn.coe.service.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.coe.service.edu.entity.Subject;
import com.learn.coe.service.edu.entity.vo.SubjectVo;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    List<SubjectVo> selectNestedListByParentId(String parentId);
}
