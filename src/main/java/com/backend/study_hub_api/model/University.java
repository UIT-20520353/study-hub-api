package com.backend.study_hub_api.model;

import com.backend.study_hub_api.helper.enumeration.UniversityStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "t_universities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "short_name")
    private String shortName;

    private String address;

    @Column(name = "email_domain", unique = true, nullable = false)
    private String emailDomain;

    private String city;

    private String website;

    @Column(name = "logo_url")
    private String logoUrl;

    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UniversityStatus status;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "university", fetch = FetchType.LAZY)
    private List<User> users;

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