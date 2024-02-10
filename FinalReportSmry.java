package com.nseit.trb.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FinalReportSmry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2624212277454959710L;
	
	public String examName;
	public String subject;
	public long noofQuestions;
	public long noofObjectionRcvd;
	public long noofUniqueCandidates;
	public long noofUniqueQuestions;
	public long approved;
	public long noAnswerKey;
	public long ansKeyChange;
	public long multipleAnsKey;
	public long wrongQuestion;
	
}