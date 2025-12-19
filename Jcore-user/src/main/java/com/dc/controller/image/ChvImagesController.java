package com.dc.controller.image;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc.result.ResultVo;
import com.dc.service.ChvImagesService;
import com.dc.vo.ChvImagesVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chv")
public class ChvImagesController {


    @Autowired
    private ChvImagesService chvImagesService;

    @GetMapping("/list")
    public ResultVo list(@RequestParam(defaultValue = "1") Integer current,
                         @RequestParam(defaultValue = "10") Integer size) {
        Page<ChvImagesVo> pageDataVo = chvImagesService.list(current, size);
        return ResultVo.success("获取成功",pageDataVo);
    }
}
