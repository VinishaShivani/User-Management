package com.trb.allocationservice.dto;

public interface NotificationDTO {
	
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
	String getFinalObjectionStatus();
	String getSectionName();
	String getTopic();
	String getauthoredCorrectAns();
	String getrevidesAns();
	Integer getparentId();

}
