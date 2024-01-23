package com.vote.controller;

import com.vote.pojo.Result;
import com.vote.pojo.Vote;
import com.vote.service.ParticipateService;
import com.vote.service.VoteService;
import com.vote.util.StringTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vote")
@CrossOrigin(origins = "*", maxAge = 60)
public class VoteController {

  @Autowired private VoteService voteService;
  @Autowired private ParticipateService participateService;

  /** 添加投票 */
  @PostMapping
  public Result AddVote(@RequestBody Vote vote) {
    voteService.addVote(vote);
    return Result.success();
  }

  /** 获取投票信息 */
  @GetMapping
  public Result getAllVote() {
    List<Vote> voteList = voteService.getAllVote();
    return Result.success(voteList);
  }

  /** 删除我发起的投 */
  @DeleteMapping
  public Result deleteVote(String title, String userId) {
    voteService.deleteVote(title, userId);
    return Result.success();
  }

  /** 模糊查询 */
  @GetMapping("/search")
  public Result search(String content) {
    List<Vote> voteList = voteService.searchLike(content);
    return Result.success(voteList);
  }

  /** 我的投票模糊查询 */
  @GetMapping("/searchMyVote")
  public Result searchMyVote(String content, String creatorId) {
    List<Vote> voteList = voteService.searchMyVote(content, creatorId);
    return Result.success(voteList);
  }

  /** 我参与的投票模糊查询 */
  @GetMapping("/searchMyParticipate")
  public Result searchMyParticipate(String content, String username) {
    List<Vote> voteList = voteService.searchMyParticipate(content, username);
    return Result.success(voteList);
  }

  @GetMapping("/choice")
  public Result getChoice(String title, String userId) {
    List<Vote> voteList = voteService.listChoice(title, userId);
    if (voteList.size() == 1) {
      return Result.success(StringTool.splitByDot(voteList.get(0).getChoice()));
    }
    return Result.error("传递选项失败");
  }

  /** 参与投票 */
  @GetMapping("/participate")
  public Result participate(String username, int number, String title, String creatorId) {
    if (!voteService.isStop(title, creatorId)) {

      if (!participateService.isVoteAlready(username, title, creatorId)) {
        voteService.participate(username, number, title, creatorId);
        return Result.success();
      }
      return Result.error("请勿多次投票");

    } else {
      return Result.error("投票已被暂停");
    }
  }

  /** 获取我发起的投票的信息 */
  @GetMapping("/myVote")
  public Result myVote(String id) {
    List<Vote> voteList = voteService.getMyVote(id);
    return Result.success(voteList);
  }

  /** 查询我参与的投票 */
  @GetMapping("/myParticipate")
  public Result getMyParticipate(String id) {
    List<Vote> votelist = voteService.getMyParticipate(id);
    return Result.success(votelist);
  }

  /** 撤回已参与的投票 */
  @DeleteMapping("/deleteMyParticipate")
  public Result deleteMyParticipate(String title, String userId, String creatorId) {
    voteService.deleteParticipate(title, userId, creatorId);
    return Result.success();
  }

  /** 更新已经发起的投票 */
  @PutMapping
  public Result updateMyVote(@RequestBody Vote vote) {
    voteService.updateMyVote(vote);
    return Result.success();
  }

  /** 查询投票各个选项的参与人数 */
  @GetMapping("/count")
  public Result choiceCount(String title, String creatorId) {
    String s = voteService.choiceCount(title, creatorId);
    System.out.println(s);
    return Result.success(s);
  }

  /** 查看是否已经参与该投票 */
  @GetMapping("/voteAlready")
  public Result voteAlready(String username, String title, String creatorId) {
    if (participateService.isVoteAlready(username, title, creatorId)) {
      return Result.success();
    }
    return Result.error("没有参与投票，无法查看投票结果");
  }

  /** 按热度排序 */
  @GetMapping("/sortHot")
  public Result sortHot() {
    List<Vote> voteList = voteService.sortByHot();
    return Result.success(voteList);
  }

  /** 暂停投票 */
  @GetMapping("/stop")
  public Result stopVote(String title, String username) {
    boolean StartToStop = voteService.stopVote(title, username);
    if (StartToStop) {
      return Result.success();
    }
    return Result.error("");
  }
}
