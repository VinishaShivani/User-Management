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
public class TotalObjectionResponseContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8301624968634501360L;
	
	public String subject;
	public Integer noofUniqueQuestion;
	public Integer noofObjection;
	
}
