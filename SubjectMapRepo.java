package com.nseit.subjectmasterservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nseit.subjectmasterservice.entity.UserSubjectLevelModel;

public interface SubjectMapRepo extends JpaRepository<UserSubjectLevelModel, Long> {

	boolean existsByrefId(Long refId);
	
}
