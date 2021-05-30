package com.learn.coe.service.ucenter.controller.api;


import com.learn.coe.common.base.result.R;
import com.learn.coe.common.base.result.ResultCodeEnum;
import com.learn.coe.common.base.util.JwtInfo;
import com.learn.coe.common.base.util.JwtUtils;
import com.learn.coe.service.base.dto.MemberDto;
import com.learn.coe.service.base.exception.CoeException;
import com.learn.coe.service.ucenter.entity.vo.LoginVo;
import com.learn.coe.service.ucenter.entity.vo.RegisterVo;
import com.learn.coe.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Coffee
 * @since 2021-05-24
 */
@Api("会员管理")
//@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/ucenter/member")
public class ApiMemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation("会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){

        memberService.register(registerVo);
        return R.ok().message("注册成功");
    }


    @ApiOperation("会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){
        String token = memberService.login(loginVo);
        return R.ok().data("token",token).message("登录成功");
    }

    @ApiOperation("根据token获取登录信息")
    @GetMapping("get-login-info")
    public R getLoginInfo(HttpServletRequest request){

        try {
            JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
            return R.ok().data("userInfo",jwtInfo);
        } catch (Exception e) {
            log.error("解析用户信息失败：{}",e.getMessage());
            throw new CoeException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
    }

    @ApiOperation("根据会员ID查询会员信息")
    @GetMapping("inner/get-member-dto/{memberId}")
    public MemberDto getMemberDtoByMemberId(@ApiParam(value = "会员ID",required = true)@PathVariable String memberId){
        return memberService.getMemberDtoByMemberId(memberId);
    }
}
