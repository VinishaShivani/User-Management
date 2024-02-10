package com.trb.allocationservice.service;

import java.util.List;

import com.trb.allocationservice.dto.ResponseDTO;

public interface getAllocationService {

	ResponseDTO getAllocation(String status,String subject,String examName,String examDate, String examBatch, String level, String userId, String objQuestion, String smeStatus, boolean showPrevious);

	ResponseDTO getAllocation();
	
	ResponseDTO getAllocation(List<Long> questionId,Long objAllocPK);
}
