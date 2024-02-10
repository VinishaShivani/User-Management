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
public class TotalObjectionDTO implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7092725143698490539L;


	public String statusCode;
	public String message;
	public TotalObjectionResponseContent responseContent;

}
