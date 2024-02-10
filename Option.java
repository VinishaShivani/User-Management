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
public class Option  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1216035961473914704L;
	
	
//	public Integer optionId;
//	public String optionDescription;
	
	public Integer optionId;
	public String option;
	public String optionDescription;
	public String qstCrctAnsId;
	public String modAnsId;
	public Object qstOptionImage;



}
