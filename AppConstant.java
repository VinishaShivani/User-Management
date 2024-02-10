package com.nseit.trb.client.constant;

public interface AppConstant {
	
	
	final String USER_ID = "userId"; 
	final String SUBJECT = "subject";  
	final String STATUS  = "status";
	
	
	//Domain Constants
	final String ALLOCATION  = "allocation";
	final String APRROVAL  = "approval";
	
	//Allocation Domain Constants
	final String STS_YTA  = "YET_TO_ALLOCATE";
	final String STS_ALLOC  = "ALLOCATED";
	
	//Approval Domain Constants
	final String STS_PENDING  = "PENDING_APPROVAL";
	final String STS_APPROVED  = "APPROVED";
	final String STS_REJECTED  = "REJECTED";
	
	//Action Constants
	final String STS_NO_ANS_KEY  = "NO_ANSWER_KEY";
	final String STS_MULTI_ANS_KEY  = "MULTIPLE_ANSWER_KEY";
	final String STS_WRONG_QSTN  = "WRONG_QUESTION";
	final String STS_ANS_KEY_CHANGE = "ANSWER_KEY_CHANGE";
	
	//Error Code and Description
	final String STS_CD_000  = "SUCC_000";
	final String STS_CD_001  = "ERR_001";
	final String STS_CD_002  = "ERR_002";
	
	
	final String STS_MSG_000  = "Success";
	final String STS_MSG_001  = "Empty Payload";
	final String STS_MSG_002  = "Record is not available";

	
	
}
