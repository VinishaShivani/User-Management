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
public class ReportsBySme implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2624212277454959710L;
	
	public long srlno;
	public String examName;
	public String date;
	public String batch;
	public String smeName;
	public String subject;
	public long qstnAssigned;
	public long qstnCompleted;
	
}