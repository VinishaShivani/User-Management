package com.trb.allocationservice.service;

import com.trb.allocationservice.dto.ResponseDTO;

public interface SmeActionServices {
	
	ResponseDTO getSmeActions(Long QuestionId,boolean isAdmin,Long levelId);

}
