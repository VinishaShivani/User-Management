package com.trb.allocationservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class FinalStatusDTO {
	
	private Integer levelId;
	private List<SmeDetailsDTO> smeDetails;
	private String overallstatus;
}
