package com.trb.allocationservice.service;

import com.trb.allocationservice.dto.ResponseDTO;

public interface NotificationReportService {
	
	ResponseDTO getNotificationReport(String examName,String examDate,String examBatch);

}
