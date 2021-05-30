package com.learn.coe.service.trade.controller.api;


import com.github.wxpay.sdk.WXPayUtil;
import com.learn.coe.common.base.result.R;
import com.learn.coe.service.trade.entity.Order;
import com.learn.coe.service.trade.service.OrderService;
import com.learn.coe.service.trade.service.WeixinPayService;
import com.learn.coe.service.trade.util.StreamUtils;
import com.learn.coe.service.trade.util.WeixinPayProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author helen
 * @since 2020/5/6
 */
@RestController
@RequestMapping("/api/trade/weixin-pay")
@Api("网站微信支付")
//@CrossOrigin //跨域
@Slf4j
public class ApiWeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private WeixinPayProperties weixinPayProperties;

    @Autowired
    private OrderService orderService;

    /**
     * 统一下单接口的调用
     */
    @GetMapping("create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request){
        String remoteAddr = request.getRemoteAddr();
        Map<String, Object> map = weixinPayService.createNative(orderNo, remoteAddr);
        return R.ok().data(map);
    }

    @PostMapping("callback/notify")
    public String wxNotify(HttpServletRequest request, HttpServletResponse response)throws Exception{

        ServletInputStream inputStream = request.getInputStream();
        String notifyXml = StreamUtils.inputStream2String(inputStream, "utf-8");

        //校验签名
        if (WXPayUtil.isSignatureValid(notifyXml,weixinPayProperties.getPartnerKey())){

            //解析返回结果
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyXml);

            //判断支付是否成功
            if ("SUCCESS".equals(notifyMap.get("result_code"))){

                //金额校验
                String totalFee = notifyMap.get("total_fee");//支付结果的订单金额
                Order order = orderService.getOrderByOrderNo(notifyMap.get("out_trade_no"));//查询本地订单
                if(order != null && order.getTotalFee().intValue() == Integer.parseInt(totalFee)){

                    //无论接口被调用多少次，最后所影响的结果都是一致的（幂等性）
                    if (order.getStatus() != 1){
                        //更新订单状态
                        orderService.updateOrderStatus(notifyMap);
                    }

                    //支付成功，给微信发送响应
                    //创建响应对象
                    HashMap<String, String> returnMap = new HashMap<>();
                    returnMap.put("return_code","SUCCESS");
                    returnMap.put("return_msg","OK");

                    String returnXml = WXPayUtil.mapToXml(returnMap);
                    response.setContentType("text/xml");

                    //应答
                    return returnXml;
                }
            }
        }
        //创建响应对象
        HashMap<String, String> returnMap = new HashMap<>();
        returnMap.put("return_code","FAIL");
        returnMap.put("return_msg","");

        String returnXml = WXPayUtil.mapToXml(returnMap);
        response.setContentType("text/xml");

        //应答
        return returnXml;
    }



}
