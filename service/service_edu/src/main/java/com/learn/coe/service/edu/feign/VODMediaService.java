package com.learn.coe.service.edu.feign;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.edu.feign.fallback.VODFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author coffee
 * @since 2021-05-20 13:04
 */
@FeignClient(value = "service-vod",fallback = VODFileServiceFallBack.class)
public interface VODMediaService {

    @DeleteMapping("admin/vod/media/remove/{vodId}")
    R removeVideo(@PathVariable("vodId") String vodId);

    @DeleteMapping("admin/vod/media/remove")
    R removeVideoByIdList(@RequestBody List<String> vodIdList);
}
