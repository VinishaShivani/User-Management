package com.nseit.subjectmasterservice.dao;

import org.mapstruct.Mapper;

import com.nseit.subjectmasterservice.dto.UpdatesubjectmasterDTO;
import com.nseit.subjectmasterservice.entity.MasterModel;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
	

	MasterModel mapSubjectmaster(String name, String description, Long status);

	UpdatesubjectmasterDTO getSubjectmasterDTO(MasterModel subject);

}
