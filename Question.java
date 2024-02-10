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
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1216035961473914704L;
	
	public String language;
	public Integer questionsId;
	public String questionsDescription;
	public List<Option> options;
	public Object qstImage;


}
