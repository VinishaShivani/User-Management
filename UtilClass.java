package com.nseit.subjectmasterservice.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nseit.subjectmasterservice.dao.SubjectMapper;
import com.nseit.subjectmasterservice.dto.UpdatesubjectmasterDTO;
import com.nseit.subjectmasterservice.entity.MasterModel;

@Service
public class UtilClass {

	@Autowired
	SubjectMapper subjectMapper;

	public List<UpdatesubjectmasterDTO> mapSubjectMaster(List<MasterModel> subjectDetails) {
		List<UpdatesubjectmasterDTO> finalSubject = new ArrayList<UpdatesubjectmasterDTO>();
		subjectDetails.forEach(subject -> {
			UpdatesubjectmasterDTO subjectdto = subjectMapper.getSubjectmasterDTO(subject);
			subjectdto.setStatus(subject.getStatus() == 12 ? "ACTIVE" : "INACTIVE");
			finalSubject.add(subjectdto);
		});
		return finalSubject;
	}

}
