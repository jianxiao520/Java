package com.example.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.pojo.User;

import java.util.Calendar;
import java.util.Date;


public class JwtUtil {

    // 自定义私钥
    private static final String _SECRET = "ReactAndSpringBoot";

    /**
     * 生成Token
     *
     * @param user
     * @return
     */
    public static String getToken(User user) {
        // 设置 Token 过期时间 20 分钟
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 20);
        Date expiresDate = nowTime.getTime();
        return JWT.create().withAudience(user.getId().toString())   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .withClaim("Email", user.getEmail())    //载荷
                .sign(Algorithm.HMAC256(user.getPassword() + _SECRET));   //加密
    }


    /**
     * 验证Token有效性
     *
     * @param Token
     * @param user
     * @return
     */
    public static boolean verifyToken(String Token, User user) throws JWTVerificationException{
        // 解密认证 Token
        JWTVerifier jwtVerifier = JWT.require(
                Algorithm.HMAC256(user.getPassword() + _SECRET))
                .build();
        jwtVerifier.verify(Token);
        return true;
    }

}