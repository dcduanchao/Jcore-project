package com.dc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName chv_images
 */
@TableName(value ="chv_images")
@Data
public class ChvImages implements Serializable {
    /**
     * 
     */
    @TableId(value = "image_id", type = IdType.AUTO)
    private Integer imageId;

    /**
     * 
     */
    @TableField(value = "image_name")
    private String imageName;

    /**
     * 
     */
    @TableField(value = "image_extension")
    private String imageExtension;

    /**
     * 
     */
    @TableField(value = "image_size")
    private Long imageSize;

    /**
     * 
     */
    @TableField(value = "image_width")
    private Integer imageWidth;

    /**
     * 
     */
    @TableField(value = "image_height")
    private Integer imageHeight;

    /**
     * 
     */
    @TableField(value = "image_date")
    private Date imageDate;

    /**
     * 
     */
    @TableField(value = "image_date_gmt")
    private Date imageDateGmt;

    /**
     * 
     */
    @TableField(value = "image_title")
    private String imageTitle;

    /**
     * 
     */
    @TableField(value = "image_description")
    private String imageDescription;

    /**
     * 
     */
    @TableField(value = "image_nsfw")
    private Integer imageNsfw;

    /**
     * 
     */
    @TableField(value = "image_user_id")
    private Integer imageUserId;

    /**
     * 
     */
    @TableField(value = "image_album_id")
    private Integer imageAlbumId;

    /**
     * 
     */
    @TableField(value = "image_uploader_ip")
    private String imageUploaderIp;

    /**
     * 
     */
    @TableField(value = "image_storage_mode")
    private Object imageStorageMode;

    /**
     * 
     */
    @TableField(value = "image_path")
    private String imagePath;

    /**
     * 
     */
    @TableField(value = "image_storage_id")
    private Integer imageStorageId;

    /**
     * 
     */
    @TableField(value = "image_checksum")
    private String imageChecksum;

    /**
     * 
     */
    @TableField(value = "image_source_checksum")
    private String imageSourceChecksum;

    /**
     * 
     */
    @TableField(value = "image_original_filename")
    private String imageOriginalFilename;

    /**
     * 
     */
    @TableField(value = "image_original_exifdata")
    private String imageOriginalExifdata;

    /**
     * 
     */
    @TableField(value = "image_views")
    private Integer imageViews;

    /**
     * 
     */
    @TableField(value = "image_category_id")
    private Integer imageCategoryId;

    /**
     * 
     */
    @TableField(value = "image_chain")
    private Integer imageChain;

    /**
     * 
     */
    @TableField(value = "image_thumb_size")
    private Integer imageThumbSize;

    /**
     * 
     */
    @TableField(value = "image_medium_size")
    private Integer imageMediumSize;

    /**
     * 
     */
    @TableField(value = "image_frame_size")
    private Integer imageFrameSize;

    /**
     * 
     */
    @TableField(value = "image_expiration_date_gmt")
    private Date imageExpirationDateGmt;

    /**
     * 
     */
    @TableField(value = "image_likes")
    private Integer imageLikes;

    /**
     * 
     */
    @TableField(value = "image_is_animated")
    private Integer imageIsAnimated;

    /**
     * 
     */
    @TableField(value = "image_is_approved")
    private Integer imageIsApproved;

    /**
     * 
     */
    @TableField(value = "image_is_360")
    private Integer imageIs360;

    /**
     * 
     */
    @TableField(value = "image_duration")
    private Integer imageDuration;

    /**
     * 
     */
    @TableField(value = "image_type")
    private Integer imageType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}