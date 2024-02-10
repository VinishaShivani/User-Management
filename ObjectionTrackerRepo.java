package com.trb.allocationservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trb.allocationservice.entity.ObjectionTrackerModel;


@Repository
public interface ObjectionTrackerRepo  extends JpaRepository<ObjectionTrackerModel, Long>  {

	ObjectionTrackerModel findByObjquesPkAndCurrrent(Long objquesId, String currrent);
	
	@Transactional
	@Modifying
	@Query(value ="UPDATE thrdexams.obj_tracker SET currrent='N' WHERE objques_pk =?1", nativeQuery = true)
	public void updateTrackerRecord(Integer objquesPk);

}
