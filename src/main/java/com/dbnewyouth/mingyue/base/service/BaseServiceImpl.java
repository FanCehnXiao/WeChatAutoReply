package com.dbnewyouth.mingyue.base.service;

import java.util.List;

import com.dbnewyouth.mingyue.base.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务实现基础类
 * 
 * @author xinfeng
 * @since 2017-11-01
 */
public abstract class BaseServiceImpl<T extends BaseEntity> implements IBaseService<T> {

    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Override
    public long add(T bean) {
        return getBaseMapper().add(bean);
    }

    @Override
    public long update(T bean) {
        return getBaseMapper().update(bean);
    }

    @Override
    public long remove(Integer id) {
        return getBaseMapper().remove(id);
    }

    @Override
    public T queryById(Integer id) {
        return getBaseMapper().queryById(id);
    }

    @Override
    public T queryOne(T bean) {
        return getBaseMapper().queryOne(bean);
    }

    @Override
    public List<T> queryList(T bean) {
        return getBaseMapper().queryList(bean);
    }

}
