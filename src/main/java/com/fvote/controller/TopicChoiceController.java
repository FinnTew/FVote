package com.fvote.controller;

import com.fvote.entity.*;
import com.fvote.service.TopicChoiceService;
import com.fvote.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/topic")
public class TopicChoiceController {
    @Autowired
    private TopicChoiceService topicChoiceService;

    @PostMapping("/create")
    public Result<?> createTopic(@RequestBody TopicCreateReq req) {
        if (req.getTopic().getTitle().isEmpty()) {
            return Result.error("Title is required");
        }
        if (req.getTopic().getIsMultipleChoice() == null) {
            return Result.error("isMultipleChoice is required");
        }
        if (req.getTopic().getMaxChoice() == null) {
            return Result.error("maxChoices is required");
        }
        if (req.getChoices().length == 0) {
            return Result.error("Choices is required");
        }

        if (req.getTopic().getIsMultipleChoice() && req.getTopic().getMaxChoice() < 2) {
            return Result.error("Max choices must be at least 2 for multiple choice topic");
        }

        Topics topic = new Topics();
        topic.setTitle(req.getTopic().getTitle());
        topic.setDescription(req.getTopic().getDescription());
        topic.setMultipleChoice(req.getTopic().getIsMultipleChoice());
        topic.setMaxChoices(req.getTopic().getMaxChoice());
        topic.setCreatedAt(new Date());
        topic.setUpdatedAt(new Date());
        topic.setCreatedBy(req.getUserId());

        Topics t = topicChoiceService.createTopic(topic);

        List<Choices> choices = new ArrayList<>();
        for (String choiceText : req.getChoices()) {
            Choices choice = new Choices();
            choice.setTopicId(t.getId());
            choice.setChoiceText(choiceText);
            choice.setCreatedAt(new Date());
            choices.add(choice);
            topicChoiceService.createChoice(choice);
        }

        TopicCreateResp resp = new TopicCreateResp();
        resp.setTopic(t);
        resp.setChoices(choices);

        return Result.success(
                "Create topic success",
                resp
        );
    }

    @PostMapping("/update")
    public Result<?> updateTopic(@RequestBody TopicUpdateReq req) {
        if (req.getUserId() == null) {
            return Result.error("User id is required");
        }
        if (req.getTopic().getTopicId() == null) {
            return Result.error("Topic id is required");
        }

        Topics topic = topicChoiceService.getTopicById(req.getTopic().getTopicId());
        if (topic == null) {
            return Result.error("Topic not found");
        }
        if (!topic.getCreatedBy().equals(req.getUserId())) {
            return Result.error("You are not the owner of this topic");
        }

        if (req.getTopic().getTitle() != null) {
            topic.setTitle(req.getTopic().getTitle());
        }
        if (req.getTopic().getDescription() != null) {
            topic.setDescription(req.getTopic().getDescription());
        }
        if (req.getTopic().getIsMultipleChoice() != null) {
            topic.setMultipleChoice(req.getTopic().getIsMultipleChoice());
        }
        if (req.getTopic().getMaxChoice() != null) {
            topic.setMaxChoices(req.getTopic().getMaxChoice());
        }
        topic.setUpdatedAt(new Date());
        topicChoiceService.updateTopic(req.getTopic().getTopicId(), topic);

        if (req.getChoices() != null) {
            for (TopicUpdateReq.Choice choice : req.getChoices()) {
                Choices c = new Choices();
                if (choice.getChoiceId() == null) {
                    c.setTopicId(topic.getId());
                    c.setChoiceText(choice.getChoiceText());
                    c.setCreatedAt(new Date());
                    topicChoiceService.createChoice(c);
                    continue;
                }
                c.setId(choice.getChoiceId());
                c.setChoiceText(choice.getChoiceText());
                topicChoiceService.updateChoice(choice.getChoiceId(), c);
            }
        }

        return Result.success(
                "Update topic success",
                null
        );
    }

    @GetMapping("/delete/{id}")
    public Result<?> deleteTopic(@PathVariable Long id) {
        topicChoiceService.deleteTopic(id);
        return Result.success(
                "Delete topic success",
                null
        );
    }

    @GetMapping("/get/{id}")
    public Result<?> getTopic(@PathVariable Long id) {
        Topics topic = topicChoiceService.getTopicById(id);
        if (topic == null) {
            return Result.error("Topic not found");
        }

        List<Choices> choices = topicChoiceService.getChoicesByTopicId(id);

        TopicGetResp resp = new TopicGetResp();
        resp.setTopic(topic);
        resp.setChoices(choices);

        return Result.success(
                "Get topic success",
                resp
        );
    }

    @GetMapping("/getByUser/{userId}")
    public Result<?> getTopicsByUser(@PathVariable Long userId) {
        List<Topics> topics = topicChoiceService.getTopicsByUserId(userId);
        if (topics.isEmpty()) {
            return Result.error("No topics found");
        }

        List<TopicGetResp> resps = new ArrayList<>();
        for (Topics topic : topics) {
            List<Choices> choices = topicChoiceService.getChoicesByTopicId(topic.getId());
            TopicGetResp resp = new TopicGetResp();
            resp.setTopic(topic);
            resp.setChoices(choices);
            resps.add(resp);
        }

        return Result.success(
                "Get topics success",
                resps
        );
    }
}
