package com.trb.allocationservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trb.allocationservice.entity.ObjectedQuestionRemarksModel;

@Repository
public interface objectedAllocationRemarksRepo extends JpaRepository<ObjectedQuestionRemarksModel, Long> {

	ObjectedQuestionRemarksModel deleteByObjallocpk(Long allocationPk);
	ObjectedQuestionRemarksModel findByObjallocpk(Long allocationPk);
}
