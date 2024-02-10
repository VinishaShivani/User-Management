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
public class ObjectionQuestionOptionDTO implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6736643122107796206L;
	
	private int optionId;
    private String optionDescription;

}
