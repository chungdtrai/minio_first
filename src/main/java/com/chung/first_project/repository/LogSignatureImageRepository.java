package com.chung.first_project.repository;

import com.chung.first_project.entity.LogSignatureImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogSignatureImageRepository extends JpaRepository<LogSignatureImage, Long> {
}
