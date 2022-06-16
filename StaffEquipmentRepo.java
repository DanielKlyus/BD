package com.nsu.data.repo;

import com.nsu.data.entities.staff.StaffEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffEquipmentRepo extends JpaRepository<StaffEquipment, Integer> {

}
