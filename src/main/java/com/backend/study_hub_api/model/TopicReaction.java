package com.backend.study_hub_api.model;

import com.backend.study_hub_api.helper.enumeration.ReactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "t_topic_reactions",
       uniqueConstraints = {
               @UniqueConstraint(columnNames = {"topic_id", "user_id"})
       })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}