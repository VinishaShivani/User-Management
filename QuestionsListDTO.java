package com.trb.allocationservice.dto;

import lombok.Data;

@Data
public class QuestionsListDTO {
	
	public Integer langId;
	public String langName;
	public String questionsDescription;
	private byte[] qstImage;
	
}
