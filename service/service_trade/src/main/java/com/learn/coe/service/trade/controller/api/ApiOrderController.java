package com.learn.coe.service.trade.controller.api;


import com.learn.coe.common.base.result.R;
import com.learn.coe.common.base.result.ResultCodeEnum;
import com.learn.coe.common.base.util.JwtInfo;
import com.learn.coe.common.base.util.JwtUtils;
import com.learn.coe.service.trade.entity.Order;
import com.learn.coe.service.trade.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author Coffee
 * @since 2021-05-27
 */
@RestController
@RequestMapping("/api/trade/order")
@Api("网站订单管理")
//@CrossOrigin
@Slf4j
public class ApiOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("新增订单")
    @PostMapping("auth/save/{courseId}")
    public R save(@PathVariable String courseId, HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        String orderId = orderService.saveOrder(courseId,jwtInfo.getId());
        return R.ok().data("orderId",orderId);
    }

    @ApiOperation("获取订单")
    @GetMapping("auth/get/{orderId}")
    public R get(@PathVariable String orderId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        Order order = orderService.getByOrderId(orderId, jwtInfo.getId());
        return R.ok().data("item", order);
    }

    @ApiOperation("判断课程是否购买")
    @GetMapping("auth/is-buy/{courseId}")
    public R isBuyByCourseId(@PathVariable String courseId, HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean isBuy = orderService.isBuyByCourseId(courseId,jwtInfo.getId());
        return R.ok().data("isBuy",isBuy);
    }

    @ApiOperation("获取当前用户订单列表")
    @GetMapping("auth/list")
    public R list(HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        List<Order> list = orderService.selectByMemberId(jwtInfo.getId());
        return R.ok().data("items",list);
    }

    @ApiOperation("删除订单")
    @DeleteMapping ("auth/remove/{orderId}")
    public R remove(@PathVariable String orderId, HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean remove = orderService.removeById(orderId, jwtInfo.getId());
        if (remove){
            return R.ok().message("删除成功");
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("查询订单支付状态")
    @GetMapping  ("query-pay-status/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){

        boolean status = orderService.queryPayStatus(orderNo);
        if (status) {
            return R.ok().message("支付成功");
        }
        return R.setResult(ResultCodeEnum.PAY_RUN);
    }
}

