package com.springboot.community.service;

import com.springboot.community.dto.PaginationDto;
import com.springboot.community.dto.QuestionDto;
import com.springboot.community.mapper.QuestionMapper;
import com.springboot.community.mapper.UserMapper;
import com.springboot.community.model.Question;
import com.springboot.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//在这里不仅能使用questionMapper，还能使用userMapper，起到一个组装的作用
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    public PaginationDto list(Integer page, Integer size) {
        PaginationDto paginationDto = new PaginationDto();
        Integer totalPage; //页面总数
        Integer totalCount = questionMapper.count(); //拿到所有数量
        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }
        //防止页面被改变出现-1等越界页面
        if (page < 1){
            page = 1;
        }else if (page > totalPage){
            page = totalPage;
        }
        paginationDto.setPagination(totalPage, page); //把所有需要的页面参数存起来
        //这一页开头问题的序号
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset, size); //查到每一页的列表
        List<QuestionDto> questionDtoList = new ArrayList<>();
        for (Question question:questions) {
            //creator和user的id关联，拿到creator就是拿到user的id
           User user = userMapper.findById(question.getCreator());
           QuestionDto questionDto = new QuestionDto();
           //这个方法的作用是把question的所有属性copy进questionDto中
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDto.setQuestions(questionDtoList);
        return paginationDto;
    }

    public PaginationDto listByUserId(int userId, Integer page, Integer size) {
        PaginationDto paginationDto = new PaginationDto();
        Integer totalPage; //页面总数
        Integer totalCount = questionMapper.countByUserId(userId); //拿到所有数量
        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }
        //防止页面被改变出现-1等越界页面
        if (page < 1){
            page = 1;
        }else if (page > totalPage){
            page = totalPage;
        }
        paginationDto.setPagination(totalPage, page); //把所有需要的页面参数存起来
        //这一页开头问题的序号
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.listByUserId(userId, offset, size); //查到每一页的列表
        List<QuestionDto> questionDtoList = new ArrayList<>();
        for (Question question:questions) {
            //creator和user的id关联，拿到creator就是拿到user的id
            User user = userMapper.findById(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            //这个方法的作用是把question的所有属性copy进questionDto中
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDto.setQuestions(questionDtoList);
        return paginationDto;
    }

    public QuestionDto getQuestionDtoById(Integer id) {
        Question question = questionMapper.getQuestionDtoById(id);
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question, questionDto);
        User user = userMapper.findById(question.getCreator());
        questionDto.setUser(user);
        return questionDto;
    }
}
