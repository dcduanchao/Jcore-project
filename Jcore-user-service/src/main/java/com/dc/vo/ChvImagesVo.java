package com.dc.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class ChvImagesVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -2307815323006765782L;


    private Integer imageId;

    private String imageName;

    private Date imageDateGmt;

    private String imageExtension;

    private String imageUrl;
}
