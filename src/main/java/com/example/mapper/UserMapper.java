package com.example.mapper;

import com.example.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 新增账号
     *
     * @param user
     * @return
     */
    @Insert("INSERT INTO user ( email, password, salt, confirm_code, activation_time, is_valid )" +
            " VALUES ( " +
            "#{email}, " +
            "#{password}, " +
            "#{salt}, " +
            "#{confirmCode}, " +
            "#{activationTime}, " +
            "#{isValid} )")
    int insertUser(User user);

    /**
     * 根据校验码查询用户
     *
     * @param confirmCode
     * @return
     */
    @Select("SELECT email, activation_time " +
            "FROM user " +
            "WHERE confirm_code = #{confirmCode} AND is_valid = 0")
    User selectUserByConfirmCode(@Param("confirmCode") String confirmCode);


    /**
     * 更新用户状态码 为 1 (可用)
     *
     * @param confirmCode
     * @return
     */
    @Update("UPDATE user " +
            "SET is_valid = 1 " +
            "WHERE confirm_code = #{confirmCode}")
    int updateUserByConfirmCode(@Param("confirmCode") String confirmCode);

    /**
     * 根据用户 Email 查询 用户数据 WHERE 可用，返回LIST ( 防止有多个账户 )  ->  需判断valid
     *
     * @param email
     * @return
     */
    @Select("SELECT * " +
            "FROM user " +
            "WHERE email = #{email} AND is_valid = 1")
    List<User> selectUserByEmailAsValid(@Param("email") String email);

    /**
     * 根据用户 Email 查询 用户数据 WHERE 可用，返回LIST ( 防止有多个账户 )  ->  无需判断valid
     *
     * @param email
     * @return
     */
    @Select("SELECT * " +
            "FROM user " +
            "WHERE email = #{email}")
    List<User> selectUserByEmail(@Param("email") String email);


    /**
     * 根据用户ID查询用户信息
     * @param id
     * @return
     */
    @Select("SELECT * " +
            "FROM user" +
            " WHERE id = #{id}")
    User selectUserById(@Param("id") String id);


    /**
     * 测试查询全部数据
     *
     * @return
     */
    @Select("SELECT * FROM user WHERE is_valid = 1")
    List<User> selectUserTable();
}

