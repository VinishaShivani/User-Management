package com.trb.allocationservice.service;

import com.trb.allocationservice.dto.RemoveAllocationDTO;
import com.trb.allocationservice.dto.ResponseDTO;

public interface deleteAllocationService {

	ResponseDTO removeAllocatedQuestions(Integer[] questionIds);

	ResponseDTO removeUserAllocation(RemoveAllocationDTO smeRemoveDTO);
}
