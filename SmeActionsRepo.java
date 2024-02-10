package com.trb.allocationservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trb.allocationservice.dto.Level;
import com.trb.allocationservice.entity.ObjectedQuestionsAllocationModel;

public interface SmeActionsRepo extends JpaRepository<ObjectedQuestionsAllocationModel, Long> {
	
	//@Query(value = "select oa.objalloc_pk as objallocId, oas.objques_pk as questionId, to_char(ot.updt_dt, 'DD-MM-YYYY') as Date,u.identity_num as smeNo,u.user_id as userId,u.firstname as smefirstname,u.lastname as lastname,l.lvl_code as level,obr.objrmk_pk as Objrmk,s.code as status,oa.action as Action from thrdexams.obj_alloc as oa left join thrdexams.obj_alloc_summary as oas on  oas.id=oa.summary_id left join thrdexams.obj_tracker as ot on	oas.objques_pk = ot.objques_pk left join thrdexams.levels as l on ot.level_id = l.lvl_id left join thrdexams.users as u on oa.user_pk = u.user_pk left join thrdexams.status as s on oa.status_id=s.status_id left join thrdexams.obj_remarks as obr on obr.objalloc_pk=oa.objalloc_pk  where ot.currrent<>'Y' and oas.objques_pk=:objques_pk", nativeQuery = true)
	//List<Level> getSmeDetailsByobjectedQuestionsPk(@Param(value = "objques_pk") Long objectedQuestionsPk);
	
	@Query(value = "select distinct oa.objalloc_pk as objallocId, oas.objques_pk as questionId ,to_char(ot.updt_dt, 'DD-MM-YYYY') as Date ,u.identity_num as smeNo ,u.user_id as userId ,u.firstname as smefirstname ,u.lastname as lastname ,l.lvl_code as level,obr.objrmk_pk as Objrmk ,s.code as status ,oa.action as Action ,to_char(oa.crte_dt, 'DD-MM-YYYY') as AllocatedDt,to_char(oa.updt_dt, 'DD-MM-YYYY') as ActionDt from thrdexams.obj_alloc as oa  left join thrdexams.users as u on oa.user_pk = u.user_pk  left join thrdexams.obj_alloc_summary as oas on  oas.id=oa.summary_id  left join thrdexams.obj_tracker as ot on	oas.objques_pk = ot.objques_pk  left join thrdexams.levels as l on oas.level_id = l.lvl_id  left join thrdexams.status as s on oa.status_id=s.status_id  left join thrdexams.obj_remarks as obr on obr.objalloc_pk=oa.objalloc_pk   where oas.objques_pk=?1  and (?2 is null or oas.currrent = ?2) and (?3 is null or l.lvl_id < ?3)", nativeQuery = true)
	List<Level> getSmeDetailsByobjectedQuestionsPk(Long objectedQuestionsPk,String id,Long levelId);


}
