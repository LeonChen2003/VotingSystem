package com.vote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vote.pojo.Vote;

import java.util.List;

public interface VoteService extends IService<Vote> {
  void addVote(Vote vote);

  List<Vote> getAllVote();

  List<Vote> searchLike(String content);

  List<Vote> listChoice(String title, String userId);

  void participate(String username, int number, String title, String creatorId);

  List<Vote> getMyVote(String id);

  void deleteVote(String title, String userId);

  List<Vote> getMyParticipate(String id);

  void deleteParticipate(String title, String userId, String creatorId);

  List<Vote> searchMyVote(String content, String creatorId);

  List<Vote> searchMyParticipate(String content, String username);

  void updateMyVote(Vote vote);

  String choiceCount(String title, String creatorId);

  List<Vote> sortByHot();

  boolean stopVote(String title, String username);

  boolean isStop(String title, String creatorId);
}
