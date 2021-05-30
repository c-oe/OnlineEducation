package com.learn.coe.service.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.coe.service.trade.entity.Order;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-27
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String memberId);

    boolean isBuyByCourseId(String courseId, String memberId);

    List<Order> selectByMemberId(String memberId);

    boolean removeById(String orderId, String memberId);

    Order getByOrderId(String orderId, String memberId);

    Order getOrderByOrderNo(String orderNo);

    void updateOrderStatus(Map<String, String> notifyMap);

    boolean queryPayStatus(String orderNo);
}
