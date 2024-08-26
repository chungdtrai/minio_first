package com.chung.first_project.repository;

import com.chung.first_project.entity.FileDownUpLog;
import com.chung.first_project.entity.LogParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDownUpRepository extends LogParentRepository {
}
