package com.vote;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vote.mapper.UserMapper;
import com.vote.mapper.VoteMapper;
import com.vote.pojo.User;
import com.vote.pojo.Vote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class VotebackApplicationTests {

    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void insertTest(){
        Vote vote = new Vote();
        vote.setTitle("who is the richest?");
        vote.setUserId("skull");
        vote.setCreateTime(LocalDate.now());
        voteMapper.insert(vote);
    }

    @Test
    void deleteTest(){
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_id","skull");
        voteMapper.delete(queryWrapper);
    }

    @Test
    public void logTest() {
        Map<String,Object> map = new HashMap<>();
        map.put("id","user");
        map.put("password","123456");
        List<User> list = userMapper.selectByMap(map);
        System.out.println(list);
    }
}
