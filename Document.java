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
public class Document  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1216035961473914704L;
	
	public Integer rmkPk;
	public String docPath;


}
