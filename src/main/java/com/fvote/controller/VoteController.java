package com.fvote.controller;

import com.fvote.entity.Topics;
import com.fvote.entity.VoteCreateReq;
import com.fvote.entity.Votes;
import com.fvote.service.TopicChoiceService;
import com.fvote.service.VoteService;
import com.fvote.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;

    @Autowired
    private TopicChoiceService topicChoiceService;

    @PostMapping("/create")
    public Result<?> createVotes(@RequestBody VoteCreateReq req) {
        if (req.getTopicId() == null) {
            return Result.error("Topic ID is required");
        }
        if (req.getChoiceIds().length == 0) {
            return Result.error("Choice IDs is required");
        }

        if (topicChoiceService.getTopicById(req.getTopicId()) == null) {
            return Result.error("Topic not found with ID: " + req.getTopicId());
        }

        Topics topic = topicChoiceService.getTopicById(req.getTopicId());
        if (topic.getMultipleChoice() && req.getChoiceIds().length > topic.getMaxChoices()) {
            return Result.error("Max choices for this topic is " + topic.getMaxChoices());
        }
        if (!topic.getMultipleChoice() && req.getChoiceIds().length > 1) {
            return Result.error("This topic is not multiple choice");
        }

        for (Long choiceId : req.getChoiceIds()) {
            if (topicChoiceService.getChoicesByTopicId(req.getTopicId()).stream().noneMatch(c -> c.getId().equals(choiceId))) {
                return Result.error("Choice not found with ID: " + choiceId);
            }
            if (voteService.getVoteByTopicIdAndChoiceId(req.getTopicId(), choiceId).isPresent()) {
                return Result.error("Vote already exists for this choice");
            }

            Votes vote = new Votes();
            vote.setTopicId(req.getTopicId());
            vote.setChoiceId(choiceId);
            vote.setCreatedAt(new Date());
            voteService.createVote(vote);
        }

        return Result.success("Vote created successfully", null);
    }

    @GetMapping("/getByTopicId/{topicId}")
    public Result<?> getVotesByTopicId(@PathVariable Long topicId) {
        if (topicChoiceService.getTopicById(topicId) == null) {
            return Result.error("Topic not found with ID: " + topicId);
        }

        return Result.success("Votes retrieved successfully", voteService.getVotesByTopicId(topicId));
    }
}
