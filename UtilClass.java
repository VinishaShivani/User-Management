package com.trb.allocationservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.trb.allocationservice.dao.AllocationMapper;
import com.trb.allocationservice.dao.LevelsRepo;
import com.trb.allocationservice.dao.ObjQuestionModAnsRepo;
import com.trb.allocationservice.dao.ObjectionSummaryRepo;
import com.trb.allocationservice.dao.ObjectionTrackerDetailsRepo;
import com.trb.allocationservice.dao.ObjectionTrackerRepo;
import com.trb.allocationservice.dao.objectionAllocationRepo;
import com.trb.allocationservice.dao.statusRepo;
import com.trb.allocationservice.dto.CurrentStatusDTO;
import com.trb.allocationservice.dto.GetAllocationDetailsDTO;
import com.trb.allocationservice.dto.ModQuesOptionDTO;
import com.trb.allocationservice.dto.NotificationDTO;
import com.trb.allocationservice.dto.NotificationResDTO;
import com.trb.allocationservice.dto.QuestionAllocationDTO;
import com.trb.allocationservice.dto.QuestionsListDTO;
import com.trb.allocationservice.dto.RulesengineDTO;
import com.trb.allocationservice.dto.SmeDetailsDTO;
import com.trb.allocationservice.dto.StatusDTO;
import com.trb.allocationservice.entity.LevelsModel;
import com.trb.allocationservice.entity.ObjectedQuestionsAllocationModel;
import com.trb.allocationservice.entity.ObjectionQuesModAns;
import com.trb.allocationservice.entity.ObjectionSummaryModel;
import com.trb.allocationservice.entity.ObjectionTrackerDetails;
import com.trb.allocationservice.entity.ObjectionTrackerModel;
import com.trb.allocationservice.entity.StatusModel;

@Service
public class UtilClass {
	
	@Autowired
	AllocationMapper allocationMapper;
	
	@Autowired
	ObjectionTrackerRepo objectionTrackerRepo;
	
	@Autowired
	ObjectionTrackerDetailsRepo objectionTrackerDetailsRepo;
	
	@Value("${sme.datasource.rulesengineservice.url}")
	public String rulesengineserviceURL;
	
	@Autowired
	objectionAllocationRepo objAllocation;
	
	@Autowired
	ObjectionSummaryRepo objAllocSummary;
	
	@Autowired
	statusRepo statusRepo;
	
	@Autowired
	LevelsRepo levelsRepo;
	
	@Autowired
	ObjQuestionModAnsRepo objQuestionModAnsRepo;
	

