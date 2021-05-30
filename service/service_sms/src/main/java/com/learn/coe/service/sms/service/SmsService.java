package com.learn.coe.service.sms.service;

import com.aliyuncs.exceptions.ClientException;

/**
 * @author coffee
 * @since 2021-05-24 11:28
 */
public interface SmsService {
    void send(String mobile, String checkCode) throws ClientException;
}
