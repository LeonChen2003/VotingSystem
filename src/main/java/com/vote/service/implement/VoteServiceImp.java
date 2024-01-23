package com.vote.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vote.mapper.ParticipateMapper;
import com.vote.mapper.VoteMapper;
import com.vote.pojo.Participate;
import com.vote.pojo.Vote;
import com.vote.service.VoteService;
import com.vote.util.StringTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoteServiceImp extends ServiceImpl<VoteMapper, Vote> implements VoteService {

  @Autowired private VoteMapper voteMapper;

  @Autowired private ParticipateMapper participateMapper;

  @Override
  public void addVote(Vote vote) {
    vote.setCreateTime(LocalDate.now());
    voteMapper.insert(vote);
  }

  @Override
  public List<Vote> getAllVote() {
    List<Vote> voteList = voteMapper.selectList(null);
    for (Vote vote : voteList) {
      Map<String, Object> map = new HashMap<>();
      map.put("vote_id", vote.getId());
      vote.setAccount(participateMapper.selectByMap(map).size());
      voteMapper.updateById(vote);
    }
    return voteList;
  }

  @Override
  public List<Vote> searchLike(String content) {
    QueryWrapper<Vote> queryWrapper = new QueryWrapper<>();
    queryWrapper.like("title", content).or().like("user_id", content);
    List<Vote> voteList = voteMapper.selectList(queryWrapper);
    return voteList;
  }

  @Override
  public List<Vote> listChoice(String title, String userId) {
    Map<String, Object> map = new HashMap<>();
    map.put("title", title);
    map.put("user_id", userId);
    List<Vote> voteList = voteMapper.selectByMap(map);
    return voteList;
  }

  @Override
  public void participate(String username, int number, String title, String creatorId) {
    long id = 0;
    Map<String, Object> map = new HashMap<>();
    map.put("title", title);
    map.put("user_id", creatorId);
    List<Vote> voteList = voteMapper.selectByMap(map);
    if (voteList.size() == 1) {
      id = voteList.get(0).getId();
    }
    Participate participate = new Participate(id, username, number);
    participateMapper.insert(participate);
  }

  @Override
  public List<Vote> getMyVote(String id) {
    Map<String, Object> map = new HashMap<>();
    map.put("user_id", id);
    List<Vote> voteList = voteMapper.selectByMap(map);
    for (Vote vote : voteList) {
      Map<String, Object> map2 = new HashMap<>();
      map2.put("vote_id", vote.getId());
      vote.setAccount(participateMapper.selectByMap(map2).size());
    }
    return voteList;
  }

  @Override
  public void deleteVote(String title, String userId) {
    Map<String, Object> map = new HashMap<>();
    map.put("title", title);
    map.put("user_id", userId);
    voteMapper.deleteByMap(map);
  }

  @Override
  public List<Vote> getMyParticipate(String id) {
    List<Vote> voteListResult = new ArrayList<>();
    Map<String, Object> map1 = new HashMap<>();
    Map<String, Object> map2 = new HashMap<>();
    map1.put("user_id", id);
    List<Participate> participateList = participateMapper.selectByMap(map1);
    for (Participate participate : participateList) {
      long voteId = participate.getVoteId();
      map2.put("id", voteId);
      List<Vote> voteList = voteMapper.selectByMap(map2);
      for (Vote vote : voteList) {
        String[] strings = StringTool.splitByDot(vote.getChoice());
        vote.setChoice(strings[participate.getChoice() - 1]);
        voteListResult.add(vote);
      }
    }
    return voteListResult;
  }

  @Override
  public void deleteParticipate(String title, String userId, String creatorId) {
    long voteId = 0;
    Map<String, Object> map1 = new HashMap<>();
    Map<String, Object> map2 = new HashMap<>();

    map1.put("title", title);
    map1.put("user_id", creatorId);
    List<Vote> voteList = voteMapper.selectByMap(map1);
    if (voteList.size() == 1) {
      for (Vote vote : voteList) {
        voteId = vote.getId();
      }
      map2.put("user_id", userId);
      map2.put("vote_id", voteId);
      participateMapper.deleteByMap(map2);
    }
  }

  @Override
  public List<Vote> searchMyVote(String content, String creatorId) {

    return voteMapper.searchMyVote(content, creatorId);
  }

  @Override
  public List<Vote> searchMyParticipate(String content, String username) {
    List<Vote> voteListResult = new ArrayList<>();
    List<Vote> voteList = voteMapper.searchTitle(content, username);
    Map<String, Object> map1 = new HashMap<>();
    Map<String, Object> map2 = new HashMap<>();
    for (Vote vote : voteList) {
      map1.put("vote_id", vote.getId());
      map1.put("user_id", username);
      List<Participate> participateList = participateMapper.selectByMap(map1);
      if (participateList != null) {
        for (Participate participate : participateList) {
          long voteId = participate.getVoteId();
          map2.put("id", voteId);
          List<Vote> voteList1 = voteMapper.selectByMap(map2);
          for (Vote vote1 : voteList1) {
            String[] strings = StringTool.splitByDot(vote1.getChoice());
            vote1.setChoice(strings[participate.getChoice() - 1]);
            voteListResult.add(vote1);
          }
        }
      }
    }
    return voteListResult;
  }

  @Override
  public void updateMyVote(Vote vote) {
    voteMapper.updateMyVote(vote);
  }

  @Override
  public String choiceCount(String title, String creatorId) {
    int[] count = new int[4];
    Map<String, Object> map = new HashMap<>();
    map.put("title", title);
    map.put("user_id", creatorId);
    List<Vote> voteList = voteMapper.selectByMap(map);
    for (Vote vote : voteList) {
      Map<String, Object> mapParticipate = new HashMap<>();
      mapParticipate.put("vote_id", vote.getId());
      List<Participate> participateList = participateMapper.selectByMap(mapParticipate);
      for (Participate participate : participateList) {
        count[participate.getChoice() - 1]++;
      }
    }
    String s = count[0] + "," + count[1] + "," + count[2] + "," + count[3];
    return s;
  }

  @Override
  public List<Vote> sortByHot() {
    QueryWrapper<Vote> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("account");
    return voteMapper.selectList(queryWrapper);
  }

  @Override
  public boolean stopVote(String title, String username) {
    Map<String,Object> map = new HashMap<>();
    map.put("title",title);
    map.put("user_id",username);
    List<Vote> voteList = voteMapper.selectByMap(map);
    for (Vote vote: voteList) {
        if(vote.getState()==0){
          vote.setState(1);
          voteMapper.updateById(vote);
          return true;
        }else {
          vote.setState(0);
          voteMapper.updateById(vote);

        }
    }
    return false;
  }

  @Override
  public boolean isStop(String title, String creatorId) {
    Map<String,Object> map = new HashMap<>();
    map.put("title",title);
    map.put("user_id",creatorId);
    List<Vote> voteList = voteMapper.selectByMap(map);
    for (Vote vote : voteList) {
        if(vote.getState()==1){
          return true;
        }
    }
    return false;
  }
}
