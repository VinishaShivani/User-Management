package com.trb.allocationservice.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.trb.allocationservice.dto.AllocationsDTO;
import com.trb.allocationservice.dto.DeleteRequestDTO;
import com.trb.allocationservice.dto.ObjectionQuestionModAnsDTO;
import com.trb.allocationservice.dto.RemoveAllocationDTO;
import com.trb.allocationservice.dto.ResponseDTO;
import com.trb.allocationservice.service.NotificationReportService;
import com.trb.allocationservice.service.SmeActionServices;
import com.trb.allocationservice.service.deleteAllocationService;
import com.trb.allocationservice.service.downloadService;
import com.trb.allocationservice.service.getAllocationService;
import com.trb.allocationservice.service.saveAllocationService;
import com.trb.allocationservice.service.storageService;
import com.trb.allocationservice.service.updateAllocationService;
import com.trb.allocationservice.util.AppConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/allocation")
public class AllocationServiceController {
	
	@Autowired
	saveAllocationService saveAllocationsService;
	
	@Autowired
	deleteAllocationService deleteAllocationService;
	
	@Autowired
	updateAllocationService updateAllocationService;
	
	@Autowired
	getAllocationService getAllocationService;
	
	@Autowired
	storageService storageService;
	
	@Autowired
	downloadService downloadService;
	
	@Autowired
	SmeActionServices smeActionServices;
	
	@Autowired
	NotificationReportService notificationReportService;

//	@PostMapping("/saveAllocations")
//	public ResponseDTO addUser(@RequestBody RequestDTO userAllocations) {
//
//		ResponseDTO response = new ResponseDTO();
//		
//		if(userAllocations.getUserAllocation().size() > 0) {
//			response = saveAllocationsService.saveAllocations(userAllocations);
//		}
//		else {
//			response.setMessage(AppConstants.NOAllOCATIONS_RESPONSE_MESSAGE);
//			response.setStatusCode(AppConstants.NOAllOCATIONS_RESPONSE_CODE);
//		}
//		
//		return response;
//	}
	
	@PostMapping("/saveAllocations")
	public ResponseDTO addAllocations(@RequestBody AllocationsDTO questionAllocation) {

		ResponseDTO response = new ResponseDTO();
		
		if(questionAllocation.getQuestionAllocation().size() > 0) {
			response = saveAllocationsService.saveAllocations(questionAllocation);
		}
		else {
			response.setMessage(AppConstants.NOAllOCATIONS_RESPONSE_MESSAGE);
			response.setStatusCode(AppConstants.NOAllOCATIONS_RESPONSE_CODE);
		}
		
		return response;
	}
	
	@DeleteMapping("/removeAllocation/{questionIds}")
	public ResponseDTO removeAllocatedQuestions(@PathVariable Integer[] questionIds ) {
		ResponseDTO response = new ResponseDTO();
		
		if(questionIds.length > 0) {
			response = deleteAllocationService.removeAllocatedQuestions(questionIds);
		}
		else {
			response.setMessage(AppConstants.DELETE_AllOCATIONS_RESPONSE_MESSAGE);
			response.setStatusCode(AppConstants.DELETE_AllOCATIONS_RESPONSE_CODE);
		}
		
		return response;
	}
	
	@PostMapping("/removeUserAllocation")
	public ResponseDTO removeUserAllocation(@RequestBody RemoveAllocationDTO smeRemoveDTO) {
		ResponseDTO response = new ResponseDTO();
		
		if(smeRemoveDTO!=null) {
			response = deleteAllocationService.removeUserAllocation(smeRemoveDTO);
		}
		else {
			response.setMessage(AppConstants.DELETE_AllOCATIONS_RESPONSE_MESSAGE);
			response.setStatusCode(AppConstants.DELETE_AllOCATIONS_RESPONSE_CODE);
		}
		
		return response;
	}
	

	@PostMapping("/updateApproval")
	public ResponseDTO updateAllocationStatus(@RequestParam(value="userId", required=true) String userId,@RequestParam(value="files", required=false) MultipartFile[] files,
			@RequestParam(value="identifyNumber", required=false) String identifyNumber,@RequestParam(value="objQuestion", required=true) String objQuestion,@RequestParam(value="subject", required=true) String subject,
			@RequestParam(value="status", required=false) String status,@RequestParam(value="standard", required=false) String standard,@RequestParam(value="pageNumber", required=false) String pageNumber,
			@RequestParam(value="remarks", required=false) String remarks,@RequestParam(value="documentPath", required=false) String documentPath,
			@RequestParam(value="action", required=false) String action,@RequestParam(value="referenceUrl", required=false) String referenceUrl,@RequestParam(value="author", required=false) String author,
			@RequestParam(value="allocPK", required=false) Long allocPK) {
		ResponseDTO response = new ResponseDTO();
		response = updateAllocationService.updateAllocations(userId,files,identifyNumber,objQuestion,subject,status,standard,pageNumber,remarks,documentPath,action,referenceUrl,author, allocPK);
		return response;
	}
	
