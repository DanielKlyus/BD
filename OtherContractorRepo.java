package com.nsu.data.repo;

import com.nsu.data.entities.contract.OtherContractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherContractorRepo extends JpaRepository<OtherContractor, Integer> {

}