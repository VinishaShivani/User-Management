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
public class RequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7199468865928391868L;
	
	public String userId;
	public String subject;
	public String status;
	public String smeId;
	public String date;
	public String batch;
	public String examName;
}
