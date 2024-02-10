package com.nseit.trb.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class UserDtlResponseContent implements Serializable {

	
	public Integer userPk;
	public String userId;
	public String password;
	public String identityNumber;
	public String firstName;
	public String subject;
	public String expiryDate;
	public String userCrteDate;
	public String userUpdtDt;
	public Integer valdityInDays;
	
}
