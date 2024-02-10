package com.trb.allocationservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trb.allocationservice.entity.LevelsModel;

@Repository
public interface LevelsRepo extends JpaRepository<LevelsModel, Long> {

	LevelsModel findByLvlId(Long id);
	
}
