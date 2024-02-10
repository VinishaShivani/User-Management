package com.trb.allocationservice.service.impl;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trb.allocationservice.dao.AllocationMapper;
import com.trb.allocationservice.dao.AllocationTrackerMapper;
import com.trb.allocationservice.dao.LevelsRepo;
import com.trb.allocationservice.dao.ObjectionSummaryRepo;
import com.trb.allocationservice.dao.ObjectionTrackerDetailsRepo;
import com.trb.allocationservice.dao.ObjectionTrackerRepo;
import com.trb.allocationservice.dao.UserRepo;
import com.trb.allocationservice.dao.objectedAllocationRemarksRepo;
import com.trb.allocationservice.dao.objectedQuestionsRepo;
import com.trb.allocationservice.dao.objectionAllocationRepo;
import com.trb.allocationservice.dao.statusRepo;
import com.trb.allocationservice.dto.AllocationsDTO;
import com.trb.allocationservice.dto.QuestionAllocationDTO;
import com.trb.allocationservice.dto.ResponseDTO;
import com.trb.allocationservice.dto.RulesEngineStatusDTO;
import com.trb.allocationservice.dto.RulesengineDTO;
import com.trb.allocationservice.dto.ValidationDTO;
import com.trb.allocationservice.entity.LevelsModel;
import com.trb.allocationservice.entity.ObjectedQuestionsAllocationModel;
import com.trb.allocationservice.entity.ObjectionSummaryModel;
import com.trb.allocationservice.entity.ObjectionTrackerModel;
import com.trb.allocationservice.entity.StatusModel;
import com.trb.allocationservice.service.saveAllocationService;
import com.trb.allocationservice.util.AppConstants;
import com.trb.allocationservice.util.UtilClass;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class saveAllocationServiceImpl implements saveAllocationService {

	@Autowired
	objectionAllocationRepo objAllocation;
	
	
	@Autowired
	objectedAllocationRemarksRepo remarksRepo;
	
	@Autowired
	UserRepo usersRepo;
	
	@Autowired
	objectedQuestionsRepo objectedQuestionsRepo;
	
	@Autowired
	statusRepo statusRepo;
	
	@Autowired
	AllocationMapper allocationMapper;
	
	@Autowired
	AllocationTrackerMapper allocationTrackerMapper;
	
	
	@Autowired
	ObjectionTrackerRepo objectionTrackerRepo;
	
	@Autowired
	ObjectionSummaryRepo objectionSummaryRepo;
	
	@Autowired
	ObjectionTrackerDetailsRepo objectionTrackerDetailsRepo;
	
//	@Value("${sme.datasource.rulesengineservice.url}")
//	public String rulesengineserviceURL;

	
	@Autowired
	UtilClass utilClass;
	
	@Autowired
	LevelsRepo levelsRepo;
	
	
	//@Override
//	public ResponseDTO saveAllocations(RequestDTO requestDTO) {
//		
//		ResponseDTO responseDTO = new ResponseDTO();
//		
//		requestDTO.getUserAllocation().stream().forEach(allocationsList -> {
//
//			if(allocationsList.getUserId() != null && (!allocationsList.getUserId().trim().equalsIgnoreCase(""))) {
//				
//				if(allocationsList.getSubject() != null && (!allocationsList.getSubject().trim().equalsIgnoreCase(""))) {
//					
//					if(allocationsList.getObjQuestion() != null && (!allocationsList.getObjQuestion().trim().equalsIgnoreCase(""))) {
//						
//						UsersModel users = usersRepo.findByuserId(allocationsList.getUserId());
//						ObjectedQuestionsModel objectedModel = objectedQuestionsRepo.findByobjectedQuestionsPk(Long.valueOf(allocationsList.getObjQuestion()));
//						StatusModel statusModel = statusRepo.findBycode(allocationsList.getStatus());
//						ObjectedQuestionsAllocationModel questionsallocation =allocationMapper.mapQuestionsToUser(users.getUserPk(),statusModel.getStatusPk(),
//								Long.valueOf(allocationsList.getObjQuestion()),allocationsList.getSubject());
//						try {
//							questionsallocation.setStatus(statusModel);
//							questionsallocation.setUsers(users);
//							questionsallocation.setObjQuestions(objectedModel);
//							ObjectedQuestionsAllocationModel objQuestionsModel = objAllocation.findByObjectedQuestionsPk(Long.valueOf(allocationsList.getObjQuestion()));
//							if(objQuestionsModel==null) {
//								saveAllocationsInDB(questionsallocation);
//							}
//							else {
//								questionsallocation.setObjectionAllocationPk(objQuestionsModel.getObjectionAllocationPk());
//								saveAllocationsInDB(questionsallocation);
//							}
//							Long allocatedPk = getAllocationKeyFromDB(questionsallocation);
//							ObjectedQuestionRemarksModel remarksModel = allocationMapper.mapRemarksToObjections(allocatedPk);
//							System.out.println("remarksModel:"+remarksModel);
//							
//							ObjectedQuestionRemarksModel remarksObj = remarksRepo.findByObjallocpk(remarksModel.getObjallocpk());
//							System.out.println("remarksObj:"+remarksObj);
//							
//							if(remarksObj!=null) {
//								remarksObj.setUserUpdtDt(new Timestamp(System.currentTimeMillis()));
//								saveAllocationRemarksInDB(remarksObj);
//							}
//							else{
//								saveAllocationRemarksInDB(remarksModel);
//							}
//							
//							responseDTO.setMessage(AppConstants.SUCCESS_RESPONSE_MESSAGE);
//							responseDTO.setStatusCode(AppConstants.SUCCESS_RESPONSE_CODE);
//						} catch (SQLDataException e) {
//							e.printStackTrace();
//							responseDTO.setMessage(AppConstants.Execption_RESPONSE_MESSAGE);
//							responseDTO.setStatusCode(AppConstants.Execption_RESPONSE_CODE);
//						}
//						
//					}
//					else {
//						responseDTO.setMessage(AppConstants.OBJECTION_QUESTIONS_RESPONSE_MESSAGE);
//						responseDTO.setStatusCode(AppConstants.OBJECTION_QUESTIONS_RESPONSE_CODE);
//					}
//				}
//				else {
//					responseDTO.setMessage(AppConstants.SUBJECT_RESPONSE_MESSAGE);
//					responseDTO.setStatusCode(AppConstants.SUBJECT_RESPONSE_CODE);
//				}
//				
//			}
//			else {
//				responseDTO.setMessage(AppConstants.NAME_RESPONSE_MESSAGE);
//				responseDTO.setStatusCode(AppConstants.NAME_RESPONSE_CODE);
//			}
//		});
//		
//		return responseDTO;
//	}
//
//	private void saveAllocationRemarksInDB(ObjectedQuestionRemarksModel remarksModel) {
//		try {
//			remarksRepo.save(remarksModel);
//			}
//			catch(HibernateException e) {
//				log.error(e.getMessage());
//			}
//			catch(Exception e) {
//				log.error(e.getMessage());
//			}
//	}

//	private Long getAllocationKeyFromDB(ObjectedQuestionsAllocationModel questionsallocation) {
//		
//		ObjectedQuestionsAllocationModel allocation = objAllocation.findByuserPkAndStatusIdAndObjectedQuestionsPk(questionsallocation.getUserPk(), questionsallocation.getStatusId(), questionsallocation.getObjectedQuestionsPk());
//		return allocation.getObjectionAllocationPk();
//	}
//
	private void saveAllocationsInDB(ObjectedQuestionsAllocationModel questionsallocation) throws SQLDataException {
		try {
		objAllocation.save(questionsallocation);
		}
		catch(HibernateException e) {
			log.error(e.getMessage());
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
	}
    
	private void saveAllocationsTrackerInDB(ObjectionTrackerModel objectionTrackerModel) throws SQLDataException {
		try {
			objectionTrackerRepo.save(objectionTrackerModel);
		}
		catch(HibernateException e) {
			log.error(e.getMessage());
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
	}
	
	@Override
	public ResponseDTO saveAllocations(AllocationsDTO allocationsDTO) {
		ResponseDTO responseDTO = new ResponseDTO();
		List<ValidationDTO> Validations = new ArrayList<ValidationDTO>();
		allocationsDTO.getQuestionAllocation().stream().forEach(allocationsList -> {
			ValidationDTO validationdto = new ValidationDTO();
			
			if(allocationsList.getStatus().getCurrentStatus().getLevelId()!=null) {
				LevelsModel levelModel = levelsRepo.findByLvlId(allocationsList.getStatus().getCurrentStatus().getLevelId().longValue());
				allocationsList.getStatus().getCurrentStatus().setLevel(levelModel.getLvlCode());
			}
			
			if(allocationsList.getStatus().getCurrentStatus().getOverallstatus()!=null) {
				StatusModel statusModel = statusRepo
					.findBycode(allocationsList.getStatus().getCurrentStatus().getOverallstatus());
				allocationsList.getStatus().getCurrentStatus().setStatusId(statusModel.getStatusPk().intValue());
			} else {
				validationdto.setStatus("failed. over all status is empty");
				Validations.add(validationdto);
				responseDTO.setResponseContent(Validations);
			}
			
			//if allocationsList's sme.size==1 then check sme role for ADMIN allocation process
			//else consider as SME allocation
			String userRole = "";
			if(allocationsList.getStatus().getCurrentStatus().getSme().size()==1) {
				String userId = allocationsList.getStatus().getCurrentStatus().getSme().stream()
                    .map(user -> String.valueOf(user.getUserId()))
                    .collect(Collectors.joining(", "));
				System.out.println("userId:"+userId);
			
				userRole = usersRepo.getUserRoles(userId);
				System.out.println("userRole:"+userRole);
			}
			
			//Inserting into allocation summary & alloc tables
			allcationInsertion(allocationsList);
			
			RulesengineDTO rulesengineResponse = new RulesengineDTO();
			if(!userRole.equals("") && userRole.equals("ADMIN")) {
				RulesEngineStatusDTO overAllStatusDTO = new RulesEngineStatusDTO();
				rulesengineResponse.setMessage("Success");
				overAllStatusDTO.setOverallStatus(allocationsList.getStatus().getCurrentStatus().getOverallstatus());
				rulesengineResponse.setResponseContent(overAllStatusDTO);
			}else {
				if(allocationsList.getStatus().getCurrentStatus().getSummaryId()!=null && allocationsList.getStatus().getCurrentStatus().getLevelId()  !=null) {
					QuestionAllocationDTO allSmeStatus = utilClass.getAllSmeStatusForQstn(allocationsList.getStatus().getCurrentStatus().getSummaryId());
					System.out.println("allSmeStatus:"+allSmeStatus);
					rulesengineResponse = utilClass.getStatusFromRulesEngine(allSmeStatus);
				}else{
					rulesengineResponse = utilClass.getStatusFromRulesEngine(allocationsList);
				}
			}
			
			
			System.out.println(">>CurrentStatus:"+rulesengineResponse.getResponseContent().getOverallStatus());
			if (rulesengineResponse.getMessage().equalsIgnoreCase("Success")) {
				
				if(rulesengineResponse.getResponseContent().getOverallStatus()!=null) {
					StatusModel statusModel = statusRepo
						.findBycode(rulesengineResponse.getResponseContent().getOverallStatus());
					allocationsList.getStatus().getCurrentStatus().setStatusId(statusModel.getStatusPk().intValue());
					System.out.println(">>statusModel:"+statusModel.getStatusPk().intValue());
					ObjectionSummaryModel summaryModel  = objectionSummaryRepo.findByObjquesIdAndCurrrentEquals(allocationsList.getObjquesId(),"Y");
					objectionSummaryRepo.updateAllocSummaryStatus(statusModel.getStatusPk().intValue(),summaryModel.getSummaryId());
				}
				
				//Setting mode 
				if(allocationsList.getStatus().getCurrentStatus().getSummaryId() == null) {
					allocationsList.setActionFlag("A");
				}else {
					allocationsList.setActionFlag("M");
				}
				
				utilClass.allcationTrackerInsertion(allocationsList);
				validationdto.setStatus("success");
				Validations.add(validationdto);
				responseDTO.setResponseContent(Validations);
			} else {
				validationdto.setStatus("failure");
				Validations.add(validationdto);
				responseDTO.setResponseContent(Validations);
			}
		});
		return getStatusMessage(responseDTO);
	}

//	private RulesengineDTO getStatusFromRulesEngine(QuestionAllocationDTO allocationsList) {
//		try {
//			System.out.println("Called RulesEngine Service............." + allocationsList);
//
//			RestTemplate restTemplate = new RestTemplate();
//			ResponseEntity<RulesengineDTO> result = restTemplate.postForEntity(rulesengineserviceURL, allocationsList,
//					RulesengineDTO.class);
//			System.out.println("Message: " + result.getStatusCode() + result.getBody().toString());
//			return result.getBody();
//		} catch (Exception e) {
//			System.out.println("Exception occured @getStatusFromRulesEngine()" + e.getMessage());
//			e.printStackTrace();
//		}
//
//		return null;
//	}

	private ResponseDTO getStatusMessage(ResponseDTO dto) {
		List<ValidationDTO> validationdto = (List<ValidationDTO>) dto.getResponseContent();
		Long successRecordCount = validationdto.stream().filter(data -> data.getStatus().equalsIgnoreCase("success"))
				.count();
		Long failureRecordCount = validationdto.stream().filter(data -> data.getStatus().equalsIgnoreCase("failure"))
				.count();
		if (successRecordCount > 0 && failureRecordCount > 0) {
			dto.setStatusCode(AppConstants.PARTIAL_SUCCESS_RESPONSE_CODE);
			dto.setMessage(AppConstants.PARTIAL_SUCCESS_RESPONSE_MSG);
		} else if (successRecordCount == 0) {
			dto.setStatusCode(AppConstants.NO_RECORD_SUCCESS_RESPONSE_CODE);
			dto.setMessage(AppConstants.NO_RECORD_SUCCESS_RESPONSE_MSG);
		} else if (failureRecordCount == 0 && successRecordCount > 0) {
			dto.setStatusCode(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_CODE);
			dto.setMessage(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_MESSAGE);
		}
		return dto;
	}

	private void allcationInsertion(QuestionAllocationDTO allocationsList) {

		ObjectionSummaryModel getSummaryDetails = allocationMapper.mapSummaryDetails(
				allocationsList.getStatus().getCurrentStatus().getSummaryId(),
				allocationsList.getObjquesId().longValue(),
				allocationsList.getStatus().getCurrentStatus().getSubjectId(),
				allocationsList.getStatus().getCurrentStatus().getLevelId(), "Y",
				allocationsList.getStatus().getCurrentStatus().getStatusId());
		
		objectionSummaryRepo.updateSummaryRecord(allocationsList.getObjquesId());
		ObjectionSummaryModel summaryDetails = objectionSummaryRepo.save(getSummaryDetails);

		if (!allocationsList.getStatus().getCurrentStatus().getSme().isEmpty()) {
			List<ObjectedQuestionsAllocationModel> allocationList = new ArrayList<ObjectedQuestionsAllocationModel>();
			allocationsList.getStatus().getCurrentStatus().getSme().stream().forEach(sme -> {
				StatusModel statusModel = statusRepo.findBycode(sme.getStatus());
				ObjectedQuestionsAllocationModel mapAllocationDetails = allocationMapper.mapAllocationDetails(
						sme.getObjectionAllocationPk(), sme.getAction(), summaryDetails.getSummaryId(),
						statusModel.getStatusPk().intValue());
				mapAllocationDetails.setUserPk(sme.getSmeId().longValue());

				allocationList.add(mapAllocationDetails);
			});
			objAllocation.saveAll(allocationList);
		}
	}

//	private void allcationTrackerInsertion(QuestionAllocationDTO allocationsList) {
//		ObjectionTrackerModel getObjtrac = allocationMapper.mapAllocationTracker(
//				allocationsList.getStatus().getCurrentStatus().getLevelId(),
//				allocationsList.getStatus().getCurrentStatus().getSubjectId(), "Y");
////		getObjtrac.setId(allocationsList.getStatus().getCurrentStatus().getTrackingId());
//		getObjtrac.setObjquesPk(allocationsList.getObjquesId());
//		getObjtrac.setStatusId(allocationsList.getStatus().getCurrentStatus().getStatusId());
//		objectionTrackerRepo.updateTrackerRecord(allocationsList.getObjquesId());
//		ObjectionTrackerModel objtrac = objectionTrackerRepo.save(getObjtrac);
//		if (objtrac.getId() != null) {
//			if (!allocationsList.getStatus().getCurrentStatus().getSme().isEmpty()) {
//				List<ObjectionTrackerDetails> detailsList = new ArrayList<ObjectionTrackerDetails>();
//				allocationsList.getStatus().getCurrentStatus().getSme().stream().forEach(sme -> {
//					ObjectionTrackerDetails objTrackerDetails = new ObjectionTrackerDetails();
//					objTrackerDetails.setObjTrackerId(objtrac.getId().intValue());
//					if (allocationsList.getStatus().getCurrentStatus().getSummaryId() == null) {
//						objTrackerDetails.setMode("A");
//					} else {
//						objTrackerDetails.setMode("M");
//					}
//					objTrackerDetails.setUserPk(sme.getSmeId().longValue());
//					detailsList.add(objTrackerDetails);
//				});
//				objectionTrackerDetailsRepo.saveAll(detailsList);
//			}
//		}
//	}

}
