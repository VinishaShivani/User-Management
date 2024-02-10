package com.trb.allocationservice.dto;

import org.jetbrains.annotations.Nullable;

public interface GetAllocationDetailsDTO {
	
	Integer getobjquesId();
	Integer getquestionsId();
	Integer getqbId();
	Integer getclientId();
	String getexamDate();
	String getexamBatch();
	String getexamName();
	Integer getlangId();
	String getlangName();
	String getquestionsDescription();
	byte[] getqstImage();
	@Nullable Integer getobjectionAllocationPk();
	Integer getsubjectId();
	Integer getlevelId();
	String getsubject();
	String getlevel();
	Integer getsmeId();
	String getname();
	String getstatus();
	String getaction();
	Integer getsummaryId();
	Long getobjtrackerPK();
	String getstatusid();
	String getstatusCode();
	Integer getqst_pid();
	

}
