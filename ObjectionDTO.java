package com.nseit.trb.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ObjectionDTO {
	
	public ObjectionDTO(String string) {
		// TODO Auto-generated constructor stub
	}

	public String statusCode;
	public String message;
	public List<ObjectionResponseContent> responseContent = null;

}
