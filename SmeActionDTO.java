package com.trb.allocationservice.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmeActionDTO {
	
	private Long objQuesId ;
	private String Date;
	private List<LevelDTO> level;
	private List<SmeDetailsDTO> sme;
	private String summaryId;

}
