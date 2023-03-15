package org.jeecg.modules.neksmart.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 商家信息
 * @Author: jeecg-boot
 * @Date:   2023-03-15
 * @Version: V1.0
 */
@Data
@TableName("t_store")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_store对象", description="商家信息")
public class Tstore {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**店铺名*/
	@Excel(name = "店铺名", width = 15)
    @ApiModelProperty(value = "店铺名")
	private java.lang.String name;
	/**imgUrls*/
	@Excel(name = "imgUrls", width = 15)
    @ApiModelProperty(value = "imgUrls")
	private java.lang.String imgUrls;
	/**地址*/
	@Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
	private java.lang.String address;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
	private java.lang.String mobile;
	/**店铺开始时间*/
	@Excel(name = "店铺开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "店铺开始时间")
	private java.util.Date beginTime;
	/**店铺结束时间*/
	@Excel(name = "店铺结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "店铺结束时间")
	private java.util.Date endTime;
	/**店铺描述*/
	@Excel(name = "店铺描述", width = 15)
    @ApiModelProperty(value = "店铺描述")
	private java.lang.String description;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
	private java.lang.Double longitude;
	/**维度*/
	@Excel(name = "维度", width = 15)
    @ApiModelProperty(value = "维度")
	private java.lang.Double latitude;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.Integer createBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
	private java.lang.Integer updateBy;
	/**remark*/
	@Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
	private java.lang.String remark;
	/**0:失效 1:有效*/
	@Excel(name = "0:失效 1:有效", width = 15)
    @ApiModelProperty(value = "0:失效 1:有效")
	private java.lang.Integer isDelete;
}
