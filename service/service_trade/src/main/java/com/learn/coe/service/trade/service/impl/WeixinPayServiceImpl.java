package com.learn.coe.service.trade.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.learn.coe.common.base.result.ResultCodeEnum;
import com.learn.coe.common.base.util.ExceptionUtils;
import com.learn.coe.common.base.util.HttpClientUtils;
import com.learn.coe.service.base.exception.CoeException;
import com.learn.coe.service.trade.entity.Order;
import com.learn.coe.service.trade.service.OrderService;
import com.learn.coe.service.trade.service.WeixinPayService;
import com.learn.coe.service.trade.util.WeixinPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author coffee
 * @since 2021-05-28 16:35
 */
@Service
@Slf4j
public class WeixinPayServiceImpl implements WeixinPayService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeixinPayProperties weixinPayProperties;
    @Override
    public Map<String, Object> createNative(String orderNo, String remoteAddr) {
        try {
            //根据订单号获取订单
            Order order = orderService.getOrderByOrderNo(orderNo);

            //调用微信统一下单api
            HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //组装参数
            Map<String, String> params = new HashMap<>();
            params.put("appid",weixinPayProperties.getAppId());//appid
            params.put("mch_id",weixinPayProperties.getPartner());//商户号
            params.put("nonce_str", WXPayUtil.generateNonceStr());//随机数
            params.put("body",order.getCourseTitle());//商品描述
            params.put("out_trade_no",orderNo);//商家订单号
            params.put("total_fee",order.getTotalFee().intValue() + "");//订单总金额(分)
            params.put("spbill_create_ip", remoteAddr);//终端IP
            params.put("notify_url",weixinPayProperties.getNotifyUrl());//回调地址
            params.put("trade_type","NATIVE");//交易类型

            //转换为xml字符串,并在字符串最后增加计算出的签名
            String xml = WXPayUtil.generateSignedXml(params, weixinPayProperties.getPartnerKey());

            //将参数放入请求对象的方法体
            client.setXmlParam(xml);
            //使用https,post发送请求
            client.setHttps(true);
            client.post();
            //得到响应
            String resultXml = client.getContent();
            //转换为map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);

            //失败处理
            if ("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code"))){
                log.error("微信支付统一下单错误 - return_code:{},return_msg:{},result_code:{},err_code:{},err_code_des:{}",
                        resultMap.get("return_code"),resultMap.get("return_msg"),resultMap.get("result_code"),
                        resultMap.get("err_code"),resultMap.get("err_code_des"));

                throw new CoeException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }

            //成功,封装结果对象
            Map<String, Object> map = new HashMap<>();
            map.put("result_code",resultMap.get("result_code"));//交易标识
            map.put("code_url",resultMap.get("code_url"));//二维码url
            map.put("course_id",order.getCourseId());//课程id
            map.put("total_fee",order.getTotalFee());//订单金额
            map.put("order_trade_no",orderNo);//订单号

            return map;
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new CoeException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }
    }
}
