package com.trb.allocationservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class CurrentStatusDTO {
	
	private Integer summaryId;
	private Integer subjectId;
	private String subject;
	private Integer levelId;
	private String level;
	private List<SmeDetailsDTO> sme;
	private Integer statusId;
	private String overallstatus;
	private Long trackingId;
}
