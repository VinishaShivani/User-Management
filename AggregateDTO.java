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
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregateDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1403008034020700901L;
	
	
//	private AllocationResponseContent allocationResponseContent;
//	private ObjectionResponseContent objectionResponseContent;
	
	private String statuscode;
	private String message;
	public List<AllocationQuestion> allocationQuestion = null;
	


}
