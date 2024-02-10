package com.nseit.trb.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FinalReportResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4087241392555521333L;
	
	
//	public String examName;
//	public String examDate;
//	public String batchTime;
//	public String subject;
//	public Integer qstNo;
//	public String crctAns;
//	public Integer noofObjections;
//	public Object modQuesOption;
//	public String action;
//	public String status;
//	public String remarks;

	
	public String moduleName;
	public String examDate;
	public String examBatch;
	public String subject;
	public Integer clientId;
	public List<Question> questions = null;
	public List<CandidateObjction> candidateObjctions = null;
	public String action;
	public String status;
	public String remarks;
	public String standard;
	public String pageNo;
	public List<Document> documents = null;	
	
}
