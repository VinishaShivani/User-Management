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
public class AllocationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7199468865928391868L;
	
	public String statusCode;
	public String message;
	public List<AllocationResponseContent> responseContent ;
		
}
