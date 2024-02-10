package com.trb.allocationservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationDTO {
    
	String status;
	@JsonIgnore
	List<String> errorDescriptionList;
	String errorDescription;
}
