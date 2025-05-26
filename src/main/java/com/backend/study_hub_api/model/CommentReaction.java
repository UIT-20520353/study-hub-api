package com.backend.study_hub_api.model;

import com.backend.study_hub_api.helper.enumeration.ReactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "t_comment_reactions",
       uniqueConstraints = {
               @UniqueConstraint(columnNames = {"comment_id", "user_id"})
       })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private TopicComment comment;

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