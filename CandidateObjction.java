package com.nseit.trb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CandidateObjction {
	
	public String subject;
	public Integer clientId;
	public Integer objectionPk;
	public Integer eedId;
	public String objectionText;
	public String objectionType;
	public String fileName;
	public String fileDetails;
	public Object bookPageNo;
	public Object qtpCandAns;
	public String standard;
	

}
