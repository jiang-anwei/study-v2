package com.example.mybatisactable.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//import com.gitee.sunchenbin.mybatis.actable.annotation.Table;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-07 14:09
 **/
@Table(name = "user")
@Data
public class User  {

    private static final long serialVersionUID = -5368134454546655109L;
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private String userName;
}
