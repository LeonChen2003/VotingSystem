package com.vote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vote.pojo.Vote;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VoteMapper extends BaseMapper<Vote> {
    List<Vote> searchMyVote(String content, String creatorId);

    List<Vote> searchTitle(String content, String username);

    void updateMyVote(Vote vote);
}
