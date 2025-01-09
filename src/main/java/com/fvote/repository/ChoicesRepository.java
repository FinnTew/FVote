package com.fvote.repository;

import com.fvote.entity.Choices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChoicesRepository extends JpaRepository<Choices, Long>, JpaSpecificationExecutor<Choices> {
    List<Choices> findAllByTopicId(Long topicId);
}