	@GetMapping("/getAllocations")
	public ResponseDTO getAllocations(
			@RequestParam(value = "status" , required = false) String status,
			@RequestParam(value = "subject" , required = false) String subject,
			@RequestParam(value = "examName" , required = false) String examName,
			@RequestParam(value = "examDate" , required = false) String examDate,
			@RequestParam(value = "examBatch" , required = false) String examBatch,
			@RequestParam(value = "level" , required = false) String level,
			@RequestParam(value = "userId" , required = false) String userId,
			@RequestParam(value = "objQuestion" , required = false) String objQuestion,
			@RequestParam(value = "smeStatus" , required = false) String smeStatus,
			@RequestParam(value = "showPrevious" , required = false) boolean showPrevious){
		ResponseDTO response = new ResponseDTO();
		
		response = getAllocationService.getAllocation(status,subject, examName,examDate,examBatch,level,userId, objQuestion, smeStatus,showPrevious);
		
		return response;
	}
	
	@GetMapping("/getAllAllocations")
	public ResponseDTO getAllocations() {
		ResponseDTO response = new ResponseDTO();
		
		response = getAllocationService.getAllocation();
		
		return response;
	}
	
	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> downloadFile(@RequestHeader(value="filePath",required=true) String filePath,HttpServletRequest request) throws IOException {
		
		return downloadService.downloadFile(request,filePath);
	}
	
	@PostMapping("/deleteFile")
	public ResponseDTO deleteFile(@RequestBody DeleteRequestDTO requestPayload) {
		
		
		ResponseDTO response = new ResponseDTO();
		String result = updateAllocationService.deleteFile(requestPayload);
		
		if(result.equals("SUCCESS")) {
			response.setStatusCode(AppConstants.UPDATE_RECORD_SUCCESS_RESPONSE_CODE);
			response.setMessage("File deleted successfully!");
		}
		else if (result.equals("FILE_NOT_EXIST")) {
			response.setStatusCode(AppConstants.UPDATE_RECORD_NOT_FOUND_RESPONSE_CODE);
			response.setMessage("File not exist!");	
		}
		else if (result.equals("FIELD_MANDATE")) {
			response.setStatusCode(AppConstants.UPDATE_RECORD_NOT_FOUND_RESPONSE_CODE);
			response.setMessage("Mandatory Field Missing");	
		}
		else{
			response.setStatusCode(AppConstants.UPDATE_RECORD_NOT_FOUND_RESPONSE_CODE);
			response.setMessage("File deletion failed!");
		}
	
		return response;
	}
	
	@GetMapping("/getSmeActionDetails")
	public ResponseDTO getSmeActions(@RequestParam(value = "objquesId" , required = false) Long QuestionId,
			@RequestParam(value = "isAdmin" , required = false) boolean isAdmin,
			@RequestParam(value = "levelId" , required = false) long levelId) {
		ResponseDTO reponse=new ResponseDTO();
		reponse=smeActionServices.getSmeActions(QuestionId,isAdmin,levelId);
		return reponse;
		
	}
	
	@GetMapping("/getSMEAction")
	public ResponseDTO getAllocations(
			@RequestParam(value = "objQuestionId", required = false) List<Long> objectionQuestionId,
			@RequestParam(value = "objAllocPK", required = false) Long objAllocPK) {
		ResponseDTO response = new ResponseDTO();
		
		response = getAllocationService.getAllocation(objectionQuestionId, objAllocPK);
		
		return response;
	}
	
	@PostMapping("/saveModifyAnswers")
	public ResponseDTO saveModifyAnswers(@RequestBody ObjectionQuestionModAnsDTO objectionQuestionModAns)
			throws Exception {

		ResponseDTO reponse = updateAllocationService.saveModifyAnswers(objectionQuestionModAns);

		return reponse;
	}

	@GetMapping("/deleteModifyAnswers")
	public ResponseDTO deleteModifyAnswers(
			@RequestParam(value="objquesId", required=false) Integer objquesId,
			@RequestParam(value="allocPK", required=false) Integer allocPK) throws Exception {
		ResponseDTO reponse = updateAllocationService.deleteModifyAnswers(objquesId, allocPK);
		return reponse;
	}
	@GetMapping("/getNotificationReport")
	public ResponseDTO getNotificationDetails(
			@RequestParam(value = "examName" , required = false) String examName,
			@RequestParam(value = "examDate" , required = false) String examDate,
			@RequestParam(value = "examBatch" , required = false) String examBatch) {
		ResponseDTO response=new ResponseDTO();
		response=notificationReportService.getNotificationReport(examName,examDate,examBatch);
		return response;
	
	}
}
