package com.nseit.subjectmasterservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nseit.subjectmasterservice.entity.StatusModel;


public interface StatusRepo extends JpaRepository<StatusModel, Long> {
	
	StatusModel findBycode(String code);

}
