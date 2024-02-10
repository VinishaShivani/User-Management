package com.trb.allocationservice.dto;

import java.sql.Date;
import java.util.List;

public interface Level {	
	 Long getobjallocId();
	 Long getquestionId();
	 String getDate();
	 Long getsmeNo() ;
	 String getuserId() ;
	 String	getsmefirstname();
	 String	getlastname();
	 String	getlevel();
	 String	getstatus();
	 String getAllocatedDt();
	 String getActionDt();
	 String getAction();
	 Long getObjrmk();
}
