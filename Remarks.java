package com.nseit.trb.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Remarks implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1723063621061740888L;
	
	private String standard;
    private String pageNumber;
    private List<String> docPath;
    private String remarks;

}
