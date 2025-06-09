package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.helper.enumeration.TopicStatus;
import com.backend.study_hub_api.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {

    List<Topic> findTop10ByStatusOrderByViewCountDesc(TopicStatus status);

}
