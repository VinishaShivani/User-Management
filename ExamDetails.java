package com.nseit.trb.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamDetails implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7285845854726748651L;
	private String qbId;
	private String examName;
	private String date;
	private List<String> batch;
	
}
