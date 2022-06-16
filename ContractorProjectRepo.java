package com.nsu.data.repo;

import com.nsu.data.entities.project.ContractorProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorProjectRepo extends JpaRepository<ContractorProject, Integer> {

}