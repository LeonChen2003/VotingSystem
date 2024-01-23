package com.vote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vote.pojo.Participate;

public interface ParticipateService extends IService<Participate> {
    boolean isVoteAlready(String username, String title, String creatorId);
}
