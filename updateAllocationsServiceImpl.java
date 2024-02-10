package com.trb.allocationservice.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.trb.allocationservice.dao.AllocationMapper;
import com.trb.allocationservice.dao.ObjQuestionModAnsRepo;
import com.trb.allocationservice.dao.ObjectionDocumentRepo;
import com.trb.allocationservice.dao.ObjectionSummaryRepo;
import com.trb.allocationservice.dao.UserRepo;
import com.trb.allocationservice.dao.objectedAllocationRemarksRepo;
import com.trb.allocationservice.dao.objectedQuestionsRepo;
import com.trb.allocationservice.dao.objectionAllocationRepo;
import com.trb.allocationservice.dao.statusRepo;
import com.trb.allocationservice.dto.DeleteRequestDTO;
import com.trb.allocationservice.dto.ObjectionQuestionModAnsDTO;
import com.trb.allocationservice.dto.QuestionAllocationDTO;
import com.trb.allocationservice.dto.RequestDTO;
import com.trb.allocationservice.dto.ResponseDTO;
import com.trb.allocationservice.dto.RulesengineDTO;
import com.trb.allocationservice.entity.ObjectedDocumentModel;
import com.trb.allocationservice.entity.ObjectedQuestionRemarksModel;
import com.trb.allocationservice.entity.ObjectedQuestionsAllocationModel;
import com.trb.allocationservice.entity.ObjectionQuesModAns;
import com.trb.allocationservice.entity.StatusModel;
import com.trb.allocationservice.entity.UsersModel;
import com.trb.allocationservice.service.storageService;
import com.trb.allocationservice.service.updateAllocationService;
import com.trb.allocationservice.util.AppConstants;
import com.trb.allocationservice.util.FileStorage;
import com.trb.allocationservice.util.UtilClass;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class updateAllocationsServiceImpl implements updateAllocationService {

	@Autowired
	objectionAllocationRepo objAllocation;
	
	@Autowired
	objectedAllocationRemarksRepo remarksRepo;
	
	@Autowired
	UserRepo usersRepo;
	
	@Autowired
	objectedQuestionsRepo objectedQuestionsRepo;
	
	@Autowired
    FileStorage fileStorageService;
	
	@Autowired
	statusRepo statusRepo;
	
	@Autowired
	AllocationMapper allocationMapper;
	
	@Autowired
	storageService storageService;
	
	@Autowired
	ObjectionDocumentRepo documentRepo;
	
	@Autowired
	UtilClass utilClass;
	
	@Autowired
	ObjectionSummaryRepo objAllocSummary;
	
	@Autowired
	ObjQuestionModAnsRepo objQuestionModAnsRepo;
	
	@Override
	public ResponseDTO updateAllocations(String userId, MultipartFile[] files, String identifyNumber, String objQuestion,
			String subject, String status, String standard, String pageNumber, String Objectionremarks, String documentPath,String action,String referenceUrl,String author, Long allocPK) {
		
		ResponseDTO responseDTO = new ResponseDTO();
			if(userId != null && (!userId.trim().equalsIgnoreCase(""))) {
				
				//if(subject != null && (!subject.trim().equalsIgnoreCase(""))) {
					
					if(objQuestion != null && (!objQuestion.trim().equalsIgnoreCase(""))) {
						
						log.info("userId:{}",userId);
						UsersModel users = usersRepo.findByuserId(userId);
						
						String userRole = usersRepo.getUserRoles(userId);
						System.out.println("userRole:"+userRole);
						
						
						if(users!=null) {
						StatusModel statusModel = statusRepo.findBycode(status);						
						ObjectedQuestionsAllocationModel allocation = objAllocation.findAllByObjectionAllocationPk(allocPK);						
						if(allocation!=null) {
							if(statusModel!=null) {
								allocation.setStatusId(statusModel.getStatusPk());
//								responseDTO.setMessage(AppConstants.UPDATE_RECORD_SUCCESS_RESPONSE_MESSAGE);
							}
							
							allocation.setAction(action);
							//allocation.get().setSubject(subject);
							updateAllocationsStatusInDB(allocation);
							
							String currStatus ="";
							if(!userRole.equals("ADMIN")){
								//fetch All SME's status against SummaryId before Delete Allocation Table
								QuestionAllocationDTO allSmeStatus = utilClass.getAllSmeStatusForQstn(allocation.getSummaryId());
								System.out.println("allSmeStatus:"+allSmeStatus);
							
								//Sending list of sme status to rule engine;
								RulesengineDTO rulesengineResponse = utilClass.getStatusFromRulesEngine(allSmeStatus);
								currStatus = rulesengineResponse.getResponseContent().getOverallStatus();
							}else {
								currStatus = status;
							}
							
							Integer currStatusId = null;
							log.info("currStatus:{}",currStatus);
							if(currStatus!=null || !currStatus.equals("") ) {
								statusModel = statusRepo.findBycode(currStatus);
								if(statusModel!=null) {
									currStatusId = statusModel.getStatusPk().intValue();
									objAllocSummary.updateAllocSummaryStatus(currStatusId,allocation.getSummaryId().intValue());
								}
							}
							
							//insert tracker table and tracker detail table
							if(!status.equals("")) {
							utilClass.insertTrackerTables(Integer.valueOf(objQuestion), allocation.getSummaryId(), currStatusId,AppConstants.MODE_MODIFY, allocPK);
							}
							
							//build FTP file path
							String filepath = users.getUserId()+"/"+allocation.getSummaryId()+"/"+allocation.getObjectionAllocationPk()+"/";
							String fileName = null;
							
							List<ObjectedDocumentModel> documents = new ArrayList<>();
							ObjectedQuestionRemarksModel remarks = remarksRepo.findByObjallocpk(allocation.getObjectionAllocationPk());
							if(remarks==null) {
								ObjectedQuestionRemarksModel remarksModel = allocationMapper.mapRemarksToObjections(allocation.getObjectionAllocationPk());
								saveAllocationRemarksInDB(remarksModel);
								remarks = remarksRepo.findByObjallocpk(allocation.getObjectionAllocationPk());
							}
							
							
							
							//FTP server issue
							if(files==null) {
								//deletion before save
								String result = storageService.deleteFolder(filepath);
								log.info("deleteFolder->result:{}",result);
								if(result.equals("SUCCESS")) {
									deleteDocTableEntry(remarks.getObjectionRemarksPk());
								}
								else {
									responseDTO.setMessage("Process Failed. Folder Not deleted");
									responseDTO.setStatusCode("ISS-999");
									return responseDTO;
									
								}
							}
							
							//Save files in FTP server
							if(files!=null && files.length > 0) {
								log.info(">>files.length:{}",files.length);
								for(MultipartFile file : files) {
									fileName = downloadFileAndSaveInServer(file,filepath);
									if(!fileName.equals("FAILED") && !fileName.equals("FILE_EXIST")) {
										ObjectedDocumentModel document = allocationMapper.getDocumentModel(remarks.getObjectionRemarksPk(), fileName,remarks);
										documents.add(document);
									}
								}

							}
							
							remarks = allocationMapper.updateRemarks(remarks.getObjectionRemarksPk(), allocation.getObjectionAllocationPk() ,subject, standard, pageNumber,referenceUrl,author, Objectionremarks,documents);
							UpdateRemarksInDB(remarks);
							
							// success message changed
//							responseDTO.setMessage(AppConstants.UPDATE_RECORD_SUCCESS_RESPONSE_MESSAGE);
//							responseDTO.setStatusCode(AppConstants.UPDATE_RECORD_SUCCESS_RESPONSE_CODE);
						responseDTO.setStatusCode(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_CODE);
						responseDTO.setMessage(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_MESSAGE);
							}
							else {
								log.error("Record is not available");
								responseDTO.setMessage(AppConstants.UPDATE_RECORD_NOT_FOUND_RESPONSE_MESSAGE);
								responseDTO.setStatusCode(AppConstants.UPDATE_RECORD_NOT_FOUND_RESPONSE_CODE);
							}
					  }
						else {
							responseDTO.setMessage("User is not available");
							responseDTO.setStatusCode("ISS-010");
						}
						
					}
					else {
						responseDTO.setMessage(AppConstants.OBJECTION_QUESTIONS_RESPONSE_MESSAGE);
						responseDTO.setStatusCode(AppConstants.OBJECTION_QUESTIONS_RESPONSE_CODE);
					}
//				}
//				else {
//					responseDTO.setMessage(AppConstants.SUBJECT_RESPONSE_MESSAGE);
//					responseDTO.setStatusCode(AppConstants.SUBJECT_RESPONSE_CODE);
//				}
				
			}
			else {
				responseDTO.setMessage(AppConstants.NAME_RESPONSE_MESSAGE);
				responseDTO.setStatusCode(AppConstants.NAME_RESPONSE_CODE);
			}
			
		return responseDTO;
	}

	private void deleteDocTableEntry(Long remarksPK) {
		// TODO Auto-generated method stub
		List<ObjectedDocumentModel> model = documentRepo.findObjectionDocumentPkByObjectionRemarksPk(remarksPK);			
		List<Long> ids = new ArrayList<>();
		if(model!=null) {					
			for (ObjectedDocumentModel id : model) {
				ids.add(id.getObjectionDocumentPk());
			}			
			documentRepo.deleteAllById(ids);
		}
	}

	private String downloadFileAndSaveInServer(MultipartFile file, String filepath) {
		
		String fileName = storageService.save(file,filepath);
	    return fileName;   
	
		
	}

	private void UpdateRemarksInDB(ObjectedQuestionRemarksModel remarks) {
		// TODO Auto-generated method stub
		try {
			remarksRepo.save(remarks);
		}
		catch(HibernateException e) {
			log.error(e.getMessage());
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
		
	}

	private void updateAllocationsStatusInDB(ObjectedQuestionsAllocationModel allocation) {
		// TODO Auto-generated method stub
		try {
			objAllocation.save(allocation);
			}
			catch(HibernateException e) {
				log.error(e.getMessage());
			}
			catch(Exception e) {
				log.error(e.getMessage());
			}
	}

	@Override
	public ResponseDTO updateAllocations(RequestDTO request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDTO updateAllocations(RequestDTO request, MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}
	
		
	//Helper methods
		public static boolean isNullOrEmpty( final Collection< ? > c ) {
		    return c == null || c.isEmpty();
		}

		public static boolean isNullOrEmpty( final Map< ?, ? > m ) {
		    return m == null || m.isEmpty();
		}
		
		public static boolean isNullOrEmpty( final String s ) {
		    return s == null || s == "";
		}

		@Override
		public String deleteFile(DeleteRequestDTO requestPayload) {
			// TODO Auto-generated method stub
			String result = "";
			try {
				
				Integer allocPK = 0;
				String objQuestion = "";
				String filePath = "";
				
				
				//validating payload null check
				if (Optional.ofNullable(requestPayload.getAllocPK()).orElse(0) == 0) 
				{ return result = "AllocPK Field is Mandatory"; }else { allocPK = requestPayload.getAllocPK(); }
				
				if(isNullOrEmpty(requestPayload.getObjQuestion())) 
				{ return result = "QuestionId Field is Mandatory"; }else { objQuestion = requestPayload.getObjQuestion(); }
				
				if(isNullOrEmpty(requestPayload.getFiles()))  
				{ return result = "File Path is Mandatory"; }else { filePath = requestPayload.getFiles(); }
				
				
				System.out.println("filePath:"+filePath);
				result = fileStorageService.deleteFile(filePath);
				if(result.equals("SUCCESS")) {
					result = deleteDocTableEntry(allocPK, filePath, objQuestion);
				}
				
			} catch (Exception e) {
			    e.getStackTrace();
			    throw new RuntimeException("Could not delete the File. Error: " + e.getMessage());
			}
			
			return result;
		}

		public String deleteDocTableEntry(Integer allocPK, String filePath, String objQuestion) {
			
			String result = "Document path deleteion failed";
			
			try {
				
				//UsersModel users = usersRepo.findByuserId(allocPK);
				Optional<ObjectedQuestionsAllocationModel> allocation = objAllocation.findByObjectionAllocationPk(Long.valueOf(allocPK));
				ObjectedQuestionRemarksModel remarks = remarksRepo.findByObjallocpk(allocation.get().getObjectionAllocationPk());			
		        
				List<ObjectedDocumentModel> model = documentRepo.findObjectionDocumentPkByObjectionRemarksPkAndDocumentPath(remarks.getObjectionRemarksPk(),filePath);			
		        System.out.println("model::"+model.isEmpty());
		        
		        List<Long> ids = new ArrayList<>();
				if(!model.isEmpty()) {					
					for (ObjectedDocumentModel id : model) {
						ids.add(id.getObjectionDocumentPk());
					}			
					documentRepo.deleteAllById(ids);
					System.out.println("DocPath deleted successfully");
					return "SUCCESS";
				}
			
			} catch (Exception e) {
			    e.getStackTrace();
			    throw new RuntimeException("Could not delete the DocPath. Error: " + e.getMessage());
			}
	        
	       return result;
		}

		
		@Override
		public ResponseDTO saveModifyAnswers(ObjectionQuestionModAnsDTO objectionQuestionModAns) {
			ResponseDTO response = new ResponseDTO();
			List<ObjectionQuesModAns> optionList = new ArrayList<ObjectionQuesModAns>();
			objectionQuestionModAns.getOptions().forEach(opt -> {
				ObjectionQuesModAns options = new ObjectionQuesModAns();
				options.setObjquesId(objectionQuestionModAns.getObjquesId());
				options.setModifyoptionId(opt.getOptionId());
				options.setObjallocPK(objectionQuestionModAns.getObjallocPK());
				options.setModifyDescription(opt.getOptionDescription());
				optionList.add(options);
			});
			if (optionList.isEmpty()) {
				response.setStatusCode(AppConstants.SUCCESS_RESPONSE_CODE3);
				response.setMessage(AppConstants.SUCCESS_RESPONSE_MSG3);
			} else {
				List<ObjectionQuesModAns> modifyAnswers = objQuestionModAnsRepo
						.findByObjquesIdAndObjallocPK(objectionQuestionModAns.getObjquesId(),objectionQuestionModAns.getObjallocPK());
				if (modifyAnswers.isEmpty() && !validateNullCheck(optionList)) {
					objQuestionModAnsRepo.saveAll(optionList);
					response.setStatusCode(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_CODE);
					response.setMessage(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_MESSAGE);
				} else {
					boolean deleteStatus = deleteObjQuesAnswer(modifyAnswers);
					if (deleteStatus && !validateNullCheck(optionList)) {
						objQuestionModAnsRepo.saveAll(optionList);
						response.setStatusCode(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_CODE);
						response.setMessage(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_MESSAGE);
					} else {
						response.setStatusCode(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_CODE);
						response.setMessage(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_MESSAGE);
					}
				}

			}
			return response;
		}

		private boolean validateNullCheck(List<ObjectionQuesModAns> optionList) {
			return optionList.stream().anyMatch(p -> p.getModifyoptionId()==null || p.getModifyoptionId()==0 
														|| p.getModifyDescription()==null || p.getModifyDescription().isEmpty());
		}

		private boolean deleteObjQuesAnswer(List<ObjectionQuesModAns> modifyAnswers) {
			modifyAnswers.forEach(opt -> {
				objQuestionModAnsRepo.delete(opt);
			});
			return true;
		}

		@Override
		public ResponseDTO deleteModifyAnswers(Integer objquesId, Integer objallocPK) {
			ResponseDTO response = new ResponseDTO();
			List<ObjectionQuesModAns> modifyAnswers = objQuestionModAnsRepo.findByObjquesIdAndObjallocPK(objquesId, objallocPK);
			if (modifyAnswers.isEmpty()) {
				response.setStatusCode(AppConstants.SUCCESS_RESPONSE_CODE2);
				response.setMessage(AppConstants.SUCCESS_RESPONSE_MSG2);
			} else {
				boolean deleteStatus = deleteObjQuesAnswer(modifyAnswers);
				response.setStatusCode(AppConstants.SUCCESS_RESPONSE_CODE4);
				response.setMessage(AppConstants.SUCCESS_RESPONSE_MSG4);
			}
			return response;
		}
		
		private void saveAllocationRemarksInDB(ObjectedQuestionRemarksModel remarksModel) {
			try {
				remarksRepo.save(remarksModel);
				}
				catch(HibernateException e) {
					log.error(e.getMessage());
				}
				catch(Exception e) {
					log.error(e.getMessage());
				}
		}
}
