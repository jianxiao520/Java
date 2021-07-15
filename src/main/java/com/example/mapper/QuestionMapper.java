package com.example.mapper;

import com.example.pojo.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    /**
     * 查询全部题目信息
     *
     * @return
     */
    @Select("SELECT id, title, amountCompleted, Difficulty, datetime " +
            "FROM questions")
    List<Question> selectAllQuestion();

    /**
     * 根据题目ID查询问题详细数据
     *
     * @return
     */
    @Select("SELECT * FROM questions WHERE id = #{id}")
    Question selectQuestionById(@Param("id") String id);
}
