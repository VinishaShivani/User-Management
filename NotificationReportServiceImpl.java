package com.trb.allocationservice.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trb.allocationservice.dao.AllocationMapper;
import com.trb.allocationservice.dao.objectedQuestionsRepo;
import com.trb.allocationservice.dao.objectionAllocationRepo;
import com.trb.allocationservice.dto.GetAllocationDetailsDTO;
import com.trb.allocationservice.dto.NotificationDTO;
import com.trb.allocationservice.dto.NotificationResDTO;
import com.trb.allocationservice.dto.QuestionsListDTO;
import com.trb.allocationservice.dto.ResponseDTO;
import com.trb.allocationservice.service.NotificationReportService;
import com.trb.allocationservice.util.AppConstants;
import com.trb.allocationservice.util.UtilClass;

@Service
public class NotificationReportServiceImpl implements NotificationReportService {

	@Autowired
	objectedQuestionsRepo ObjectedQuestionsRepo;

	@Autowired
	objectionAllocationRepo allocationRepo;

	@Autowired
	AllocationMapper allocationMapper;

	@Autowired
	UtilClass utilClass;

	@Override
	public ResponseDTO getNotificationReport(String examName,String examDate,String examBatch) {
		// TODO Auto-generated method stub
		ResponseDTO response = new ResponseDTO();
		List<NotificationDTO> reportList = ObjectedQuestionsRepo.getNotificationReportDetails(examName,examDate,examBatch);
		if (reportList.isEmpty()) {
			response.setStatusCode(AppConstants.SUCCESS_RESPONSE_CODE2);
			response.setMessage(AppConstants.SUCCESS_RESPONSE_MSG2);
			response.setResponseContent(reportList);
		} else {
			List<NotificationResDTO> allaclist = utilClass.getNotification(reportList);
			allaclist = updateListWithParentData(allaclist);
			response.setResponseContent(allaclist);			
			response.setMessage("Success");
			response.setStatusCode("suc-001");
		}
		return response;
	}

	private List<NotificationResDTO> updateListWithParentData(List<NotificationResDTO> allcaList) {

		List<NotificationResDTO> updatedAllocationList = allcaList.stream().map(dto -> {
			if (dto.getParentId() == null) {
				// if parentId is null, return the element as it is
				return dto;
			} else {
				// if parentId is not null, perform the operation
				Integer qstPk = dto.getQuestionsId();
				Integer qstQbId = dto.getQbId();
				List<GetAllocationDetailsDTO> parentQstnData = allocationRepo.getParentQuestionDetails(qstPk, qstQbId);
				System.out.println("parentQstnData:" + parentQstnData);
				if (Objects.nonNull(parentQstnData)) {
					List<QuestionsListDTO> responseList = parentQstnData.stream()
							.map(parentDTO -> allocationMapper.toQuestionsListDTO(parentDTO))
							.collect(Collectors.toList());

					dto.setParentList(responseList);
				}
				return dto;
			}
		}).collect(Collectors.toList());

		return updatedAllocationList;

	}

}
