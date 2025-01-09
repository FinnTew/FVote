package com.fvote.service;

import com.fvote.entity.Votes;
import java.util.List;

public interface VoteService {

    Votes createVote(Votes vote);

    Votes getVoteById(Long id);

    List<Votes> getAllVotes();

    List<Votes> getVotesByUserId(Long userId);

    List<Votes> getVotesByTopicId(Long topicId);

    Votes updateVote(Long id, Votes updatedVote);

    void deleteVote(Long id);
}