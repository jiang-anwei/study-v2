package com.example.distributedtranscation.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-04-11 17:14
 **/
@Table(name = "user")
@Data
@RequiredArgsConstructor()
public class User {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    @NonNull
    private String userName;
}
