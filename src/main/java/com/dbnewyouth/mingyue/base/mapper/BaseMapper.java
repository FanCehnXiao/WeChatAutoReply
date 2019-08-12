package com.dbnewyouth.mingyue.base.mapper;

import java.io.Serializable;
import java.util.List;

/**
 * 基础Mapper数据接口
 * 
 * @author xinfeng
 * @since 2017-11-01
 */
public interface BaseMapper<T> extends Serializable {

    /**
     * 新增
     * 
     * @param bean
     */
    public long add(T bean);

    /**
     * 更新
     * 
     * @param bean
     */
    public long update(T bean);

    /**
     * 删除
     * 
     * @param id
     */
    public long remove(Integer id);

    /**
     * 根据主键查询
     * 
     * @param id
     * @return
     */
    public T queryById(Integer id);

    /**
     * 条件查询单个对象
     * 
     * @param bean
     * @return
     */
    public T queryOne(T bean);

    /**
     * 条件查询
     * 
     * @param bean
     * @return
     */
    public List<T> queryList(T bean);

}
