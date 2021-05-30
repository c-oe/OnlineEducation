package com.learn.coe.service.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.coe.service.edu.entity.Subject;
import com.learn.coe.service.edu.entity.vo.SubjectVo;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
public interface SubjectService extends IService<Subject> {

    void batchImport(InputStream inputStream);

    List<SubjectVo> nestedList();

}
