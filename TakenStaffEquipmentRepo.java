package com.nsu.data.repo;

import com.nsu.data.entities.staff.TakenStaffEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakenStaffEquipmentRepo extends JpaRepository<TakenStaffEquipment, Integer> {

}