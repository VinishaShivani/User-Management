package com.nseit.trb.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class AllocationQuestion implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 6738374182955683879L;
	
	private AllocationResponseContent allocationResponseContent;
	private ObjectionResponseContent objectionResponseContent;
	

}
