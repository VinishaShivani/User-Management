package com.trb.allocationservice.dao;

import org.mapstruct.Mapper;

import com.trb.allocationservice.entity.ObjectionTrackerModel;

@Mapper(componentModel = "spring")
public interface AllocationTrackerMapper {

	ObjectionTrackerModel mapTrackerDetails(Integer subjectId, Integer levelId, String currrent, Long objquesPk);

	

}
