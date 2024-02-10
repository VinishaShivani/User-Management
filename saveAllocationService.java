package com.trb.allocationservice.service;

import com.trb.allocationservice.dto.AllocationsDTO;
import com.trb.allocationservice.dto.RequestDTO;
import com.trb.allocationservice.dto.ResponseDTO;

public interface saveAllocationService {

	//ResponseDTO saveAllocations(RequestDTO requestDTO);
	
	ResponseDTO saveAllocations(AllocationsDTO allocationsDTO);
}
