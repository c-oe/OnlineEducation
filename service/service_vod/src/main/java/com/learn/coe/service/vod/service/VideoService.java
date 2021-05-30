package com.learn.coe.service.vod.service;

import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.List;

/**
 * @author coffee
 * @since 2021-05-19 22:13
 */
public interface VideoService {
    String uploadVideo(InputStream inputStream,String originalFileName);

    void removeVideo(String videoId) throws ClientException;

    void removeVideoByIdList(List<String> vodIdList) throws ClientException;

    String getPlayAuth(String videoSourceId) throws ClientException;
}
