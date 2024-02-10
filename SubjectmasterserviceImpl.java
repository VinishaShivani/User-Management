package com.nseit.subjectmasterservice.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nseit.subjectmasterservice.dao.StatusRepo;
import com.nseit.subjectmasterservice.dao.SubjectMapRepo;
import com.nseit.subjectmasterservice.dao.SubjectMapper;
import com.nseit.subjectmasterservice.dao.SubjectmasterRepo;
import com.nseit.subjectmasterservice.dto.RequestDTO;
import com.nseit.subjectmasterservice.dto.ResponseDTO;
import com.nseit.subjectmasterservice.dto.UpdatesubjectmasterDTO;
import com.nseit.subjectmasterservice.dto.ValidationDTO;
import com.nseit.subjectmasterservice.entity.MasterModel;
import com.nseit.subjectmasterservice.entity.StatusModel;
import com.nseit.subjectmasterservice.service.Subjectmasterservice;
import com.nseit.subjectmasterservice.util.AppConstants;
import com.nseit.subjectmasterservice.util.UtilClass;

import lombok.var;

@Service
public class SubjectmasterserviceImpl implements Subjectmasterservice {

	@Autowired
	SubjectmasterRepo subjectmasterRepo;

	@Autowired
	StatusRepo statusRepo;

	@Autowired
	SubjectMapper subjectMapper;

	@Autowired
	UtilClass utilClass;
	
	@Autowired
	SubjectMapRepo subjectMapRepo;

	@Override
	public ResponseDTO saveSubject(RequestDTO requestDTO) {
		ResponseDTO responseDTO = new ResponseDTO();
		List<ValidationDTO> Validations = new ArrayList<ValidationDTO>();
		requestDTO.getSubjectDetails().forEach(subject -> {
			ValidationDTO validationdto = new ValidationDTO();
			if (!isNullOrEmpty(subject.getName().trim())) {
//		  if(splCharCheck(subject.getName().trim())) {
					if (!isNullOrEmpty(subject.getStatus().trim())) {
						MasterModel existingSubjects = subjectmasterRepo.findByNameOrderById(subject.getName().trim());
						if (existingSubjects == null) {
							StatusModel statusModel = statusRepo.findBycode(subject.getStatus());
							MasterModel subjectDetails = subjectMapper.mapSubjectmaster(subject.getName().trim(),
									subject.getDescription(), statusModel.getStatusPk());
							subjectDetails.setCategory("SUBJECT");
							subjectmasterRepo.save(subjectDetails);
							validationdto.setName(subject.getName().trim());
							validationdto.setStatus(subject.getStatus());
							validationdto.setStatus("success");
							Validations.add(validationdto);
							validationdto.setErrorDescription("Subject Saved Successfully");
							responseDTO.setResponseContent(Validations);

						} else {
							validationdto.setErrorDescription("Subject Already Exist");
							validationdto.setName(subject.getName());
							validationdto.setStatus(subject.getStatus());
							validationdto.setStatus("failure");
							Validations.add(validationdto);
							responseDTO.setResponseContent(Validations);
						}
					} else {
						validationdto.setErrorDescription("Subject Status is Mandatory");
						validationdto.setName(subject.getName());
						validationdto.setStatus("failure");
						Validations.add(validationdto);
						responseDTO.setResponseContent(Validations);
					}

//			} else {
//				validationdto.setErrorDescription("Subject Name Contains Special Characters");
//				validationdto.setName(subject.getName());
//				validationdto.setStatus("failure");
//				Validations.add(validationdto);
//				responseDTO.setResponseContent(Validations);
//			}
		} else {
			validationdto.setErrorDescription("Subject Name is Mandatory");
			validationdto.setName(subject.getName());
			validationdto.setStatus("failure");
			Validations.add(validationdto);
			responseDTO.setResponseContent(Validations);
		  }

		});
		return getStatusMessage(responseDTO);
	}

	private boolean splCharCheck(String subject) {
		// TODO Auto-generated method stub
		
		boolean result = false ;
		Pattern p1 = Pattern.compile("[^a-zA-Z0-9&\\s]");
		Pattern p2 = Pattern.compile("^\\s|\\s$"); 
		 
		Matcher m1 = p1.matcher(subject); 
		Matcher m2 = p2.matcher(subject); 

		if (m1.find() || m2.find()) {
			result = false;
		}
		else {
			result = true;
		}
		
		return result;
	}

