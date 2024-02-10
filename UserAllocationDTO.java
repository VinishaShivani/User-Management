package com.trb.allocationservice.dto;

import lombok.Data;

@Data
public class UserAllocationDTO {

	private String userId;
	private String identifyNumber;
	private String objQuestion;
	private String subject;
	private String status;
	private String standard;
	private String pageNumber;
	private String documentPath;
	private String remarks;
	private String action;
}
