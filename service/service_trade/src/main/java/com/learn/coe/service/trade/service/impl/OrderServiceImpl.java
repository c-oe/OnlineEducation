package com.learn.coe.service.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.learn.coe.common.base.result.ResultCodeEnum;
import com.learn.coe.service.base.dto.CourseDto;
import com.learn.coe.service.base.dto.MemberDto;
import com.learn.coe.service.base.exception.CoeException;
import com.learn.coe.service.trade.entity.Order;
import com.learn.coe.service.trade.entity.PayLog;
import com.learn.coe.service.trade.feign.EduCourseService;
import com.learn.coe.service.trade.feign.UcenterMemberService;
import com.learn.coe.service.trade.mapper.OrderMapper;
import com.learn.coe.service.trade.mapper.PayLogMapper;
import com.learn.coe.service.trade.service.OrderService;
import com.learn.coe.service.trade.util.OrderNoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-27
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @Autowired
    private PayLogMapper payLogMapper;


    @Override
    public String saveOrder(String courseId, String memberId) {

        //查询当前用户是否已有当前课程的订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("memberId",memberId);
        Order orderExist = baseMapper.selectOne(queryWrapper);
        if (orderExist != null){
            return orderExist.getId();//订单存在，直接返回订单id
        }

        //查询课程信息
        CourseDto courseDto = eduCourseService.getCourseDtoById(courseId);
        if (courseDto == null){
            throw new CoeException(ResultCodeEnum.PARAM_ERROR);
        }

        //查询用户信息
        MemberDto memberDto = ucenterMemberService.getMemberDtoByMemberId(memberId);
        if (memberDto == null){
            throw new CoeException(ResultCodeEnum.PARAM_ERROR);
        }


        //创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());//订单号
        order.setCourseId(courseId);
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        order.setTotalFee(courseDto.getPrice().multiply(new BigDecimal(100)));

        order.setMemberId(memberId);
        order.setMobile(memberDto.getMobile());
        order.setNickname(memberDto.getNickname());

        order.setStatus(0);//未支付
        order.setPayType(1);//微信支付

        baseMapper.insert(order);
        return order.getId();
    }

    @Override
    public boolean isBuyByCourseId(String courseId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId)
                .eq("member_id",memberId)
                .eq("status",1);

        Integer count = baseMapper.selectCount(queryWrapper);

        return count > 0;
    }

    @Override
    public List<Order> selectByMemberId(String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create")
                    .eq("member_id",memberId);

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean removeById(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId)
                .eq("member_id",memberId);
        return removeById(queryWrapper);
    }

    @Override
    public Order getByOrderId(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId)
                .eq("member_id",memberId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        return baseMapper.selectOne(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderStatus(Map<String, String> notifyMap) {

        //更新订单状态
        String outTradeNo = notifyMap.get("out_trade_no");
        Order order = getOrderByOrderNo(outTradeNo);
        order.setStatus(1);//支付成功
        baseMapper.updateById(order);

        //记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(outTradeNo);
        payLog.setPayTime(new Date());
        payLog.setPayType(1);
        payLog.setTotalFee(Long.valueOf(notifyMap.get("total_fee")));
        payLog.setTradeState(notifyMap.get("result_code"));
        payLog.setTransactionId(notifyMap.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(notifyMap));

        payLogMapper.insert(payLog);

        //更新课程销量
        eduCourseService.updateBuyCountById(order.getCourseId());
    }

    /**
     * 查询支付结果
     * @param orderNo 订单号
     * @return true 已支付，false 未支付
     */
    @Override
    public boolean queryPayStatus(String orderNo) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        Order order = baseMapper.selectOne(queryWrapper);
        return order.getStatus() == 1;
    }
}
