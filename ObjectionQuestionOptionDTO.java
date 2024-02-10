package com.trb.allocationservice.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectionQuestionOptionDTO implements Serializable {
 
	private int optionId;
	private String option;
	private String optionDescription;
	private String modAnsId;
	private String qstCrctAnsId;
}
