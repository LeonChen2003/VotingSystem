package com.vote.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vote.mapper.ParticipateMapper;
import com.vote.mapper.VoteMapper;
import com.vote.pojo.Participate;
import com.vote.pojo.Vote;
import com.vote.service.ParticipateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParticipateServiceImp extends ServiceImpl<ParticipateMapper, Participate>
    implements ParticipateService {

  @Autowired private VoteMapper voteMapper;

  @Autowired private ParticipateMapper participateMapper;

  @Override
  public boolean isVoteAlready(String username, String title, String creatorId) {
    //    QueryWrapper<Vote> queryWrapper = new QueryWrapper<>();
    //    queryWrapper.select("title", "user_id").eq(title, creatorId);
    //    List<Vote> voteList1 = voteMapper.selectList(queryWrapper);
    Map<String, Object> mapVote = new HashMap<>();
    mapVote.put("title", title);
    mapVote.put("user_id", creatorId);
    List<Vote> voteList = voteMapper.selectByMap(mapVote);
    for (Vote vote : voteList) {
      Map<String, Object> map = new HashMap<>();
      map.put("user_id", username);
      map.put("vote_id", vote.getId());
      if (participateMapper.selectByMap(map).size() == 0) {
        return false;
      }
    }
    return true;
  }
}
