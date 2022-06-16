package com.nsu.data.repo;

import com.nsu.data.entities.employees.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupervisorsRepo extends JpaRepository<Employees, Integer> {

}