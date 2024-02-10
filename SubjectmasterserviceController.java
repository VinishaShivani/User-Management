package com.nseit.subjectmasterservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nseit.subjectmasterservice.dto.RequestDTO;
import com.nseit.subjectmasterservice.dto.ResponseDTO;
import com.nseit.subjectmasterservice.dto.SubjectmasterDTO;
import com.nseit.subjectmasterservice.dto.UpdatesubjectmasterDTO;
import com.nseit.subjectmasterservice.service.Subjectmasterservice;
import com.nseit.subjectmasterservice.util.AppConstants;

@RestController
@RequestMapping("/subjectmaster")
public class SubjectmasterserviceController {

	@Autowired
	Subjectmasterservice subjectmasterservice;

	@PostMapping(path = "/subject/save")
	public ResponseDTO addsubjectMaster(@RequestBody RequestDTO subjectDetails) {
		ResponseDTO response = new ResponseDTO();

		if (subjectDetails.getSubjectDetails().size() > 0) {
			response = subjectmasterservice.saveSubject(subjectDetails);
		} else {
			response.setMessage(AppConstants.NOSUBJECT_RESPONSE_CODE);
			response.setStatusCode(AppConstants.NOSUBJECT_RESPONSE_MESSAGE);
		}

		return response;
	}

	@GetMapping("/subject/find")
	public ResponseDTO findsubjectMaster(@RequestHeader(value = "name", required = false) String name,
			@RequestHeader(value = "status", required = false) String status) {
		ResponseDTO response = new ResponseDTO();
		response = subjectmasterservice.getSubjectDetails(name, status);

		return response;
	}

	@PostMapping("/subject/update")
	public ResponseDTO updatesubjectMaster(@RequestBody UpdatesubjectmasterDTO subjectDetails) {
		ResponseDTO response = new ResponseDTO();

		response = subjectmasterservice.updateSubjectDetails(subjectDetails);

		return response;
	}
}
