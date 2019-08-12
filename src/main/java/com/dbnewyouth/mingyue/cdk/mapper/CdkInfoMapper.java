package com.dbnewyouth.mingyue.cdk.mapper;

import com.dbnewyouth.mingyue.base.mapper.BaseMapper;
import com.dbnewyouth.mingyue.cdk.model.CdkInfoBean;

/**
 * @author xinfeng
 * @version 1.0
 * @date 2019/8/10 0:31
 */
public interface CdkInfoMapper extends BaseMapper<CdkInfoBean> {

	CdkInfoBean queryByAsc(String cdkType);

	CdkInfoBean queryByOpenId(CdkInfoBean params);
}