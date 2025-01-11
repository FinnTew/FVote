package com.fvote.repository;

import com.fvote.entity.Votes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface VotesRepository extends JpaRepository<Votes, Long>, JpaSpecificationExecutor<Votes> {

    List<Votes> findAllByTopicId(Long topicId);

    Optional<Votes> findVotesByTopicIdAndChoiceId(Long topicId, Long choiceId);
}