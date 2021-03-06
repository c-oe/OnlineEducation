package com.learn.coe.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.coe.service.edu.entity.Subject;
import com.learn.coe.service.edu.entity.excel.ExcelSubjectData;
import com.learn.coe.service.edu.entity.vo.SubjectVo;
import com.learn.coe.service.edu.listener.ExcelSubjectDataListener;
import com.learn.coe.service.edu.mapper.SubjectMapper;
import com.learn.coe.service.edu.service.SubjectService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {


    @Override
    public void batchImport(InputStream inputStream) {

        EasyExcel.read(inputStream, ExcelSubjectData.class,new ExcelSubjectDataListener(baseMapper))
                .excelType(ExcelTypeEnum.XLS)
                .sheet().doRead();
    }

    @Override
    public List<SubjectVo> nestedList() {
        return baseMapper.selectNestedListByParentId("0");
    }
}
