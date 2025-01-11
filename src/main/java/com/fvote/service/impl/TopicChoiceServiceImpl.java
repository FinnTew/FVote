package com.fvote.service.impl;

import com.fvote.entity.Topics;
import com.fvote.entity.Choices;
import com.fvote.repository.ChoicesRepository;
import com.fvote.repository.TopicsRepository;
import com.fvote.service.TopicChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TopicChoiceServiceImpl implements TopicChoiceService {

    @Autowired
    private TopicsRepository topicsRepository;

    @Autowired
    private ChoicesRepository choicesRepository;

    @Override
    @Transactional
    public Topics createTopic(Topics topic) {
        return topicsRepository.save(topic);
    }

    @Override
    public List<Topics> getTopicsByUserId(Long userId) {
        return topicsRepository.findAllByCreatedBy(userId);
    }

    @Override
    @Transactional
    public Choices createChoice(Choices choice) {
        return choicesRepository.save(choice);
    }

    @Override
    public Topics getTopicById(Long id) {
        return topicsRepository.findById(id).orElse(null);
    }

    @Override
    public List<Topics> getAllTopics() {
        return topicsRepository.findAll();
    }

    @Override
    public List<Choices> getChoicesByTopicId(Long topicId) {
        return choicesRepository.findAllByTopicId(topicId);
    }

    @Override
    @Transactional
    public Topics updateTopic(Long id, Topics updatedTopic) {
        Topics existingTopic = topicsRepository.findById(id).orElse(null);
        if (existingTopic != null) {
            existingTopic.setTitle(updatedTopic.getTitle());
            existingTopic.setDescription(updatedTopic.getDescription());
            existingTopic.setMultipleChoice(updatedTopic.getMultipleChoice());
            existingTopic.setMaxChoices(updatedTopic.getMaxChoices());
            existingTopic.setCreatedBy(updatedTopic.getCreatedBy());
            existingTopic.setUpdatedAt(new Date());
            return topicsRepository.save(existingTopic);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteTopic(Long id) {
        topicsRepository.deleteById(id);
        choicesRepository.deleteByTopicId(id);
    }

    @Override
    @Transactional
    public Choices updateChoice(Long id, Choices updatedChoice) {
        Choices existingChoice = choicesRepository.findById(id).orElse(null);
        if (existingChoice != null) {
            existingChoice.setChoiceText(updatedChoice.getChoiceText());
            existingChoice.setCreatedAt(new Date());
            return choicesRepository.save(existingChoice);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteChoice(Long id) {
        choicesRepository.deleteById(id);
    }
}