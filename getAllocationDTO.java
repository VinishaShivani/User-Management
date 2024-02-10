package com.trb.allocationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class getAllocationDTO {

	private String userId;
	private String name;
	private String subject;
	private String action;
	private String identifyNumber;
	private Long allocatedQuestion;
	private Long allocatedPk;
	private String status;
	private AllocationRemarksDTO remarks;
}
