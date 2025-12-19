package com.dc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.dc.entity.ChvImages;
import com.dc.vo.ChvImagesVo;

/**
* @author admin
* @description 针对表【chv_images】的数据库操作Service
* @createDate 2025-07-21 14:28:59
*/
public interface ChvImagesService extends IService<ChvImages> {

    Page<ChvImagesVo> list(Integer current, Integer size);
}
