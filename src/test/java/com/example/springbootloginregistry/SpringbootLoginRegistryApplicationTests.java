package com.example.springbootloginregistry;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.mapper.QuestionMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.Question;
import com.example.pojo.User;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SpringbootLoginRegistryApplicationTests {

    // POJO

    // Mapper
    @Resource
    private UserMapper userMapper;

    @Resource
    private QuestionMapper questionMapper;

    // service
    @Resource
    private UserService userService;

    @Test
    void contextLoads() {
        System.out.println(questionMapper.selectAllQuestion());
//        User user = new User();
//        user.setPassword("5e6f0c5a48094421d482dc74593e2aa9");
//        user.setId(22);
//        user.setEmail("1421170337@qq.com");
//        String token = JwtUtil.getToken(user);
//

//        try {
//            JwtUtil.verifyToken(token,user);
//        } catch (JWTVerificationException e) {
//            System.out.println(e);
//        }
//        System.out.println("OK");
    }
}
