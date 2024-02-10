package com.trb.allocationservice.dto;

import lombok.Data;

@Data
public class LevelDTO {

	private String userId ;
	private Long smeNo ;
	private String	smeName;
	private String	smeLastName;
	private String	level;
	private String	status;
	private String  allocatedDt;
	private String actionDt;
	private RemarksDTO remarks;
}
