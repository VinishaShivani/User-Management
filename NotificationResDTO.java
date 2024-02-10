package com.trb.allocationservice.dto;

import java.util.List;

import lombok.Data;
@Data
public class NotificationResDTO {

	private Integer objquesId;
	private Integer clientId;
	private String examName;
	private String examBatch;
	private String examDate;
	private Integer questionsId;
	private Integer qbId;
	private List<QuestionsListDTO> questionsList;
	private List<QuestionsListDTO> ParentList;
	private String FinalObjectionStatus;
	private String sectionName;
	private String topic;
	private String authoredCorrectAns;
	private String revisedAnsKey;
	private Integer parentId;
}
