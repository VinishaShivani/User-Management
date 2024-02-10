package com.nseit.trb.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportsObjSummary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4490741231112346866L;
	
	public String statusCode;
	public String message;
	public List<ResponseContent> responseContent ;

}
