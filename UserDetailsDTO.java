package com.nseit.trb.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO implements Serializable {

	public UserDetailsDTO(String string) {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -6601011229257366221L;

	public String statusCode;
	public String message;
	public List<UserDtlResponseContent> responseContent;

}
