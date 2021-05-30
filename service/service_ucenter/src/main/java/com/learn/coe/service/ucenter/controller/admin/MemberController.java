package com.learn.coe.service.ucenter.controller.admin;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author coffee
 * @since 2021-05-29 13:59
 */
@Api("会员管理")
@RestController
@RequestMapping("/admin/ucenter/member")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation("根据日期统计注册人数")
    @GetMapping("count-register-num/{day}")
    public R countRegisterNum(@ApiParam(value = "日期",required = true) @PathVariable String day){
        Integer num = memberService.countRegisterNum(day);
        return R.ok().data("num",num);
    }
}
