package com.trb.allocationservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class PreviousStatusDTO {
	
	private Integer levelId;
	private List<SmeDetailsDTO> smeDetails;
	private String overallstatus;

}
