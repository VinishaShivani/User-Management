package com.trb.allocationservice.dto;


import lombok.Data;

@Data
public class RulesengineDTO {
	
	private String status;
	private String message;
	private RulesEngineStatusDTO responseContent;
}
