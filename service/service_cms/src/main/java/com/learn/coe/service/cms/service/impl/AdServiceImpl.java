package com.learn.coe.service.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.coe.common.base.result.R;
import com.learn.coe.service.cms.entity.Ad;
import com.learn.coe.service.cms.entity.vo.AdVo;
import com.learn.coe.service.cms.feign.OssFileService;
import com.learn.coe.service.cms.mapper.AdMapper;
import com.learn.coe.service.cms.service.AdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-23
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    private OssFileService ossFileService;
    @Override
    public boolean removeAdImageById(String id) {
        Ad ad = baseMapper.selectById(id);
        if (ad != null){
            String imageUrl = ad.getImageUrl();
            if (StringUtils.hasLength(imageUrl)){
                R r = ossFileService.removeFile(imageUrl);

                return r.getSuccess();
            }
        }
        return false;
    }

    @Override
    public IPage<AdVo> selectPage(Long page, Long limit) {
        QueryWrapper<AdVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("a.type_id","a.sort");

        Page<AdVo> pageParam = new Page<>();

        List<AdVo> records = baseMapper.selectPageByQueryWrapper(pageParam,queryWrapper);
        pageParam.setRecords(records);
        return pageParam;
    }

    /**
     * 1、查询redis缓存中是否存在需要的数据  hasKey
     * 2、如果缓存不存在从数据库中取出数据、并将数据存入缓存  set
     * 3、如果缓存存在则从缓存中读取数据  get
     */
    @Cacheable(value = "index", key = "'selectByAdTypeId'")
    @Override
    public List<Ad> selectByAdTypeId(String adTypeId) {

        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort", "id");
        queryWrapper.eq("type_id", adTypeId);
        return baseMapper.selectList(queryWrapper);
    }
}
