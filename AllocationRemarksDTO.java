package com.trb.allocationservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL )
public class AllocationRemarksDTO {

	private String standard;
	private String pageNumber;
	private List<String> docPath;
	private List<String> fileNames;
	private String remarks;
	private String rmkSubject;
	private String author;
	private String referenceUrl;	
	private List<ModQuesOptionDTO> modans;
}
