package com.example.mybatisactable.mapper;

import com.example.mybatisactable.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {
@Autowired
    UserMapper userMapper;
@Test
    public void test(){
    User user=new User();
    user.setUserName("jianganwei");
    userMapper.insert(user);

}
}