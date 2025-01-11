package com.fvote.service;

import com.fvote.entity.Votes;
import java.util.List;
import java.util.Optional;

public interface VoteService {

    Votes createVote(Votes vote);

    Votes getVoteById(Long id);

    Optional<Votes> getVoteByTopicIdAndChoiceId(Long topicId, Long choiceId);

    List<Votes> getAllVotes();

    List<Votes> getVotesByTopicId(Long topicId);

    Votes updateVote(Long id, Votes updatedVote);

    void deleteVote(Long id);
}