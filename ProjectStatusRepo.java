package com.nsu.data.repo;

import com.nsu.data.entities.project.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectStatusRepo extends JpaRepository<ProjectStatus, Integer> {

}