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
public class ReportsDTO implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7126884833205393566L;
	public String statuscode;
	public String message;
	
	public List<ReportsBySme> reportsBySme;
	
	public List<ResponseContent> reportsObjSummary;
	
	public List<FinalReportSmry> finalReportSmry ;
	
	public List<FinalReportResponse> reportsFinalObjDtl;
}
