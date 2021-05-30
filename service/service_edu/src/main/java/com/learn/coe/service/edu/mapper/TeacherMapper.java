package com.learn.coe.service.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.coe.service.edu.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 讲师 Mapper 接口
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
@Repository
public interface TeacherMapper extends BaseMapper<Teacher> {

}
