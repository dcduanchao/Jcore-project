package com.dc.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dc.entity.ChvImages;
import com.dc.mapper.ChvImagesMapper;
import com.dc.service.ChvImagesService;
import com.dc.util.DateUtil;
import com.dc.vo.ChvImagesVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author admin
* @description 针对表【chv_images】的数据库操作Service实现
* @createDate 2025-07-21 14:28:59
*/
@Service
public class ChvImagesServiceImpl extends ServiceImpl<ChvImagesMapper, ChvImages>
    implements ChvImagesService {

    @Value("${chevereto.url.prefix}")
    private String urlPrefix;

    @Autowired
    private ChvImagesMapper chvImagesMapper;
    @Override
    public Page<ChvImagesVo> list(Integer current, Integer size) {

        IPage<ChvImages> Ipage = new Page<>(current, size);
        Page<ChvImages> pageInfo = chvImagesMapper.PageList(Ipage);
        List<ChvImages> records = pageInfo.getRecords();

        Page<ChvImagesVo> pageDataVo = new Page<>() ;
        BeanUtils.copyProperties(pageInfo,pageDataVo);
        List<ChvImagesVo> chvImagesVos = new ArrayList<>();
        for (ChvImages record : records) {
            ChvImagesVo chvImagesVo = new ChvImagesVo();
            chvImagesVo.setImageId(record.getImageId());
            chvImagesVo.setImageName(record.getImageName());
            chvImagesVo.setImageDateGmt(record.getImageDateGmt());
            chvImagesVo.setImageExtension(record.getImageExtension());
            Date imageDateGmt = record.getImageDateGmt();
            String time = DateUtil.dateFormat(imageDateGmt, DateUtil.DEFAULT_DATE_FORMAT_1);
            String imageExtension = record.getImageExtension();
            String url = urlPrefix+"/images/" + time+"/"+record.getImageName()+"."+imageExtension;
            chvImagesVo.setImageUrl(url);
            chvImagesVos.add(chvImagesVo);
        }
        pageDataVo.setRecords(chvImagesVos);
        return pageDataVo;
    }
}




