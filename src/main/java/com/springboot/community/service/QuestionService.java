package com.springboot.community.service;

import com.springboot.community.dto.PaginationDto;
import com.springboot.community.dto.QuestionDto;
import com.springboot.community.exception.CustomizeErrorCode;
import com.springboot.community.exception.CustomizeException;
import com.springboot.community.mapper.QuestionExtMapper;
import com.springboot.community.mapper.QuestionMapper;
import com.springboot.community.mapper.UserMapper;
import com.springboot.community.model.Question;
import com.springboot.community.model.QuestionExample;
import com.springboot.community.model.User;
import org.apache.ibatis.session.RowBounds;
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

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDto list(Integer page, Integer size) {
        PaginationDto paginationDto = new PaginationDto();
        Integer totalPage; //页面总数
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample()); //拿到所有数量
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
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size)); //查到每一页的列表
        List<QuestionDto> questionDtoList = new ArrayList<>();
        for (Question question:questions) {
            //creator和user的id关联，拿到creator就是拿到user的id
           User user = userMapper.selectByPrimaryKey(question.getCreator());
           QuestionDto questionDto = new QuestionDto();
           //这个方法的作用是把question的所有属性copy进questionDto中
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDto.setQuestions(questionDtoList);
        return paginationDto;
    }

    public PaginationDto listByUserId(long userId, Integer page, Integer size) {
        PaginationDto paginationDto = new PaginationDto();
        Integer totalPage; //页面总数
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample); //拿到所有数量
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
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size)); //查到每一页的列表
        List<QuestionDto> questionDtoList = new ArrayList<>();
        for (Question question:questions) {
            //creator和user的id关联，拿到creator就是拿到user的id
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            //这个方法的作用是把question的所有属性copy进questionDto中
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDto.setQuestions(questionDtoList);
        return paginationDto;
    }

    public QuestionDto getQuestionDtoById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question, questionDto);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);
        return questionDto;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //插入
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insertSelective(question);
        }else {
            Question updateQuestion = new Question();
            //更新
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTitle(question.getTitle());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if (updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    //累加阅读数
    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }
}
