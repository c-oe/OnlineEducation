package com.learn.coe.service.sms.controller.api;

import com.aliyuncs.exceptions.ClientException;
import com.learn.coe.common.base.result.R;
import com.learn.coe.common.base.result.ResultCodeEnum;
import com.learn.coe.common.base.util.FormUtils;
import com.learn.coe.common.base.util.RandomUtils;
import com.learn.coe.service.base.exception.CoeException;
import com.learn.coe.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author coffee
 * @since 2021-05-24 11:26
 */
@RestController
@RequestMapping("/api/sms")
@Api("短信管理")
//@CrossOrigin
@Slf4j
public class ApiSmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("send/{mobile}")
    public R getCode(@PathVariable String mobile) throws ClientException {
        //校验手机号是否合法
        if (StringUtils.hasLength(mobile) || !FormUtils.isMobile(mobile)){
            log.error("手机号不正确");
            throw new CoeException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }

        //生成验证码
        String checkCode = RandomUtils.getFourBitRandom();

        //发送验证码
        smsService.send(mobile,checkCode);

        //存储验证码到redis
        redisTemplate.opsForValue().set(mobile,checkCode,5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");
    }
}
