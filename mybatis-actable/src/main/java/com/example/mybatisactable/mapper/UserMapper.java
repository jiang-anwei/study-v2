package com.example.mybatisactable.mapper;

import com.example.mybatisactable.entity.User;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-07 14:17
 **/
//@org.apache.ibatis.annotations.Mapper
    @org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<User> {
}
