package com.nsu.data.repo;

import com.nsu.data.entities.project.ParticipateProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipateProjectRepo extends JpaRepository<ParticipateProject, Integer> {

}