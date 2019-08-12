package com.dbnewyouth.mingyue.cdk.service.impl;

import com.dbnewyouth.mingyue.base.mapper.BaseMapper;
import com.dbnewyouth.mingyue.base.service.BaseServiceImpl;
import com.dbnewyouth.mingyue.cdk.mapper.CdkInfoMapper;
import com.dbnewyouth.mingyue.cdk.model.CdkInfoBean;
import com.dbnewyouth.mingyue.cdk.service.ICdkInfoService;
import com.dbnewyouth.mingyue.utils.StringUtil;
import com.dbnewyouth.mingyue.utils.data.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务实现类
 * CDK信息表
 * @author xinfeng
 * @since 2019-08-10
 */
@Service("cdkInfoService")
public class CdkInfoServiceImpl extends BaseServiceImpl<CdkInfoBean> implements ICdkInfoService {

    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(CdkInfoServiceImpl.class);

    @Autowired
    private CdkInfoMapper cdkInfoMapper;

    @Override
    public BaseMapper<CdkInfoBean> getBaseMapper() {
        return cdkInfoMapper;
    }

    @Override
    public String getCdk(String openId,String cdkType){
        // 参数校验
        if(StringUtil.isBlank(openId)){
            return Constants.CDK_MSG_OPENID;
        }
        // 查询是否领取过
        CdkInfoBean params = new CdkInfoBean();
        params.setOpenId(openId);
        params.setType(cdkType);
        CdkInfoBean bean = cdkInfoMapper.queryByOpenId(params);
        // 如果是空 则该用户领取过CDK
        if(bean != null){
            return Constants.CDK_MSG_RECEIVED;
        }
        // 获取一个新的CDK，如果拿不到，说明没有cdk了。
        CdkInfoBean cdk = cdkInfoMapper.queryByAsc(cdkType);
        if(cdk == null){
            return Constants.CDK_MSG_NULL;
        }
        cdk.setOpenId(openId);
        // 更新为使用状态
        cdkInfoMapper.update(cdk);
        // 返回cdk信息
        return Constants.CDK_MSG_RECEIVE + cdk.getCdk();
    }
}
