package com.nseit.trb.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseContent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4087241392555521333L;
	
	public String examName;
	public String subject;
	public Integer noofQuestion;
	public Integer noofObjection;
	public Integer noofUniqueObjection;
	public Integer noofUniqueCandidateObjection;

}
