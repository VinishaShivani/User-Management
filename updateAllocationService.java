package com.trb.allocationservice.service;

import org.springframework.web.multipart.MultipartFile;

import com.trb.allocationservice.dto.DeleteRequestDTO;
import com.trb.allocationservice.dto.ObjectionQuestionModAnsDTO;
import com.trb.allocationservice.dto.RequestDTO;
import com.trb.allocationservice.dto.ResponseDTO;

public interface updateAllocationService {

	ResponseDTO updateAllocations(RequestDTO request, MultipartFile file);
	
	ResponseDTO updateAllocations(RequestDTO request);

	ResponseDTO updateAllocations(String userId, MultipartFile[] files, String identifyNumber, String objQuestion,
			String subject, String status, String standard, String pageNumber, String remarks, String documentPath, String action,String referenceUrl,String author, Long allocPK);

	String deleteFile(DeleteRequestDTO requestPayload);
	
	ResponseDTO saveModifyAnswers(ObjectionQuestionModAnsDTO objectionQuestionModAns);
	
	ResponseDTO deleteModifyAnswers(Integer objquesId, Integer allocPK);
}
