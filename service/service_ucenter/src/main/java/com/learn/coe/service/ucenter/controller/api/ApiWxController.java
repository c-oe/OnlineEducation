package com.learn.coe.service.ucenter.controller.api;

import com.google.gson.Gson;
import com.learn.coe.common.base.result.ResultCodeEnum;
import com.learn.coe.common.base.util.ExceptionUtils;
import com.learn.coe.common.base.util.HttpClientUtils;
import com.learn.coe.common.base.util.JwtInfo;
import com.learn.coe.common.base.util.JwtUtils;
import com.learn.coe.service.base.exception.CoeException;
import com.learn.coe.service.ucenter.entity.Member;
import com.learn.coe.service.ucenter.service.MemberService;
import com.learn.coe.service.ucenter.util.UcenterProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author coffee
 * @since 2021-05-26 16:44
 */
@Api("会员管理")
//@CrossOrigin
@Slf4j
@Controller
@RequestMapping("/api/ucenter/wx")
public class ApiWxController {

    @Autowired
    private UcenterProperties ucenterProperties;

    @Autowired
    private MemberService memberService;

    @GetMapping("login")
    public String genQrConnect(HttpSession session){
        //组装url
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //生成随机state，防止csrf攻击
        String state = UUID.randomUUID().toString();
        //将state存入session中
        session.setAttribute("wx_open_state",state);

        String redirectUri = URLEncoder.encode(ucenterProperties.getRedirectUri(), StandardCharsets.UTF_8);

        String qeCodeUrl = String.format(baseUrl,
                ucenterProperties.getAppId(),
                redirectUri,
                state);


        //跳转
        return "redirect:" + qeCodeUrl;
    }

    @GetMapping("callback")
    public String callBack(String code,String state,HttpSession session){

        if (!StringUtils.hasLength(code) || !StringUtils.hasLength(state)){
            log.error("非法回调请求");
            throw new CoeException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        String sessionState = (String) session.getAttribute("wx_open_state");
        if (!state.equals(sessionState)){
            log.error("非法回调请求");
            throw new CoeException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //携带临时code,appId,appSecret请求access_token
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        //组装参数
        Map<String, String> accessTokenParam = new HashMap<>();
        accessTokenParam.put("appid",ucenterProperties.getAppId());
        accessTokenParam.put("secret",ucenterProperties.getAppSecret());
        accessTokenParam.put("code",code);
        accessTokenParam.put("grant_type","authorization_code");

        HttpClientUtils client = new HttpClientUtils(accessTokenUrl, accessTokenParam);

        //组装完成的url,发送请求
        String result;
        try {
            client.get();
            result = client.getContent();
        } catch (Exception e) {
            log.error("获取access_token失败");
            throw new CoeException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        Gson gson = new Gson();
        HashMap<String,Object> resultMap = gson.fromJson(result, HashMap.class);

        //失败的响应结果
        Object errcodeObj = resultMap.get("errcode");
        if (errcodeObj != null){
            Double errcode = (Double) errcodeObj;
            String errmsg = (String) resultMap.get("errmsg");

            log.error("获取access_token失败:code-{}--message{}",errcode,errmsg);
            throw new CoeException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        //解析出结果中的access_token和openid
        String accessToken = (String) resultMap.get("access_token");
        String openid = (String) resultMap.get("openid");


        //在本地数据库中插入当前微信用户的信息
        Member member = memberService.getByOpenid(openid);
        if (member == null){
            //不存在，去微信的资源服务器获取用户个人信息(携带access_token)
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
            Map<String, String> baseUserInfoParam = new HashMap<>();
            baseUserInfoParam.put("access_token",accessToken);
            baseUserInfoParam.put("openid",openid);

            client = new HttpClientUtils(baseUserInfoUrl,baseUserInfoParam);
            String resultUserInfo;
            try {
                client.get();
                resultUserInfo = client.getContent();
            } catch (Exception e) {
                log.error(ExceptionUtils.getMessage(e));
                throw new CoeException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            HashMap<String,Object> resultInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
            errcodeObj = resultInfoMap.get("errcode");
            if (errcodeObj != null){
                Double errcode = (Double) errcodeObj;
                String errmsg = (String) resultMap.get("errmsg");

                log.error("获取用户信息失败:code-{}--message{}",errcode,errmsg);
                throw new CoeException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            //解析出结果中的用户个人信息
            String nickname = (String)resultInfoMap.get("nickname");
            String avatar = (String)resultInfoMap.get("headimgurl");
            Double sex = (Double)resultInfoMap.get("sex");

            //在本地数据库中插入当前微信用户的信息(使用微信账号在本地服务器注册新用户)
            member= new Member();
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(avatar);
            member.setSex(sex.intValue());

            memberService.save(member);
        }

        //使用当前用户的信息登录(生成jwt)
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(member.getId());
        jwtInfo.setNickname(member.getNickname());
        jwtInfo.setAvatar(member.getAvatar());

        String jwtToken = JwtUtils.getJwtToken(jwtInfo, 1800);

        return "redirect:http://localhost:3000?token=" + jwtToken;
    }
}
