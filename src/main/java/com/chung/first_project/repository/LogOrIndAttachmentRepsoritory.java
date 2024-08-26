package com.chung.first_project.repository;

import com.chung.first_project.entity.LogOrIndAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogOrIndAttachmentRepsoritory extends JpaRepository<LogOrIndAttachment, Long> {
}
