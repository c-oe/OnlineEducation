package com.learn.coe.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.coe.common.base.result.R;
import com.learn.coe.service.edu.entity.Course;
import com.learn.coe.service.edu.entity.Teacher;
import com.learn.coe.service.edu.entity.vo.TeacherQueryVo;
import com.learn.coe.service.edu.feign.OSSFileService;
import com.learn.coe.service.edu.mapper.CourseMapper;
import com.learn.coe.service.edu.mapper.TeacherMapper;
import com.learn.coe.service.edu.service.TeacherService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Resource
    private OSSFileService ossFileService;

    @Resource
    private CourseMapper courseMapper;

    @Override
    public IPage<Teacher> selectPage(Page<Teacher> teacherPage, TeacherQueryVo teacherQueryVo) {

        //显示分页查询列表
            //1.排序：按照sort字段排序

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
            //2.分页查询
        if (teacherQueryVo == null){
            return baseMapper.selectPage(teacherPage,queryWrapper);
        }
            //3.条件查询
        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();

        if (!StringUtils.hasLength(name)){
            queryWrapper.likeRight("name",name);
        }

        if (level != null){
            queryWrapper.eq("level",level);
        }

        if (!StringUtils.hasLength(joinDateBegin)){
            queryWrapper.ge("join_date",joinDateBegin);
        }

        if (!StringUtils.hasLength(joinDateEnd)){
            queryWrapper.le("join_date",joinDateEnd);
        }

        return baseMapper.selectPage(teacherPage,queryWrapper);
    }

    @Override
    public List<Map<String, Object>> selectNameList(String key) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name");
        queryWrapper.likeRight("name",key);
        return baseMapper.selectMaps(queryWrapper);
    }

    @Override
    public boolean removeAvatarById(String id) {

        //根据id获取讲师avatar的url
        Teacher teacher = baseMapper.selectById(id);
        if (teacher != null){
            String avatar = teacher.getAvatar();
            if (StringUtils.hasLength(avatar)){
                R r = ossFileService.removeFile(avatar);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> selectTeacherInfoById(String id) {

        Teacher teacher = baseMapper.selectById(id);

        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("teacher_id",id);
        List<Course> courses = courseMapper.selectList(courseQueryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("teacher",teacher);
        map.put("courseList",courses);

        return map;
    }

    @Cacheable(value = "index", key = "'selectHotTeacher'")
    @Override
    public List<Teacher> selectHotTeacher() {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort");
        queryWrapper.last("limit 4");

        return baseMapper.selectList(queryWrapper);
    }
}
