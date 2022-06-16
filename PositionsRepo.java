package com.nsu.data.repo;

import com.nsu.data.entities.employees.Positions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionsRepo extends JpaRepository<Positions, Integer> {

}