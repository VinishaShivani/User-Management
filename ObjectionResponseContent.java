package com.nseit.trb.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectionResponseContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8301624968634501360L;
	
	public Integer objquesId;
	public Integer qstMdmId;
	public String moduleName;
	public Integer qbId;
	public String subject;
	//public Integer QuestionsId;
	public Integer clientId;
	public String examDate;
	public String examBatch;
	//public String questionsDescription;
	//public List<Option> options;
	public List<CandidateObjction> candidateObjctions;
	public List<Question> questions;

}
