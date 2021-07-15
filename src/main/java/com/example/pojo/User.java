package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

// 省略get set
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class User implements Serializable {
    private Integer id; // 主键

    @NotBlank(message = "邮箱不得为空")
    @Email()
    private String email; // 邮箱


    @NotBlank(message = "密码不得为空")
    @Length(min = 5, message = "密码长度至少6位")
    private String password; // 密码


    private String salt; // 盐
    private LocalDateTime activationTime; // 校验码过期时间
    private String confirmCode; // 校验码
    private Byte isValid; // 账号是否可用
}
