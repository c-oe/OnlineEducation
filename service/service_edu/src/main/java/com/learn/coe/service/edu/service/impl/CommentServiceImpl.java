package com.learn.coe.service.edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.coe.service.edu.entity.Comment;
import com.learn.coe.service.edu.mapper.CommentMapper;
import com.learn.coe.service.edu.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
