package org.apache.ibatis.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.entity.UserDO;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

/**
 * @author furious 2024/4/3
 */
public interface UserMapper {

    void insert();

    void insertWithParam(@Param("name") String name, @Param("age") Integer age);

    /**
     * 单个参数没有设置Param注解时，无需参数名称和xml中变量名相同
     * 见ParamNameResolver中getNamedParams方法，无Param注解，只有一个参数时直接返回值
     * 见DefaultParameterHandler中set方法，String类型已注册Handler，直接返回值
     *
     * @param namex 参数名称
     * @see ParamNameResolver#getNamedParams(java.lang.Object[])
     * @see DefaultParameterHandler#setParameters(java.sql.PreparedStatement)
     */
    void insertWithoutParam(String namex);

    void insertWithPOJO(UserDO user);
}
