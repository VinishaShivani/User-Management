package com.trb.allocationservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class QuestionAllocationDTO {
	
	private Integer objquesId;
	private Integer questionsId;
	private Integer qbId;
	private Integer clientId;
	private String examName;
	private String examBatch;
	private String examDate;
//	private Integer langId;
//	private String langName;
//	private String questionsDescription;
//	private byte[] qstImage;
	private List<QuestionsListDTO> questionsList;
	private Integer parentId;
	private List<QuestionsListDTO> parentquestionsList;
	private StatusDTO status;
	private String actionFlag;
	
}
