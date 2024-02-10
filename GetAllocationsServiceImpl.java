package com.trb.allocationservice.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trb.allocationservice.dao.AllocationMapper;
import com.trb.allocationservice.dao.ObjectionSummaryRepo;
import com.trb.allocationservice.dao.UserRepo;
import com.trb.allocationservice.dao.objectedAllocationRemarksRepo;
import com.trb.allocationservice.dao.objectedQuestionsRepo;
import com.trb.allocationservice.dao.objectionAllocationRepo;
import com.trb.allocationservice.dao.statusRepo;
import com.trb.allocationservice.dto.GetAllocationDetailsDTO;
import com.trb.allocationservice.dto.ModQuesOptionDTO;
import com.trb.allocationservice.dto.QuestionAllocationDTO;
import com.trb.allocationservice.dto.QuestionsListDTO;
import com.trb.allocationservice.dto.ResponseDTO;
import com.trb.allocationservice.dto.getAllocationDTO;
import com.trb.allocationservice.entity.ObjectedDocumentModel;
import com.trb.allocationservice.entity.ObjectedQuestionRemarksModel;
import com.trb.allocationservice.entity.ObjectedQuestionsAllocationModel;
import com.trb.allocationservice.entity.StatusModel;
import com.trb.allocationservice.entity.UsersModel;
import com.trb.allocationservice.service.getAllocationService;
import com.trb.allocationservice.util.AppConstants;
import com.trb.allocationservice.util.UtilClass;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GetAllocationsServiceImpl implements getAllocationService {

	@Autowired
	objectionAllocationRepo allocationRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	statusRepo statusRepo;
	
	@Autowired
	objectedQuestionsRepo objectedQuestionsRepo;
	
	@Autowired
	objectedAllocationRemarksRepo allocationRemarksRepo;
	
	@Autowired
	AllocationMapper allocationMapper;
	
	@Autowired
	UtilClass utilClass;
	
	
	@Autowired
	ObjectionSummaryRepo objectionSummaryRepo;
	
	
//	@Override
//	public ResponseDTO getAllocation(String userId, String status, String Subject, List<Long> questionId) {
		
		ResponseDTO responseDTO = new ResponseDTO();
//		if(userId!=null) {
//			UsersModel usersModel = userRepo.findByuserId(userId);
//			if(usersModel!=null) {
//			List<ObjectedQuestionsAllocationModel> allocationModel = allocationRepo.findAllByuserPk(usersModel.getUserPk());
//			
//			List<getAllocationDTO> allocations = new ArrayList<getAllocationDTO>();
//			if(allocationModel.size() > 0) {
//				allocationModel.stream().forEach(allocation -> {
//					if(allocation!=null) {
//						Optional<StatusModel> statusModel = statusRepo.findById(allocation.getStatusId());
//						ObjectedQuestionRemarksModel remarkModel = allocationRemarksRepo.findByObjallocpk(allocation.getObjectionAllocationPk());
//						List<String> documentPaths = remarkModel.getDocuments().stream().map(ObjectedDocumentModel :: getDocumentPath).collect(Collectors.toList());
//						List<String> fileNames = getListOfFiles(documentPaths);
//						
//						getAllocationDTO getallocation = allocationMapper.getallocationDTO(usersModel.getUserId(),allocation.getSubject(),allocation.getAction()
//							,usersModel.getIdentityNumber(),remarkModel.getStandard(),documentPaths,remarkModel.getPageNumber(),allocation.getObjectedQuestionsPk(),
//							statusModel.get().getCode(),remarkModel.getRemarks(),usersModel.getFirstName(),allocation.getObjectionAllocationPk(),fileNames);
//						
//						allocations.add(getallocation);
//					}
//				});
//				responseDTO.setResponseContent(allocations);
//				responseDTO.setMessage(AppConstants.NAME_SUCCESS_RESPONSE_MESSAGE);
//				responseDTO.setStatusCode(AppConstants.NAME_SUCCESS_RESPONSE_CODE);
//			}
//			else {
//				responseDTO.setMessage(AppConstants.NAME_RECORD_NOT_FOUND_RESPONSE_MESSAGE);
//				responseDTO.setStatusCode(AppConstants.NAME_RECORD_NOT_FOUND_RESPONSE_CODE);
//			}
//		  }
//			else {
//				responseDTO.setMessage(AppConstants.NAME_RECORD_NOT_FOUND_RESPONSE_MESSAGE);
//				responseDTO.setStatusCode(AppConstants.NAME_RECORD_NOT_FOUND_RESPONSE_CODE);
//			}
//		}
//		else if(status!=null) {
//			log.info("status :: " + status);
//			StatusModel statusModel = statusRepo.findBycode(status);
//			if(statusModel!=null) {
//			List<ObjectedQuestionsAllocationModel> allocationByStatus = allocationRepo.findAllBystatusId(statusModel.getStatusPk());
//			List<getAllocationDTO> allocations = new ArrayList<getAllocationDTO>();
//			
//			if(allocationByStatus.size() > 0) {
//				
//				allocationByStatus.stream().forEach(allocationbyStatus -> {
//					if(allocationbyStatus!=null) {
//						Optional<StatusModel> statusCode = statusRepo.findById(allocationbyStatus.getStatusId());
//						Optional<UsersModel> usersModel = userRepo.findById(allocationbyStatus.getUserPk());
//						ObjectedQuestionRemarksModel remarkModel = allocationRemarksRepo.findByObjallocpk(allocationbyStatus.getObjectionAllocationPk());
//						List<String> documentPaths = remarkModel.getDocuments().stream().map(ObjectedDocumentModel :: getDocumentPath).collect(Collectors.toList());
//						List<String> fileNames = getListOfFiles(documentPaths);
//						
//						getAllocationDTO getallocation = allocationMapper.getallocationDTO(usersModel.get().getUserId(),allocationbyStatus.getSubject(),allocationbyStatus.getAction()
//							,usersModel.get().getIdentityNumber(),remarkModel.getStandard(),documentPaths,remarkModel.getPageNumber(),allocationbyStatus.getObjectedQuestionsPk(),
//							statusCode.get().getCode(),remarkModel.getRemarks(),usersModel.get().getFirstName(),allocationbyStatus.getObjectionAllocationPk(),fileNames);
//						allocations.add(getallocation);
//					}
//				});
//				responseDTO.setResponseContent(allocations);
//				responseDTO.setMessage(AppConstants.STATUS_SUCCESS_RESPONSE_MESSAGE);
//				responseDTO.setStatusCode(AppConstants.STATUS_SUCCESS_RESPONSE_CODE);
//			}
//			else {
//				responseDTO.setMessage(AppConstants.STATUS_RECORD_NOT_FOUND_RESPONSE_MESSAGE);
//				responseDTO.setStatusCode(AppConstants.STATUS_RECORD_NOT_FOUND_RESPONSE_CODE);
//			}
//		}
//			else {
//				responseDTO.setMessage(AppConstants.STATUS_RECORD_NOT_FOUND_RESPONSE_MESSAGE);
//				responseDTO.setStatusCode(AppConstants.STATUS_RECORD_NOT_FOUND_RESPONSE_CODE);
//			}
//			
//		}
//		else if(Subject!=null) {
//			List<ObjectedQuestionsAllocationModel> allocationBySubject = allocationRepo.findAllBySubject(Subject);
//			List<getAllocationDTO> allocationsBySubject = new ArrayList<getAllocationDTO>();
//			if(allocationBySubject.size() > 0) {
//				allocationBySubject.stream().forEach(subjectAllocated -> {
//					if(subjectAllocated!=null) {
//						Optional<StatusModel> statusCode = statusRepo.findById(subjectAllocated.getStatusId());
//						Optional<UsersModel> usersModel = userRepo.findById(subjectAllocated.getUserPk());
//						ObjectedQuestionRemarksModel remarkModel = allocationRemarksRepo.findByObjallocpk(subjectAllocated.getObjectionAllocationPk());
//						List<String> documentPaths = remarkModel.getDocuments().stream().map(ObjectedDocumentModel :: getDocumentPath).collect(Collectors.toList());
//						List<String> fileNames = getListOfFiles(documentPaths);
//						
//						getAllocationDTO getallocation = allocationMapper.getallocationDTO(usersModel.get().getUserId(),subjectAllocated.getSubject(),subjectAllocated.getAction()
//							,usersModel.get().getIdentityNumber(),remarkModel.getStandard(),documentPaths,remarkModel.getPageNumber(),subjectAllocated.getObjectedQuestionsPk(),
//							statusCode.get().getCode(),remarkModel.getRemarks(),usersModel.get().getFirstName(),subjectAllocated.getObjectionAllocationPk(),fileNames);
//						allocationsBySubject.add(getallocation);
//					}
//				});
//				responseDTO.setResponseContent(allocationsBySubject);
//				responseDTO.setMessage(AppConstants.SUBJECT_RESPONSE_MESSAGE);
//				responseDTO.setStatusCode(AppConstants.SUBJECT_RESPONSE_CODE);
//			}
//			else {
//				responseDTO.setMessage(AppConstants.SUBJECT_RECORD_NOT_FOUND_RESPONSE_MESSAGE);
//				responseDTO.setStatusCode(AppConstants.SUBJECT_RECORD_NOT_FOUND_RESPONSE_CODE);
//			}
//		}
//		else if(questionId.size()>0) {
//			List<getAllocationDTO> allocations = new ArrayList<getAllocationDTO>();
//			questionId.stream().forEach(onjectionQuestionsPK -> {
//				ObjectedQuestionsModel questionsModel = objectedQuestionsRepo.findByobjectedQuestionsPk(onjectionQuestionsPK);
//				if(questionsModel!=null) {
//					ObjectedQuestionsAllocationModel allocationsByquestionId = allocationRepo.findByObjectedQuestionsPk(questionsModel.getObjectedQuestionsPk());
//					Optional<StatusModel> statusCode = statusRepo.findById(allocationsByquestionId.getStatusId());
//					Optional<UsersModel> usersModel = userRepo.findById(allocationsByquestionId.getUserPk());
//					ObjectedQuestionRemarksModel remarkModel = allocationRemarksRepo.findByObjallocpk(allocationsByquestionId.getObjectionAllocationPk());
//					List<String> documentPaths = remarkModel.getDocuments().stream().map(ObjectedDocumentModel :: getDocumentPath).collect(Collectors.toList());
//					List<String> fileNames = getListOfFiles(documentPaths);
//					
//					getAllocationDTO getallocation = allocationMapper.getallocationDTO(usersModel.get().getUserId(),allocationsByquestionId.getSubject(),allocationsByquestionId.getAction()
//						,usersModel.get().getIdentityNumber(),remarkModel.getStandard(),documentPaths,remarkModel.getPageNumber(),allocationsByquestionId.getObjectedQuestionsPk(),
//						statusCode.get().getCode(),remarkModel.getRemarks(),usersModel.get().getFirstName(),allocationsByquestionId.getObjectionAllocationPk(),fileNames);
//					allocations.add(getallocation);
//					//objectedQuestionsModel.add(allocationsByquestionId);
//					responseDTO.setResponseContent(allocations);
//					responseDTO.setMessage(AppConstants.QUESTIONID_SUCCESS_RESPONSE_MESSAGE);
//					responseDTO.setStatusCode(AppConstants.QUESTIONID_SUCCESS_RESPONSE_CODE);
//				}
//			});
//		}
//		else {
//			responseDTO.setMessage(AppConstants.RECORD_IS_NOT_AVAILABLE_CODE);
//			responseDTO.setStatusCode(AppConstants.RECORD_IS_NOT_AVAILABLE_CODE_MESSAGE);
//		}
//		return responseDTO;
	//}

	private List<String> getListOfFiles(List<String> documentPaths) {
		// TODO Auto-generated method stub
		List<String> fileList = new ArrayList<>();
		for(String str : documentPaths) {
			String[] splittedStr = str.split("/");
			System.out.print("fileList:"+splittedStr);
			fileList.add(splittedStr[splittedStr.length-1]);
		}
		
		return fileList; 
	}

	@Override
	public ResponseDTO getAllocation() {
		ResponseDTO response = new ResponseDTO();
//		
//		List<ObjectedQuestionsAllocationModel> allAllocationModel = allocationRepo.findAll();
//		List<getAllocationDTO> allocations = new ArrayList<getAllocationDTO>();
//		if(allAllocationModel.size() > 0) {
//			allAllocationModel.stream().forEach(allocation -> {
//				Optional<StatusModel> statusCode = statusRepo.findById(allocation.getStatusId());
//				Optional<UsersModel> usersModel = userRepo.findById(allocation.getUserPk());
//				ObjectedQuestionRemarksModel remarkModel = allocationRemarksRepo.findByObjallocpk(allocation.getObjectionAllocationPk());
//				List<String> documentPaths = remarkModel.getDocuments().stream().map(ObjectedDocumentModel :: getDocumentPath).collect(Collectors.toList());
//				List<String> fileNames = getListOfFiles(documentPaths);
//				
//				getAllocationDTO getallocation = allocationMapper.getallocationDTO(usersModel.get().getUserId(),allocation.getSubject(),allocation.getAction()
//						,usersModel.get().getIdentityNumber(),remarkModel.getStandard(),documentPaths,remarkModel.getPageNumber(),allocation.getObjectedQuestionsPk(),
//						statusCode.get().getCode(),remarkModel.getRemarks(),usersModel.get().getFirstName(),allocation.getObjectionAllocationPk(),fileNames);
//				allocations.add(getallocation);
//			});
//			response.setResponseContent(allocations);
//			response.setMessage(AppConstants.ALLOCATIONS_SUCCESS_RESPONSE_MESSAGE);
//			response.setStatusCode(AppConstants.ALLOCATIONS_SUCCESS_RESPONSE_CODE);
//		}
//		else {
//			response.setMessage(AppConstants.NOAllOCATIONS_RESPONSE_MESSAGE);
//			response.setStatusCode(AppConstants.NOAllOCATIONS_RESPONSE_CODE);
//		}
		
		return response;
	}
	
	@Override
	public ResponseDTO getAllocation(String status, String subject, String examName,
			String examDate, String examBatch, String level, String userId, String objQuestion, String smeStatus, boolean showPrevious) {
		ResponseDTO response = new ResponseDTO();
		
		//Default currStatus as 'Y' 
		//if showPrevious is true then null value passed to get all levels
		String currStatus = "Y";
		if(showPrevious)
			currStatus = null;
		
		List<GetAllocationDetailsDTO> allocationList = allocationRepo.getAllocationDetails(status, subject,
				examName, examDate, examBatch,level, userId, objQuestion, smeStatus, currStatus);
		if (allocationList.isEmpty()) {
			response.setStatusCode(AppConstants.SUCCESS_RESPONSE_CODE2);
			response.setMessage(AppConstants.SUCCESS_RESPONSE_MSG2);
			response.setResponseContent(allocationList);
		} else {
			List<QuestionAllocationDTO> allcaList = utilClass.mapAllocationDetails(allocationList);
			allcaList = updateListWithParentData(allcaList);
			response.setStatusCode(AppConstants.SUCCESS_RESPONSE_CODE1);
			response.setMessage(AppConstants.SUCCESS_RESPONSE_MSG1);
			response.setResponseContent(allcaList);
		}
		return response;
	}
	
	@Override
	public ResponseDTO getAllocation(List<Long> questionId, Long objAllocPK) {
		
		ResponseDTO responseDTO = new ResponseDTO();

		if(questionId.size()>0) {
			List<getAllocationDTO> allocations = new ArrayList<getAllocationDTO>();
			questionId.stream().forEach(objQuestionPk -> {
				//ObjectedQuestionsModel questionsModel = objectedQuestionsRepo.findByobjectedQuestionsPk(objQuestionPk);
				//if(questionsModel!=null) {
					ObjectedQuestionsAllocationModel allocationsByquestionId = allocationRepo.findAllByObjectionAllocationPk(objAllocPK);
					Optional<StatusModel> statusCode = statusRepo.findById(allocationsByquestionId.getStatusId());
					Optional<UsersModel> usersModel = userRepo.findById(allocationsByquestionId.getUserPk());
					ObjectedQuestionRemarksModel remarkModel = allocationRemarksRepo.findByObjallocpk(allocationsByquestionId.getObjectionAllocationPk());
//					System.out.println("remarkModel:"+remarkModel);
					getAllocationDTO getallocation;
					if(remarkModel!=null) {
						List<String> documentPaths = remarkModel.getDocuments().stream().map(ObjectedDocumentModel :: getDocumentPath).collect(Collectors.toList());
						List<String> fileNames = getListOfFiles(documentPaths);					
						getallocation = allocationMapper.getallocationDTO(usersModel.get().getUserId(),"",allocationsByquestionId.getAction(),
						usersModel.get().getIdentityNumber(),remarkModel.getStandard(),documentPaths,remarkModel.getPageNumber(),remarkModel.getAuthor(),remarkModel.getReferenceUrl(),objQuestionPk,
						statusCode.get().getCode(),remarkModel.getRemarks(),usersModel.get().getFirstName(),allocationsByquestionId.getObjectionAllocationPk(),fileNames);
						getallocation.getRemarks().setRmkSubject(remarkModel.getSubject());			
					} else  {
						List<String> emptyList = Collections.emptyList();
						getallocation = allocationMapper.getallocationDTO(usersModel.get().getUserId(),"",
						allocationsByquestionId.getAction(),usersModel.get().getIdentityNumber(),"",emptyList,"","","",objQuestionPk,
						statusCode.get().getCode(),"",usersModel.get().getFirstName(),allocationsByquestionId.getObjectionAllocationPk(),emptyList);
						getallocation.getRemarks().setRmkSubject("");
					}
					String subject=objectedQuestionsRepo.getSubject(objQuestionPk);
					getallocation.setSubject(subject);
					List<ModQuesOptionDTO> modifyAnswersList = utilClass.getModAnswers(objQuestionPk.intValue(),objAllocPK.intValue());
					System.out.print("modifyAnswersList"+modifyAnswersList);
					getallocation.getRemarks().setModans(modifyAnswersList);
					
					allocations.add(getallocation);
					//objectedQuestionsModel.add(allocationsByquestionId);
					responseDTO.setResponseContent(allocations);
					responseDTO.setMessage(AppConstants.QUESTIONID_SUCCESS_RESPONSE_MESSAGE);
					responseDTO.setStatusCode(AppConstants.QUESTIONID_SUCCESS_RESPONSE_CODE);
				//}
			});
		}
		else {
			responseDTO.setMessage(AppConstants.RECORD_IS_NOT_AVAILABLE_CODE);
			responseDTO.setStatusCode(AppConstants.RECORD_IS_NOT_AVAILABLE_CODE_MESSAGE);
		}
		return responseDTO;
	}
	
	private List<QuestionAllocationDTO> updateListWithParentData(List<QuestionAllocationDTO> allcaList) {	
		
		List<QuestionAllocationDTO> updatedAllocationList = allcaList.stream()
		        .map(dto -> {
		            if (dto.getParentId() == null) {
		                // if parentId is null, return the element as it is
		                return dto;
		            } else {
		                // if parentId is not null, perform the operation
		                Integer qstPk = dto.getQuestionsId();
		                Integer qstQbId = dto.getQbId();
		                List<GetAllocationDetailsDTO> parentQstnData = allocationRepo.getParentQuestionDetails(qstPk, qstQbId);
		                System.out.println("parentQstnData:"+parentQstnData);
		                if (Objects.nonNull(parentQstnData)) {
		                    List<QuestionsListDTO> responseList = parentQstnData.stream()
		                        .map(parentDTO -> allocationMapper.toQuestionsListDTO(parentDTO))
		                        .collect(Collectors.toList());
		                    
		                    dto.setParentquestionsList(responseList);
		                }
		                return dto;
		            }
		        })
		        .collect(Collectors.toList());
	
        return updatedAllocationList;
		
    }
	
}
