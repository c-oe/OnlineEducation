package com.learn.coe.service.cms.controller.api;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.cms.entity.Ad;
import com.learn.coe.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author coffee
 * @since 2021-05-23 14:17
 */
//@CrossOrigin
@Api("广告推荐")
@Slf4j
@RestController
@RequestMapping("/api/cms/ad")
public class ApiAdController {

    @Autowired
    private AdService adService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("根据推荐位id显示广告推荐")
    @GetMapping("list/{adTypeId}")
    public R listByAdTypeId(@ApiParam(value = "推荐位ID",required = true)@PathVariable String adTypeId){
        List<Ad> adList =  adService.selectByAdTypeId(adTypeId);
        return R.ok().data("items",adList);
    }

    @PostMapping("save-test")
    public R saveAd(@RequestBody Ad ad){

        redisTemplate.opsForValue().set("index::myAd", ad);
        return R.ok();
    }

    @GetMapping("get-test/{key}")
    public R getAd(@PathVariable String key){

        Ad ad = (Ad)redisTemplate.opsForValue().get(key);
        return R.ok().data("ad", ad);
    }

    @DeleteMapping("remove-test/{key}")
    public R removeAd(@PathVariable String key){

        Boolean delete = redisTemplate.delete(key);
        System.out.println(delete);
        Boolean aBoolean = redisTemplate.hasKey(key);
        System.out.println(aBoolean);
        return R.ok();
    }
}
