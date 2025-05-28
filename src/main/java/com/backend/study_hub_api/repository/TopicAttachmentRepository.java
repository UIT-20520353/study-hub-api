package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.model.TopicAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicAttachmentRepository extends JpaRepository<TopicAttachment, Long> {
}
