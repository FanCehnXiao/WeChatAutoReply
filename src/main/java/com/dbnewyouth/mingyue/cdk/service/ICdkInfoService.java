package com.dbnewyouth.mingyue.cdk.service;


import com.dbnewyouth.mingyue.base.service.IBaseService;
import com.dbnewyouth.mingyue.cdk.model.CdkInfoBean;

/**
 * 业务类接口
 * CDK信息表
 * @author xinfeng
 * @since 2019-08-10
 */
public interface ICdkInfoService extends IBaseService<CdkInfoBean> {

    String getCdk(String openId,String cdkType);
}
