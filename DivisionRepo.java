package com.nsu.data.repo;

import com.nsu.data.entities.employees.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionRepo extends JpaRepository<Division, Integer> {

}