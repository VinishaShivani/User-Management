package com.nseit.trb.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchInfoDTO implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3824391046556313683L;
	private String statuscode ;
	private String message;
	private List<ExamDetails> examDetails;
	public List<String> subjects;
	
	
}
