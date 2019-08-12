package com.dbnewyouth.mingyue.base.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 * 
 * @author Zhibing.Xie
 * @since 2017-11-01
 */
@Getter
@Setter
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Integer id;
    /** 创建时间 */
    private Date createTime;
    /** 修改时间 */
    private Date modifyTime;
    /** 删除标识: [1 有效; 0 删除;] */
    private Integer dr;
}
