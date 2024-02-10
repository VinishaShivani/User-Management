package com.trb.allocationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmeDetailsDTO {
	
	private Long objectionAllocationPk;
	private Integer smeId;
	private String name;
	private String status;
	private String action;
	private String userId;
	private Integer levelId;
	private String level;
	
}
