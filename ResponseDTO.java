package com.nseit.subjectmasterservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
	
	private String statusCode;
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> errors;
	private Object responseContent;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Object> responseContents;

}
