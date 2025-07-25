package com.backend.study_hub_api.model;

import com.backend.study_hub_api.helper.enumeration.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "t_users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "student_id", unique = true)
    private String studentId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    private String major;

    private Integer year;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String phone;

    private String bio;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<UserSession> sessions;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Topic> topics;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<TopicComment> comments;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TopicReaction> topicReactions;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TopicFollower> followedTopics;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY)
    private List<Notification> receivedNotifications;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Notification> sentNotifications;

    public void setNewSession(UserSession session) {
        session.setUser(this);
        this.sessions.clear();
        this.sessions.add(session);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
