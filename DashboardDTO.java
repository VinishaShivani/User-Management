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
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5684597277399422198L;
	
	public String statuscode;
	public String message;
	public List<DashBoardBySubject> dashBoardBySubject = null;
	public List<DashBoardBySme> dashBoardBySme = null;
	public DashBoardTiles dashBoardTiles;

}
