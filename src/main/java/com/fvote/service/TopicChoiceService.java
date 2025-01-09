package com.fvote.service;

import com.fvote.entity.Topics;
import com.fvote.entity.Choices;
import java.util.List;

public interface TopicChoiceService {

    Topics createTopic(Topics topic);

    Choices createChoice(Choices choice);

    Topics getTopicById(Long id);

    List<Topics> getAllTopics();

    List<Choices> getChoicesByTopicId(Long topicId);

    Topics updateTopic(Long id, Topics updatedTopic);

    void deleteTopic(Long id);

    Choices updateChoice(Long id, Choices updatedChoice);

    void deleteChoice(Long id);
}