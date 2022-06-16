package com.nsu.data.repo;

import com.nsu.data.entities.staff.TypeEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeEquipmentRepo extends JpaRepository<TypeEquipment, Integer> {

}