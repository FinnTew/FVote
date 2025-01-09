package com.fvote.service.impl;

import com.fvote.entity.Votes;
import com.fvote.repository.VotesRepository;
import com.fvote.service.VoteService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VotesRepository votesRepository;

    @Override
    @Transactional
    public Votes createVote(Votes vote) {
        vote.setCreatedAt(new Date());
        return votesRepository.save(vote);
    }

    @SneakyThrows
    @Override
    public Votes getVoteById(Long id) {
        return votesRepository.findById(id).orElseThrow(() ->
                new Exception("Vote not found with id: " + id));
    }

    @Override
    public List<Votes> getAllVotes() {
        return votesRepository.findAll();
    }

    @Override
    public List<Votes> getVotesByUserId(Long userId) {
        return votesRepository.findAllByUserId(userId);
    }

    @Override
    public List<Votes> getVotesByTopicId(Long topicId) {
        return votesRepository.findAllByTopicId(topicId);
    }

    @Override
    @Transactional
    public Votes updateVote(Long id, Votes updatedVote) {
        Votes existingVote = getVoteById(id);
        existingVote.setTopicId(updatedVote.getTopicId());
        existingVote.setUserId(updatedVote.getUserId());
        existingVote.setChoiceId(updatedVote.getChoiceId());
        existingVote.setCreatedAt(new Date());
        return votesRepository.save(existingVote);
    }

    @Override
    @Transactional
    public void deleteVote(Long id) {
        Votes existingVote = getVoteById(id);
        votesRepository.delete(existingVote);
    }
}