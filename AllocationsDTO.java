package com.trb.allocationservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllocationsDTO {
	
	private List<QuestionAllocationDTO> questionAllocation;

}
