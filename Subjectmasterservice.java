package com.nseit.subjectmasterservice.service;

import com.nseit.subjectmasterservice.dto.RequestDTO;
import com.nseit.subjectmasterservice.dto.ResponseDTO;
import com.nseit.subjectmasterservice.dto.SubjectmasterDTO;
import com.nseit.subjectmasterservice.dto.UpdatesubjectmasterDTO;

public interface Subjectmasterservice {
	ResponseDTO saveSubject(RequestDTO subjectmasterdto);

	ResponseDTO getSubjectDetails(String name, String status);

	ResponseDTO updateSubjectDetails(UpdatesubjectmasterDTO subjectDetails);

}
