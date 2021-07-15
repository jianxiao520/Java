package com.example.controller;

import com.example.annotation.UserLoginToken;
import com.example.mapper.QuestionMapper;
import com.example.pojo.Question;
import com.example.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/question")
@CrossOrigin("*")
public class QuestionController {
    @Resource
    private QuestionService questionService;

    // 查询全部问题
    @GetMapping("/queryAllQuestion")
    public ResponseEntity<Map<String, Object>> queryAllQuestion(){
        return questionService.queryAllQuestion();
    }

    // 查询问题详细 -> 需要已登录
    @UserLoginToken
    @GetMapping("/queryQuestionById")
    public ResponseEntity<Map<String, Object>> queryQuestionById(String id){
        return questionService.queryQuestionById(id);
    }
}
