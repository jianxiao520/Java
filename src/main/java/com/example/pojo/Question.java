package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

// 省略get set
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Question implements Serializable {

    private int id; // 题目ID
    private String title; // 题目
    private String html; // 题目详细
    private int amountCompleted; // 完成数
    private String Difficulty; // 难度
    private LocalDateTime datetime; // 更新时间
}
