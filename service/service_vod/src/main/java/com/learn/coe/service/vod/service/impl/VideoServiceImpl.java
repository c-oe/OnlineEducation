package com.learn.coe.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.learn.coe.common.base.result.ResultCodeEnum;
import com.learn.coe.service.base.exception.CoeException;
import com.learn.coe.service.vod.service.VideoService;
import com.learn.coe.service.vod.util.AliyunVodSDKUtils;
import com.learn.coe.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;

/**
 * @author coffee
 * @since 2021-05-19 22:14
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VodProperties vodProperties;


    //上传视频
    @Override
    public String uploadVideo(InputStream inputStream, String originalFileName) {

        String title = originalFileName.substring(0, originalFileName.lastIndexOf("."));

        UploadStreamRequest request = new UploadStreamRequest(vodProperties.getKeyId(),
                vodProperties.getKeySecret(), title, originalFileName,inputStream);
        request.setTemplateGroupId(vodProperties.getTemplateGroupId());
        request.setWorkflowId(vodProperties.getWorkflowId());

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);


        String videoId = response.getVideoId();
        if (StringUtils.hasLength(videoId)){
            log.error("阿里云上传失败：{} - {}" + response.getCode(),response.getMessage());
            throw new CoeException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }

        return null;
    }

    //删除视频
    @Override
    public void removeVideo(String videoId) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyId(), vodProperties.getKeySecret());

        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoId);
        client.getAcsResponse(request);
    }

    @Override
    public void removeVideoByIdList(List<String> vodIdList) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyId(), vodProperties.getKeySecret());

        DeleteVideoRequest request = new DeleteVideoRequest();

        int size = vodIdList.size();
        StringBuilder idListStr = new StringBuilder();
        for (int i = 0; i < size; i++) {

            idListStr.append(vodIdList.get(i));
            if (i == size - 1 || i % 20 == 19){
                //删除
                //支持传入多个视频Id，多个用逗号分隔,id不能超过20个
                log.info("idListStr = {}",idListStr.toString());
                request.setVideoIds(idListStr.toString());
                client.getAcsResponse(request);
                //重置idListStr
                idListStr = new StringBuilder();
            }else if (i % 20 < 19){
                idListStr.append(",");
            }
        }


    }

    @Override
    public String getPlayAuth(String videoSourceId) throws ClientException {

        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyId(), vodProperties.getKeySecret());

        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoSourceId);

        GetVideoPlayAuthResponse response = client.getAcsResponse(request);

        return response.getPlayAuth();
    }

}
