package com.fvote.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Setter
@Getter
@Entity
@Table(name = "votes")
public class Votes implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic_id", nullable = false)
    private Long topicId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "choice_id", nullable = false)
    private Long choiceId;

    @Column(name = "created_at")
    private Date createdAt;

}
