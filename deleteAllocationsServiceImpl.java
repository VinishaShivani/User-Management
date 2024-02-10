package com.trb.allocationservice.service.impl;



import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trb.allocationservice.dao.ObjectionSummaryRepo;
import com.trb.allocationservice.dao.UserRepo;
import com.trb.allocationservice.dao.objectedAllocationRemarksRepo;
import com.trb.allocationservice.dao.objectedQuestionsRepo;
import com.trb.allocationservice.dao.objectionAllocationRepo;
import com.trb.allocationservice.dao.statusRepo;
import com.trb.allocationservice.dto.ProcessedDTO;
import com.trb.allocationservice.dto.QuestionAllocationDTO;
import com.trb.allocationservice.dto.RemoveAllocationDTO;
import com.trb.allocationservice.dto.ResponseDTO;
import com.trb.allocationservice.dto.RulesengineDTO;
import com.trb.allocationservice.dto.SmeActionDTO;
import com.trb.allocationservice.dto.SmeDetailsDTO;
import com.trb.allocationservice.entity.ObjectedQuestionsAllocationModel;
import com.trb.allocationservice.entity.ObjectionSummaryModel;
import com.trb.allocationservice.entity.StatusModel;
import com.trb.allocationservice.service.deleteAllocationService;
import com.trb.allocationservice.util.AppConstants;
import com.trb.allocationservice.util.UtilClass;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class deleteAllocationsServiceImpl implements deleteAllocationService {

	@Autowired
	objectionAllocationRepo objAllocation;
	
	@Autowired
	ObjectionSummaryRepo objAllocSummary;
	
	@Autowired
	UserRepo usersRepo;
	
	@Autowired
	objectedQuestionsRepo objectedQuestionsRepo;
	
	@Autowired
	objectedAllocationRemarksRepo remarksRepo;
	
	@Autowired
	statusRepo statusRepo;
	
	@Autowired
	UtilClass utilClass;
	
	@Autowired
    EntityManager entityManager;
	
	@Override
	@Transactional
	public ResponseDTO removeAllocatedQuestions(Integer[] questionIds) {
		
		ResponseDTO responseDTO = new ResponseDTO();
		List<ProcessedDTO> processedUsersList = new ArrayList<>();
		for(Integer questionId : questionIds) {

		try {
			List<Integer> allocSmryPkList = getAllocSummaryPKList(questionId);
			if(allocSmryPkList!=null && !allocSmryPkList.isEmpty()) {
				
				List<ObjectionSummaryModel> allocSummary = objAllocSummary.findByObjquesId(questionId);
				for(ObjectionSummaryModel obj : allocSummary) {
					Integer subjectId=obj.getSubjectId();
					Integer levelId=obj.getLevelId();
					Integer summaryId=obj.getSummaryId();
					
					QuestionAllocationDTO trackerObj = utilClass.getTrackerTablePayload(questionId, summaryId, subjectId, levelId, 
																	AppConstants.YET_TO_ALLOCATE,AppConstants.MODE_REMOVE,null);
					utilClass.allcationTrackerInsertion(trackerObj);
				}	
				deleteAllocation(allocSmryPkList);
				
				ProcessedDTO processedUsers = new ProcessedDTO();
				responseDTO.setMessage(AppConstants.DELETE_SUCCESS_RESPONSE_MESSAGE);
				responseDTO.setStatusCode(AppConstants.DELETE_SUCCESS_RESPONSE_CODE);
				processedUsers.setQuestionIds(questionId);
				processedUsers.setMessage("Allocation questions are deleted");
				processedUsersList.add(processedUsers);
				responseDTO.setResponseContent(processedUsersList);
			}
			else {
				responseDTO.setMessage(AppConstants.DELETE_ALLOCATION_RECORD_NOT_AVAILABLE_MESSAGE);
				responseDTO.setStatusCode(AppConstants.DELETE_ALLOCATION_RECORD_NOT_AVAILABLE_CODE);
				ProcessedDTO processedUsers = new ProcessedDTO();
				processedUsers.setQuestionIds(questionId);
				processedUsers.setMessage("Allocated questions are not deleted from table");
				processedUsersList.add(processedUsers);
				responseDTO.setResponseContent(processedUsersList);
			}
						  
		}
		catch(HibernateException | SQLDataException e) {
			e.printStackTrace();
			responseDTO.setMessage(AppConstants.Execption_RESPONSE_MESSAGE);
			responseDTO.setStatusCode(AppConstants.Execption_RESPONSE_CODE);
		}
	}
		
		return responseDTO;
	}

	
	@Transactional
	private void deleteAllocation(List<Integer> allocSmryPkList) throws SQLDataException {
//		ObjectedQuestionRemarksModel remarkModel = remarksRepo.findByObjallocpk(allocatedPk);
//		remarksRepo.delete(remarkModel);
//		objAllocation.deleteById(allocatedPk);
		
		objAllocation.deleteBySummaryIdIn(allocSmryPkList);
		objAllocSummary.deleteBySummaryIdIn(allocSmryPkList);
	}

	
	private List<Integer> getAllocSummaryPKList(Integer objQstnId) {
		System.out.println("getAllocSummaryPKList");
		List<ObjectionSummaryModel> allocSummary = objAllocSummary.findByObjquesId(objQstnId);
		List<Integer> sumryIds=null;
		if(allocSummary!=null) {
			sumryIds = allocSummary.stream().map(p->p.getSummaryId()).collect(Collectors.toList());
		}
		else {
			log.error("Record is not available");
		}
		
		return sumryIds;
	}

	@Override
	public ResponseDTO removeUserAllocation(RemoveAllocationDTO smeRemoveDTO) {
		
		ResponseDTO responseDTO = new ResponseDTO();
		
		smeRemoveDTO.getRemoveAllocation().stream().forEach(smeRemove -> { 
		
		Integer objQstnId = smeRemove.getObjQuesId().intValue(); 
		Integer summaryId =  Integer.valueOf(smeRemove.getSummaryId());
		
		try {
			
			//delete Allocation Table
			deleteSMEAllocationByAllocPk(smeRemove);
			
			//fetch All SME's status against SummaryId before Delete Allocation Table
			QuestionAllocationDTO allSmeStatus = utilClass.getAllSmeStatusForQstn(summaryId);
			System.out.println("allSmeStatus:"+allSmeStatus);
			
			//Sending list of sme status to rule engine;
			RulesengineDTO rulesengineResponse = utilClass.getStatusFromRulesEngine(allSmeStatus);
			String currStatus = rulesengineResponse.getResponseContent().getOverallStatus();
			Integer currStatusId = 0;
			System.out.println(">>CurrentStatus:"+currStatus);
			
			if(currStatus!=null) {
				StatusModel statusModel = statusRepo.findBycode(currStatus);
				if(statusModel!=null) {
					currStatusId = statusModel.getStatusPk().intValue();
					objAllocSummary.updateAllocSummaryStatus(currStatusId,summaryId);
				}
			}
		
			if(currStatusId!=0) {
				//insert tracker table and tracker detail table
				utilClass.insertTrackerTables(objQstnId, summaryId, currStatusId,AppConstants.MODE_REMOVE,null);
			}
			
			responseDTO.setMessage(AppConstants.DELETE_SUCCESS_RESPONSE_MESSAGE);
			responseDTO.setStatusCode(AppConstants.DELETE_SUCCESS_RESPONSE_CODE);
			
		} catch(HibernateException | SQLDataException e) {
			e.printStackTrace();
			responseDTO.setMessage(AppConstants.Execption_RESPONSE_MESSAGE);
			responseDTO.setStatusCode(AppConstants.Execption_RESPONSE_CODE);
		}
		
		});
		
		return responseDTO;
	}

	@Transactional
	private void deleteAllocTableBySummaryId(Integer objQstnId) {
		List<Integer> allocSmryPkList = getAllocSummaryPKList(objQstnId);
		objAllocation.deleteBySummaryIdIn(allocSmryPkList);
	}

	
	@Transactional 
	private void deleteSMEAllocationByAllocPk(SmeActionDTO smeRemoveDTO) throws SQLDataException {
		List<Long> allocPKList = smeRemoveDTO.getSme().stream().map(SmeDetailsDTO::getObjectionAllocationPk).collect(Collectors.toList());
		System.out.println("allocPKList:"+allocPKList);
		
//		objAllocation.deleteByobjectionAllocationPkIn(allocPKList);
		
		List<ObjectedQuestionsAllocationModel> models = objAllocation.findAllById(allocPKList);
		objAllocation.deleteAllInBatch(models);
		
    }
	

}