	public List<QuestionAllocationDTO> mapAllocationDetails(List<GetAllocationDetailsDTO> allocationList) {
		List<QuestionAllocationDTO> allcaList = new ArrayList<QuestionAllocationDTO>();
//		List<QuestionsListDTO> questionsList = new ArrayList<QuestionsListDTO>();
			
		allocationList.stream().forEach(allocation -> {
		    // This block for identifying unique questions and setting values
		    Optional<QuestionAllocationDTO> optionalDuplicateAllocation = allcaList.stream()
		        .filter(duplicateAllocation -> duplicateAllocation.getObjquesId().equals(allocation.getobjquesId()))
		        .findFirst();
		    if (optionalDuplicateAllocation.isPresent()) {
		        // This block for same question have multiple languages and multiple SME allocation
		        QuestionAllocationDTO duplicateAllocation = optionalDuplicateAllocation.get();
		        List<QuestionsListDTO> questionsDTOList = duplicateAllocation.getQuestionsList();
		        QuestionsListDTO duplicateLanguage = questionsDTOList.stream()
		            .filter(p -> p.getLangId() == allocation.getlangId())
		            .findFirst().orElse(null);
		        // setting multiple languages
		        if (duplicateLanguage == null) {
		            QuestionsListDTO questionsDTO = allocationMapper.toQuestionsListDTO(allocation);
		            questionsDTOList.add(questionsDTO);
		        }
		        List<SmeDetailsDTO> smeList = duplicateAllocation.getStatus().getCurrentStatus().getSme();
		        SmeDetailsDTO duplicateSME = smeList.stream()
		            .filter(p -> p.getObjectionAllocationPk() == allocation.getobjectionAllocationPk().longValue())
		            .findFirst().orElse(null);
		        // setting multiple SMES allocated to the same question
		        if (duplicateSME == null && allocation.getsmeId() != null
		            && allocation.getobjectionAllocationPk() != 0) {
		        	SmeDetailsDTO smedto = allocationMapper.toSmeDetailsDTO(allocation);
		            smeList.add(smedto);
		        }
		        CurrentStatusDTO currentdto = duplicateAllocation.getStatus().getCurrentStatus();
		        currentdto.setSme(smeList);
		    } else {
		        // This block for a new question
		    	StatusDTO statusdto = new StatusDTO();
		    	CurrentStatusDTO currentdto = allocationMapper.allocationToCurrentStatusDTO(allocation);
		        List<SmeDetailsDTO> smeList = new ArrayList<SmeDetailsDTO>();
		        if (allocation.getsmeId() != null && allocation.getobjectionAllocationPk() != 0) {
		        	SmeDetailsDTO smedto = allocationMapper.toSmeDetailsDTO(allocation);
		            smeList.add(smedto);
		        }
		        currentdto.setSme(smeList);
		        statusdto.setCurrentStatus(currentdto);
		        
		        QuestionAllocationDTO questionAllocation = 
		        		allocationMapper.allocationToQuestionAllocationDTO(allocation,statusdto);
		        QuestionsListDTO questionsDTO = allocationMapper.toQuestionsListDTO(allocation);
		        List<QuestionsListDTO> questionList = new ArrayList<>();
		        questionList.add(questionsDTO);
		        questionAllocation.setQuestionsList(questionList);
		        
		        allcaList.add(questionAllocation);
		    }
		});
		
		
//		allocationList.stream().forEach(allocation -> {
//			
//			//This block for identifying unique questions and setting values
//			QuestionAllocationDTO duplicateAllocation = allcaList.stream()
//					.filter(duplicateallocation -> duplicateallocation.getObjquesId().equals(allocation.getobjquesId()))
//							.findFirst().orElse(null);
//			if (duplicateAllocation == null) {
//				QuestionAllocationDTO questionAllocation = new QuestionAllocationDTO();				
//				questionAllocation.setObjquesId(allocation.getobjquesId());
//				questionAllocation.setQuestionsId(allocation.getquestionsId());
//				questionAllocation.setExamDate(allocation.getexamDate());
//				questionAllocation.setExamBatch(allocation.getexamBatch());
//				questionAllocation.setClientId(allocation.getclientId());
//				questionAllocation.setExamName(allocation.getexamName());
//				questionAllocation.setQbId(allocation.getqbId());
////				questionAllocation.setLangId(allocation.getlangId());
////				questionAllocation.setLangName(allocation.getlangName());
////				questionAllocation.setQuestionsDescription(allocation.getquestionsDescription());
////				questionAllocation.setQstImage(allocation.getqstImage());
//				
//				QuestionsListDTO questionsDTO = new QuestionsListDTO();
//				questionsDTO.setLangId(allocation.getlangId());
//				questionsDTO.setLangName(allocation.getlangName());
//				questionsDTO.setQuestionsDescription(allocation.getquestionsDescription());
//				questionsDTO.setQstImage(allocation.getqstImage());
//				questionsList.add(questionsDTO);
//				
//				StatusDTO statusdto = new StatusDTO();
//				CurrentStatusDTO currentdto = new CurrentStatusDTO();
//				currentdto.setSummaryId(allocation.getsummaryId());
//				currentdto.setTrackingId(allocation.getobjtrackerPK());
//				currentdto.setSubjectId(allocation.getsubjectId());
//				currentdto.setLevelId(allocation.getlevelId());
//				currentdto.setLevel(allocation.getlevel());
//				currentdto.setSubject(allocation.getsubject());
//				if (allocation.getstatus() == null) {
//					currentdto.setOverallstatus("YET_TO_ALLOCATE");
//				} else {
//					currentdto.setOverallstatus(allocation.getstatus());
//				}
//				
//				List<SmeDetailsDTO> smeList = new ArrayList<SmeDetailsDTO>();
//				if (allocation.getsmeId() != null && allocation.getobjectionAllocationPk() != 0) {
//					SmeDetailsDTO smedto = new SmeDetailsDTO();
//					smedto.setSmeId(allocation.getsmeId());
//					smedto.setName(allocation.getname());
//					smedto.setAction(allocation.getaction());
//					smedto.setStatus(allocation.getstatusCode());
//					smedto.setObjectionAllocationPk(Long.valueOf(allocation.getobjectionAllocationPk()));
//					smeList.add(smedto);
//				}
//				currentdto.setSme(smeList);
//				statusdto.setCurrentStatus(currentdto);
//				questionAllocation.setStatus(statusdto);
//				questionAllocation.setQuestionsList(questionsList);
//				allcaList.add(questionAllocation);
//				
//			} else {
//				//This block for same question have multiple langauges and multiple SME allocation
//				List<QuestionsListDTO> questionsDTOList = duplicateAllocation.getQuestionsList();
//				QuestionsListDTO duplicateLanguage = questionsDTOList.stream()
//						.filter(p->p.getLangId()==allocation.getlangId())
//							.findFirst().orElse(null);
//				//setting multiple languages		
//				if(duplicateLanguage==null) {
//					QuestionsListDTO questionsDTO = new QuestionsListDTO();
//					questionsDTO.setLangId(allocation.getlangId());
//					questionsDTO.setLangName(allocation.getlangName());
//					questionsDTO.setQuestionsDescription(allocation.getquestionsDescription());
//					questionsDTO.setQstImage(allocation.getqstImage());
//					questionsDTOList.add(questionsDTO);
//				}
//				
//				List<SmeDetailsDTO> smeList = duplicateAllocation.getStatus().getCurrentStatus().getSme();
//				SmeDetailsDTO duplicateSME = smeList.stream()
//						.filter(p->p.getObjectionAllocationPk()==allocation.getobjectionAllocationPk().longValue())
//							.findFirst().orElse(null);
//				
//				//setting multiple smes allocated to same question
//				if (duplicateSME==null && allocation.getsmeId() != null 
//							 && allocation.getobjectionAllocationPk() != 0) {
//					SmeDetailsDTO smedto = new SmeDetailsDTO();
//					smedto.setSmeId(allocation.getsmeId());
//					smedto.setName(allocation.getname());
//					smedto.setAction(allocation.getaction());
//					smedto.setStatus(allocation.getstatusCode());
//					smedto.setObjectionAllocationPk(Long.valueOf(allocation.getobjectionAllocationPk()));
//					smeList.add(smedto);
//				}
//				CurrentStatusDTO currentdto = duplicateAllocation.getStatus().getCurrentStatus();
//				currentdto.setSme(smeList);
//			}
//				
//		});
		return allcaList;
	}
	
	
	public void allcationTrackerInsertion(QuestionAllocationDTO allocationsList) {
		System.out.println("Inside Util"+allocationsList);
		ObjectionTrackerModel getObjtrac = allocationMapper.mapAllocationTracker(
				allocationsList.getStatus().getCurrentStatus().getLevelId(),
				allocationsList.getStatus().getCurrentStatus().getSubjectId(), "Y");
//		getObjtrac.setId(allocationsList.getStatus().getCurrentStatus().getTrackingId());
		getObjtrac.setObjquesPk(allocationsList.getObjquesId());
		getObjtrac.setStatusId(allocationsList.getStatus().getCurrentStatus().getStatusId());
		objectionTrackerRepo.updateTrackerRecord(allocationsList.getObjquesId());
		ObjectionTrackerModel objtrac = objectionTrackerRepo.save(getObjtrac);
		if (objtrac.getId() != null) {
			if (!allocationsList.getStatus().getCurrentStatus().getSme().isEmpty()) {
				List<ObjectionTrackerDetails> detailsList = new ArrayList<ObjectionTrackerDetails>();
				allocationsList.getStatus().getCurrentStatus().getSme().stream().forEach(sme -> {
					ObjectionTrackerDetails objTrackerDetails = new ObjectionTrackerDetails();
					objTrackerDetails.setObjTrackerId(objtrac.getId().intValue());
//					if (allocationsList.getStatus().getCurrentStatus().getSummaryId() == null) {
//						objTrackerDetails.setMode("A");
//					} else {
//						objTrackerDetails.setMode("M");
//					}
					objTrackerDetails.setMode(allocationsList.getActionFlag());
					objTrackerDetails.setUserPk(sme.getSmeId().longValue());
					objTrackerDetails.setAction(sme.getAction());
					detailsList.add(objTrackerDetails);
				});
				objectionTrackerDetailsRepo.saveAll(detailsList);
			}
		}
	}
	
