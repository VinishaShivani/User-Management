package com.trb.allocationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusDTO {
	
	private CurrentStatusDTO CurrentStatus;
	private PreviousStatusDTO PreviousStatus;
	private FinalStatusDTO finalStatus;   	
}
