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
public class DashBoardBySme implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 2624212277454959710L;
	
	public String subject;
	public String smeName;
	public long allocatedQuestions;
	public long approved;
	public long rejected;
	public long pendingApproval;
	public long expiryDays;
	
}
