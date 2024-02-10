package com.trb.allocationservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.trb.allocationservice.entity.ObjectedQuestionsAllocationModel;
import com.trb.allocationservice.entity.ObjectionSummaryModel;



public interface ObjectionSummaryRepo extends JpaRepository<ObjectionSummaryModel, Integer>  {

	@Transactional
	@Modifying
	@Query(value ="UPDATE thrdexams.obj_alloc_summary SET currrent='N' WHERE objques_pk =?1", nativeQuery = true)
	public void updateSummaryRecord(Integer objquesPk);

	public List<ObjectionSummaryModel> findByObjquesId(Integer objQstnId);

	public void deleteBySummaryIdIn(List<Integer> allocSmryPkList);
	
	@Transactional
	@Modifying
	@Query(value ="UPDATE thrdexams.obj_alloc_summary SET status_id=?1 WHERE id =?2", nativeQuery = true)
	public void updateAllocSummaryStatus(Integer status, Integer summaryId);
	
	public ObjectionSummaryModel findBySummaryId(Integer summaryId);

	public ObjectionSummaryModel findByObjquesIdAndCurrrentEquals(Integer objquesId, String flag);

}