	public static boolean isNullOrEmpty(final String s) {
		return s == null || s == "" || s.equals("");
	}

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
			dto.setStatusCode(AppConstants.NO_SUBJECT_SUCCESS_RESPONSE_CODE);
			dto.setMessage(AppConstants.NO_SUBJECT_SUCCESS_RESPONSE_MSG);
		} else if (failureRecordCount == 0 && successRecordCount > 0) {
			dto.setStatusCode(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_CODE);
			dto.setMessage(AppConstants.SAVE_RECORD_SUCCESS_RESPONSE_MESSAGE);
		}
		return dto;
	}

	@Override
	public ResponseDTO getSubjectDetails(String name, String status) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (name != null && status != null) {
			int subjectStatus = status.toUpperCase().equalsIgnoreCase("ACTIVE") ? 12
					: status.toUpperCase().equalsIgnoreCase("INACTIVE") ? 13 : 0;
			List<MasterModel> subjectDetails = subjectmasterRepo.findAllBynameAndStatusOrderById(name, subjectStatus);
			if (subjectDetails.size() > 0) {
				List<UpdatesubjectmasterDTO> finalSubject = utilClass.mapSubjectMaster(subjectDetails);
				responseDTO.setStatusCode(AppConstants.SUCCESS_RESPONSE_CODE1);
				responseDTO.setMessage(AppConstants.SUCCESS_RESPONSE_MSG1);
				responseDTO.setResponseContent(finalSubject);
			} else {
				responseDTO.setStatusCode(AppConstants.ERROR_RESPONSE_CODE6);
				responseDTO.setMessage(AppConstants.ERROR_RESPONSE_MSG6);
				responseDTO.setResponseContent(subjectDetails);
			}
		} else {
			if (status != null) {
				int subjectStatus = status.toUpperCase().equalsIgnoreCase("ACTIVE") ? 12
						: status.toUpperCase().equalsIgnoreCase("INACTIVE") ? 13 : 0;
				List<MasterModel> subjectDetails = subjectmasterRepo.findAllBystatusOrderById(subjectStatus);
				if (subjectDetails.size() > 0) {
					List<UpdatesubjectmasterDTO> finalSubject = utilClass.mapSubjectMaster(subjectDetails);
					responseDTO.setStatusCode(AppConstants.SUCCESS_RESPONSE_CODE1);
					responseDTO.setMessage(AppConstants.SUCCESS_RESPONSE_MSG1);
					responseDTO.setResponseContent(finalSubject);
				} else {
					responseDTO.setStatusCode(AppConstants.ERROR_RESPONSE_CODE6);
					responseDTO.setMessage(AppConstants.ERROR_RESPONSE_MSG6);
					responseDTO.setResponseContent(subjectDetails);
				}
			} else if (name != null) {
				List<MasterModel> subjectDetails = subjectmasterRepo.findAllBynameOrderById(name);
				if (subjectDetails.size() > 0) {
					List<UpdatesubjectmasterDTO> finalSubject = utilClass.mapSubjectMaster(subjectDetails);
					responseDTO.setStatusCode(AppConstants.SUCCESS_RESPONSE_CODE1);
					responseDTO.setMessage(AppConstants.SUCCESS_RESPONSE_MSG1);
					responseDTO.setResponseContent(finalSubject);
				} else {
					responseDTO.setStatusCode(AppConstants.ERROR_RESPONSE_CODE6);
					responseDTO.setMessage(AppConstants.ERROR_RESPONSE_MSG6);
					responseDTO.setResponseContent(subjectDetails);
				}
			} else {
				List<MasterModel> subjectDetails = subjectmasterRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
				if (subjectDetails.size() > 0) {
					List<UpdatesubjectmasterDTO> finalSubject = utilClass.mapSubjectMaster(subjectDetails);
					responseDTO.setStatusCode(AppConstants.SUCCESS_RESPONSE_CODE1);
					responseDTO.setMessage(AppConstants.SUCCESS_RESPONSE_MSG1);
					responseDTO.setResponseContent(finalSubject);
				} else {
					responseDTO.setStatusCode(AppConstants.ERROR_RESPONSE_CODE6);
					responseDTO.setMessage(AppConstants.ERROR_RESPONSE_MSG6);
					responseDTO.setResponseContent(subjectDetails);
				}
			}
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO updateSubjectDetails(UpdatesubjectmasterDTO subjectDetails) {
		ResponseDTO responsedto = new ResponseDTO();
		if (subjectDetails.getId() != null) {		
			Optional<MasterModel> subject = subjectmasterRepo.findById(subjectDetails.getId());
			if (subject.isPresent()) {
				ValidationDTO validationdto = new ValidationDTO();
				if (!isNullOrEmpty(subjectDetails.getName().trim())) {
				 // if(splCharCheck(subjectDetails.getName().trim())) {
					//if (!isNullOrEmpty(subjectDetails.getDescription().trim())) {
						if (!isNullOrEmpty(subjectDetails.getStatus().trim())) {
							subject.get().setName(subjectDetails.getName().trim());
							//subject.get().setDescription(subjectDetails.getDescription());
							
							//checking subject already assigned to sme
							boolean subjectAssigned = subjectMapRepo.existsByrefId((long)subjectDetails.getId());
							if(!subjectAssigned) {
								StatusModel statusModel = statusRepo.findBycode(subjectDetails.getStatus());
								subject.get().setStatus(statusModel.getStatusPk().intValue());
								validationdto = saveUserInDB(subject.get());
							} else {
								validationdto = saveUserInDB(subject.get());
								if (validationdto.getStatus().equalsIgnoreCase("success") 
										&& !subjectDetails.getStatus().equals(AppConstants.ACTIVE)) {
									responsedto.setStatusCode(AppConstants.ERROR_RESPONSE_CODE20);
									responsedto.setMessage(AppConstants.ERROR_RESPONSE_MSG20);
									return responsedto;
								}
							}
							
							if (validationdto.getStatus().equalsIgnoreCase("success")) {
								responsedto.setStatusCode(AppConstants.UPDATE_RESPONSE_CODE7);
								responsedto.setMessage(AppConstants.UPDATE_RESPONSE_MSG7);
							} else {
								responsedto.setStatusCode(AppConstants.ERROR_RESPONSE_CODE14);
								responsedto.setMessage(AppConstants.ERROR_RESPONSE_MSG14);
							}
						} else {
							responsedto.setStatusCode(AppConstants.ERROR_RESPONSE_CODE16);
							responsedto.setMessage(AppConstants.ERROR_RESPONSE_MSG16);
						}

//					} else {
//						responsedto.setStatusCode(AppConstants.ERROR_RESPONSE_CODE17);
//						responsedto.setMessage(AppConstants.ERROR_RESPONSE_MSG17);
//					}

//				  } else {
//						responsedto.setStatusCode(AppConstants.ERROR_RESPONSE_CODE19);
//						responsedto.setMessage(AppConstants.ERROR_RESPONSE_MSG19);
//					}
					
				} else {
					responsedto.setStatusCode(AppConstants.ERROR_RESPONSE_CODE15);
					responsedto.setMessage(AppConstants.ERROR_RESPONSE_MSG15);
				}

			  } else {
				responsedto.setStatusCode(AppConstants.ERROR_RESPONSE_CODE13);
				responsedto.setMessage(AppConstants.ERROR_RESPONSE_MSG13);
			  }
			
			
			
		} else {
			responsedto.setStatusCode(AppConstants.ERROR_RESPONSE_CODE18);
			responsedto.setMessage(AppConstants.ERROR_RESPONSE_MSG18);
		}
		return responsedto;
	}

	private ValidationDTO saveUserInDB(MasterModel masterModel) {
		ValidationDTO validationdto = new ValidationDTO();
		try {
			subjectmasterRepo.save(masterModel);
			validationdto.setName(masterModel.getName());
			validationdto.setStatus("success");
		} catch (Exception e) {
			validationdto.setErrorDescription("subject Already Exist");
			validationdto.setName(masterModel.getName());
			validationdto.setStatus("failure");
		}
		return validationdto;
	}

}
