package com.trb.allocationservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class RemarksDTO {
	
	private String action;
	private String subject;
	private String standard;
	private String pageNo;
	private String remarks;
	private String referenceUrl;
	private String author;
	private List<String> docpath;
	private List<String> fileNames;
	private String resSelectedBySME;

}
