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
public class DashBoardTiles implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -824830547379426628L;
	
	public long approved;
	public long rejected;
	public long pendingApproval;
	public long pendingAllocation;

}
