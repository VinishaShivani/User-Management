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
public class ReportsRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7199468865928391868L;
	
	public String subject;
	public String smeName;
	public String examName;
	public String date;
	public String batch;
	public String masterQP;
		
}
