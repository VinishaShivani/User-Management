package com.trb.allocationservice.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModQuesOptionDTO implements Serializable {
	private int optionId;
	private String optionDescription;
}
