package com.trb.allocationservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectionQuestionModAnsDTO {
	
	private int objquesId;
	private int objallocPK;
	private List<ObjectionQuestionOptionDTO> options;

}
