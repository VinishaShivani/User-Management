package com.trb.allocationservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trb.allocationservice.entity.StatusModel;

public interface statusRepo extends JpaRepository<StatusModel, Long>{

	StatusModel findBycode(String code);
	
	List<StatusModel> findBystatusPkIn(List<Long> statusId);
}
