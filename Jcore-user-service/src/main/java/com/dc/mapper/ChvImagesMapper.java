package com.dc.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc.entity.ChvImages;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author admin
* @description 针对表【chv_images】的数据库操作Mapper
* @createDate 2025-07-21 14:28:59
* @Entity com.dc.entry.ChvImages
*/
@Mapper
public interface ChvImagesMapper extends BaseMapper<ChvImages> {

    @Select("select * from chevereto.chv_images")
    Page<ChvImages> PageList(IPage<ChvImages> ipage);
}




