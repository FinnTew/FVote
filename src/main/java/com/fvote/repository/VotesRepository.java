package com.fvote.repository;

import com.fvote.entity.Votes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface VotesRepository extends JpaRepository<Votes, Long>, JpaSpecificationExecutor<Votes> {

    List<Votes> findAllByUserId(Long userId);

    List<Votes> findAllByTopicId(Long topicId);
}