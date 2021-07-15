package com.example.service;

import com.example.mapper.QuestionMapper;
import com.example.pojo.Question;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService {
    @Resource
    private QuestionMapper questionMapper;

    /**
     * 查询数据库全部题目
     *
     * @return
     */
    public ResponseEntity<Map<String, Object>> queryAllQuestion() {
        Map<String, Object> result = new HashMap<>();
        List<Question> Questions = questionMapper.selectAllQuestion();
        // 异常判断
        if (Questions.isEmpty()) {
            result.put("code", "400");
            result.put("message", "服务器出现异常，请联系管理员！");
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
        }
        result.put("code", "200");
        result.put("message", Questions);
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }

    /**
     * 根据ID查询题目详细信息
     *
     * @param id
     * @return
     */
    public ResponseEntity<Map<String, Object>> queryQuestionById(String id) {
        Map<String, Object> result = new HashMap<>();
        Question question = questionMapper.selectQuestionById(id);
        if (question == null) {
            result.put("code","400");
            result.put("message","题目ID异常！");
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
        }

        result.put("code", "200");
        result.put("message", question);
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
}