	public RulesengineDTO getStatusFromRulesEngine(QuestionAllocationDTO allocationsList) {
		try {
			System.out.println("Called RulesEngine Service............." + allocationsList);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<RulesengineDTO> result = restTemplate.postForEntity(rulesengineserviceURL, allocationsList,
					RulesengineDTO.class);
			System.out.println("Message: " + result.getStatusCode() + result.getBody().toString());
			return result.getBody();
		} catch (Exception e) {
			System.out.println("Exception occured @getStatusFromRulesEngine()" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	public QuestionAllocationDTO getAllSmeStatusForQstn(Integer summaryId) {
		
		ObjectionSummaryModel allocSummaryModel = objAllocSummary.findBySummaryId(summaryId);
		LevelsModel levelModel = levelsRepo.findByLvlId(allocSummaryModel.getLevelId().longValue());
		
		List<ObjectedQuestionsAllocationModel> allocModel = objAllocation.findAllBysummaryId(summaryId);
		
		List<StatusModel> statusModel = statusRepo.findBystatusPkIn(allocModel.stream().map(p->p.getStatusId()).collect(Collectors.toList()));
		List<String> statusCodeList =statusModel.stream().map(p->p.getCode()).collect(Collectors.toList());
		
		List<SmeDetailsDTO> objStsCode = new ArrayList<>();
		for(String objSts :statusCodeList) {
			SmeDetailsDTO obj = new SmeDetailsDTO();
			obj.setStatus(objSts); 
			objStsCode.add(obj);
		}
		QuestionAllocationDTO qstnAllocDTO = new QuestionAllocationDTO();
		StatusDTO stsDTO = new StatusDTO();
		CurrentStatusDTO currStatus = new CurrentStatusDTO();
		currStatus.setSme(objStsCode);
		currStatus.setLevel(levelModel.getLvlCode());
		stsDTO.setCurrentStatus(currStatus);
		qstnAllocDTO.setStatus(stsDTO);
		qstnAllocDTO.setObjquesId(allocSummaryModel.getObjquesId());
		return qstnAllocDTO;
	}
	
	public void insertTrackerTables(Integer objQstnId, Integer summaryId, Integer currStatusId, String actionFlag, Long allocPK) {
		ObjectionSummaryModel allocSummary = objAllocSummary.findBySummaryId(summaryId);
//		for(ObjectionSummaryModel obj : allocSummary) {
//			Integer subjectId=obj.getSubjectId();
//			Integer levelId=obj.getLevelId();
//			
//			QuestionAllocationDTO trackerObj = getTrackerTablePayload(objQstnId, summaryId, subjectId, levelId, currStatusId,actionFlag);
//			allcationTrackerInsertion(trackerObj);
//		}
		
		Integer subjectId=allocSummary.getSubjectId();
		Integer levelId=allocSummary.getLevelId();
		QuestionAllocationDTO trackerObj = getTrackerTablePayload(objQstnId, summaryId, subjectId, levelId, currStatusId,actionFlag, allocPK);
		allcationTrackerInsertion(trackerObj);
	}
	
	public QuestionAllocationDTO getTrackerTablePayload(Integer questionId, Integer summaryId,
			Integer subjectId, Integer levelId, Integer currStatusId, String actionFlag, Long allocPK) {
		QuestionAllocationDTO trackerObj = new QuestionAllocationDTO();
		StatusDTO statusObj = new StatusDTO();
		CurrentStatusDTO currStatusObj = new CurrentStatusDTO();
		
		currStatusObj.setSubjectId(subjectId);
		currStatusObj.setLevelId(levelId);
		currStatusObj.setStatusId(currStatusId);
		statusObj.setCurrentStatus(currStatusObj);
		trackerObj.setStatus(statusObj);
		trackerObj.setObjquesId(questionId);
		trackerObj.setActionFlag(actionFlag);
		
		
		List<SmeDetailsDTO> smeObjList = new ArrayList<>();
		List<ObjectedQuestionsAllocationModel> alloc = new ArrayList<>();
		if(allocPK!=null) {
			alloc = objAllocation.findAllBysummaryIdAndObjectionAllocationPk(summaryId,allocPK);
		}else {
			alloc = objAllocation.findAllBysummaryId(summaryId);
		}
		
		for(ObjectedQuestionsAllocationModel allocObj : alloc) {
		SmeDetailsDTO smeObj = new SmeDetailsDTO();
		smeObj.setSmeId(allocObj.getUserPk().intValue());
		smeObj.setAction(allocObj.getAction());
		smeObjList.add(smeObj);
		}
		trackerObj.getStatus().getCurrentStatus().setSme(smeObjList);
		return trackerObj;
	}
	
	public List<ModQuesOptionDTO> getModAnswers(Integer objquesId, Integer objAllocPK) {
		List<ObjectionQuesModAns> answers = objQuestionModAnsRepo.findByObjquesIdAndObjallocPK(objquesId, objAllocPK);
		List<ModQuesOptionDTO> modAns = new ArrayList<ModQuesOptionDTO>();
		answers.forEach(opt -> {
			ModQuesOptionDTO ans = new ModQuesOptionDTO();
			ans.setOptionId(opt.getModifyoptionId());
			ans.setOptionDescription(opt.getModifyDescription());
			modAns.add(ans);
		});
		return modAns;
	}
	
	public List<NotificationResDTO> getNotification(List<NotificationDTO> notification){
		List<NotificationResDTO> finalReport=new ArrayList<NotificationResDTO>();
		
		
		notification.stream().forEach(allocation -> {
		    // This block for identifying unique questions and setting values
		    Optional<NotificationResDTO> optionalDuplicateAllocation = finalReport.stream()
		        .filter(duplicateAllocation -> duplicateAllocation.getObjquesId().equals(allocation.getobjquesId()))
		        .findFirst();
		    if (optionalDuplicateAllocation.isPresent()) {
		        // This block for same question have multiple languages and multiple SME allocation
		    	NotificationResDTO duplicateAllocation = optionalDuplicateAllocation.get();
		        List<QuestionsListDTO> questionsDTOList = duplicateAllocation.getQuestionsList();
		        QuestionsListDTO duplicateLanguage = questionsDTOList.stream()
		            .filter(p -> p.getLangId() == allocation.getlangId())
		            .findFirst().orElse(null);
	        // setting multiple languages
	        if (duplicateLanguage == null) {
	            QuestionsListDTO questionsDTO = allocationMapper.QuestionsListDTO(allocation);
	            questionsDTOList.add(questionsDTO);
		        }
		    }else {
		        NotificationResDTO questionAllocation = 
		        		allocationMapper.toNotificationRes(allocation);
		        QuestionsListDTO questionsDTO = allocationMapper.QuestionsListDTO(allocation);
		        List<QuestionsListDTO> questionList = new ArrayList<>();
		        questionList.add(questionsDTO);
		        questionAllocation.setQuestionsList(questionList);
		        
		        finalReport.add(questionAllocation);
		    }
		});

		return finalReport ;
		
	}
}

