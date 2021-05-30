package com.learn.coe.service.ucenter.service;

import com.learn.coe.service.base.dto.MemberDto;
import com.learn.coe.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.coe.service.ucenter.entity.vo.LoginVo;
import com.learn.coe.service.ucenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-24
 */
public interface MemberService extends IService<Member> {

    void register(RegisterVo registerVo);

    String login(LoginVo loginVo);

    Member getByOpenid(String openid);

    MemberDto getMemberDtoByMemberId(String memberId);

    Integer countRegisterNum(String day);
}
