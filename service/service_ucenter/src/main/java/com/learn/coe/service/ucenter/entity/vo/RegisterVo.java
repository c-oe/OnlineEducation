package com.learn.coe.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author coffee
 * @since 2021-05-24 16:14
 */
@Data
public class RegisterVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nickname;
    private String mobile;
    private String password;
    private String code;
}