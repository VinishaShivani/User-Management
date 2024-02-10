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
public class AllocationResponseContent implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -554201323085221311L;
	
	public String userId;
	public String name;
	public String subject;
	public Integer allocatedQuestion;
	public String status;
	public Remarks remarks;
	public String identifyNumber;
	private Long allocatedPk;
	private String action; 
	private String examName;
	public String examDate;
	public String examBatch;
	

}
