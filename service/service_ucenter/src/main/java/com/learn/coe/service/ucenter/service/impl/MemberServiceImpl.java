package com.learn.coe.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learn.coe.common.base.result.ResultCodeEnum;
import com.learn.coe.common.base.util.FormUtils;
import com.learn.coe.common.base.util.JwtInfo;
import com.learn.coe.common.base.util.JwtUtils;
import com.learn.coe.common.base.util.MD5;
import com.learn.coe.service.base.dto.MemberDto;
import com.learn.coe.service.base.exception.CoeException;
import com.learn.coe.service.ucenter.entity.Member;
import com.learn.coe.service.ucenter.entity.vo.LoginVo;
import com.learn.coe.service.ucenter.entity.vo.RegisterVo;
import com.learn.coe.service.ucenter.mapper.MemberMapper;
import com.learn.coe.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-24
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void register(RegisterVo registerVo) {
        //校验参数
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        if (!StringUtils.hasLength(mobile) || FormUtils.isMobile(mobile)){
            throw new CoeException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }

        if (!StringUtils.hasLength(nickname) || !StringUtils.hasLength(password)
                || !StringUtils.hasLength(code)){
            throw new CoeException(ResultCodeEnum.PARAM_ERROR);
        }

        //校验验证码：redis
        String checkCode = (String) redisTemplate.opsForValue().get(mobile);
        if (!code.equals(checkCode)){
            throw new CoeException(ResultCodeEnum.CODE_ERROR);
        }

        //用户是否被注册:mysql
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);

        if (count > 0){
            throw new CoeException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        //注册
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setAvatar("https://coe-glfile.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        member.setDisabled(false);
        baseMapper.insert(member);
    }

    @Override
    public String login(LoginVo loginVo) {

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //校验参数是否合法
        if (!StringUtils.hasLength(mobile) || FormUtils.isMobile(mobile) || !StringUtils.hasLength(password)){
            throw new CoeException(ResultCodeEnum.PARAM_ERROR);
        }

        //校验手机号是否存在
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Member member = baseMapper.selectOne(queryWrapper);
        if(member == null){
            throw new CoeException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }

        //校验密码是否正确
        if (!MD5.encrypt(password).equals(member.getPassword())){
            throw new CoeException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        //校验用户是否被禁用
        if (member.getDisabled()){
            throw new CoeException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //登录：生成token
        JwtInfo info = new JwtInfo();
        info.setId(member.getId());
        info.setNickname(member.getNickname());
        info.setAvatar(member.getAvatar());

        return JwtUtils.getJwtToken(info,1800);
    }

    @Override
    public Member getByOpenid(String openid) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        Member member = baseMapper.selectById(memberId);
        MemberDto memberDto = new MemberDto();

        BeanUtils.copyProperties(member,memberDto);
        return memberDto;
    }

    @Override
    public Integer countRegisterNum(String day) {

        return baseMapper.selectRegisterNumByDay(day);
    }
}
