package com.example.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.mapper.UserMapper;
import com.example.pojo.User;
import com.example.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private MailService mailService;

    /**
     * 测试接口 -> 获取全部用户列表
     *
     * @return
     */
    public List<User> getUsers() {
        return userMapper.selectUserTable();
    }

    /**
     * 创建账号
     *
     * @param user
     * @return
     */
    public ResponseEntity<Map<String, Object>> createAccount(User user) {
        Map<String, Object> resultMap = new HashMap<>();
        List<User> userList = userMapper.selectUserByEmail(user.getEmail());

        if (userList.size() > 0) {
            resultMap.put("code", 400);
            resultMap.put("message", "该邮箱已注册！");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
        }

        // [雪花算法]生成邮箱确认码
        String confirmCode = IdUtil.getSnowflake(1, 1).nextIdStr();

        // 发送邮件
        String activationUrl = "http://localhost:8888/user/activation?confirmCode=" + confirmCode;
        try {
            mailService.sendMailForActivationAccount(activationUrl, user.getEmail());
        }catch (Exception e){
            // 邮件发送失败返回
            resultMap.put("code", 400);
            resultMap.put("message", "邮件发送失败，请重试或联系管理员！");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
        }

        // 生成 -> 盐
        String salt = RandomUtil.randomString(6);
        // 密码 ： MD5(原始密码 + 盐)
        String md5Pwd = SecureUtil.md5(user.getPassword() + salt);
        // 激活失效时间：24小时
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        // 初始化账户信息
        user.setSalt(salt);
        user.setActivationTime(localDateTime);
        user.setPassword(md5Pwd);
        user.setConfirmCode(confirmCode);
        user.setIsValid((byte) 0);
        int result = userMapper.insertUser(user);

        if (result < 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "账户创建失败");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
        }

        // 返回信息
        resultMap.put("code", 200);
        resultMap.put("message", "账户创建成功");
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }

    /**
     * 登录账号
     *
     * @param user
     * @return
     */
    public ResponseEntity<Map<String, Object>> loginAccount(User user) {
        // 先查询根据 Email 查询账户数量 正常：1 不存在：0 异常 > 1
        Map<String, Object> resultMap = new HashMap<>();
        List<User> userList = userMapper.selectUserByEmailAsValid(user.getEmail());

        if (userList == null || userList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "邮箱未注册，请先注册！");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
        }

        if (userList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "您的账户异常，请联系管理员！");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
        }

        User u = userList.get(0);
        String pwdTomd5 = SecureUtil.md5(user.getPassword() + u.getSalt());
        // userMapper.selectUserByEmailPassword()

        if (!u.getPassword().equals(pwdTomd5)) {
            resultMap.put("code", 400);
            resultMap.put("message", "账户密码出错！");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
        }

        // 返回 Token
        resultMap.put("code", 200);
        resultMap.put("message", JwtUtil.getToken(u));
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }

    /**
     * 激活账户
     *
     * @param confirmCode
     * @return
     */
    public ResponseEntity<Map<String, Object>> activationAccount(String confirmCode) {
        Map<String, Object> resultMap = new HashMap<>();
        // 首先先根据 confirmCode 查询账户
        User user = userMapper.selectUserByConfirmCode(confirmCode);

        // 判断 confirmCode 是否存
        if (user == null || user.getEmail().isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "激活链接参数错误！");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
        }

        // 判断 confirmCode 是否超时
        boolean isAfter = LocalDateTime.now().isAfter(user.getActivationTime());
        if (isAfter) {
            resultMap.put("code", 400);
            resultMap.put("message", "激活链接已过期，请重新注册！");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
        }

        // 修改该账户的 激活状态
        int resultUpdateConfirmCode = userMapper.updateUserByConfirmCode(confirmCode);
        if (resultUpdateConfirmCode == 0 || resultUpdateConfirmCode > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "激活失败，请联系管理员！");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
        }
        resultMap.put("code", 200);
        resultMap.put("message", "激活成功！快去登录你的账户吧！");
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }


    /**
     * 根据UserId查询用户信息
     *
     * @param UserId
     * @return
     */
    public User findAccountInfoById(String UserId) {

        // 判断范围
        if (new Integer(UserId) < 0 || UserId.isEmpty())
            return null;
        User user = userMapper.selectUserById(UserId);
        return user;
    }

    /**
     * 校验账户是否已登录 -> 未登录借助注解进行判断
     *
     * @return
     */
    public ResponseEntity<Map<String, Object>> verifyUserIsLogin() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 200);
        resultMap.put("message", "ok");
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }
}
