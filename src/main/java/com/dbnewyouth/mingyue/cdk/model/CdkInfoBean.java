package com.dbnewyouth.mingyue.cdk.model;

import com.dbnewyouth.mingyue.base.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CDK信息
 *
 * @author xinfeng
 * @version 1.0
 * @date 2019/8/10 0:14
 */
@Getter
@Setter
@ToString
public class CdkInfoBean extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** cdk字符串 **/
    private String cdk;
    /** cdk类型 **/
    private String type;
    /** 微信的openid **/
    private String openId;
}
