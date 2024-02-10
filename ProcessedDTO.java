package com.trb.allocationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessedDTO {

	private Integer questionIds;
	private String message;
}
