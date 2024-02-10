package com.trb.allocationservice.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trb.allocationservice.entity.ObjectionQuesModAns;

public interface ObjQuestionModAnsRepo extends JpaRepository<ObjectionQuesModAns, Integer> {
	

	List<ObjectionQuesModAns> findByObjquesIdAndObjallocPK(Integer objquesId,Integer objallocPK);

}
