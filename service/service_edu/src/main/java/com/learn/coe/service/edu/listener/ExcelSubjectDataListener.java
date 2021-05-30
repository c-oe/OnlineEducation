package com.learn.coe.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learn.coe.service.edu.entity.Subject;
import com.learn.coe.service.edu.entity.excel.ExcelSubjectData;
import com.learn.coe.service.edu.mapper.SubjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author coffee
 * @Date 2021-05-17 16:14
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    private SubjectMapper subjectMapper;

    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext context) {
        log.info("解析到一条记录：{}",data);
        //处理读取出来的数据
        String levelOneTitle = data.getLevelOneTitle();
        String levelTwoTitle = data.getLevelTwoTitle();
        log.info("levelOneTitle:{}",levelOneTitle);
        log.info("levelTwoTitle:{}",levelTwoTitle);


        //判断数据是否存在
        Subject subjectLevelOne = getByTitle(levelOneTitle);
        String parentId;
        if (subjectLevelOne == null){
            //组装数据，存入数据库
            Subject subject = new Subject();
            subject.setParentId("0");
            subject.setTitle(levelOneTitle);

            subjectMapper.insert(subject);
            parentId = subject.getParentId();
        }else {
            parentId = subjectLevelOne.getId();
        }

        //判断数据是否存在
        Subject subjectLevelTwo = getSubByTitle(levelTwoTitle, parentId);
        if (subjectLevelTwo == null){
            Subject subject = new Subject();
            //组装二级类别
            subject.setTitle(levelTwoTitle);
            subject.setParentId(parentId);
            //存入数据库
            subjectMapper.insert(subject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("数据解析完成");
    }

    /**
     * 根据一级分类的名称查询数据是否存在
     * @param title 一级分类名称
     * @return 是否存在
     */
    private Subject getByTitle(String title){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id","0");
        return subjectMapper.selectOne(queryWrapper);
    }

    /**
     * 根据分类的名称和父ID查询数据是否存在
     * @param title 一级分类名称
     * @param parentId 父ID
     * @return 是否存在
     */
    private Subject getSubByTitle(String title,String parentId){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id",parentId);
        return subjectMapper.selectOne(queryWrapper);
    }
}
