package com.trb.allocationservice.dao;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.trb.allocationservice.dto.CurrentStatusDTO;
import com.trb.allocationservice.dto.GetAllocationDetailsDTO;
import com.trb.allocationservice.dto.LevelDTO;
import com.trb.allocationservice.dto.NotificationDTO;
import com.trb.allocationservice.dto.NotificationResDTO;
import com.trb.allocationservice.dto.QuestionAllocationDTO;
import com.trb.allocationservice.dto.QuestionsListDTO;
import com.trb.allocationservice.dto.SmeDetailsDTO;
import com.trb.allocationservice.dto.StatusDTO;
import com.trb.allocationservice.dto.getAllocationDTO;
import com.trb.allocationservice.entity.ObjectedDocumentModel;
import com.trb.allocationservice.entity.ObjectedQuestionRemarksModel;
import com.trb.allocationservice.entity.ObjectedQuestionsAllocationModel;
import com.trb.allocationservice.entity.ObjectionSummaryModel;
import com.trb.allocationservice.entity.ObjectionTrackerModel;

@Mapper(componentModel = "spring")
public interface AllocationMapper {

	AllocationMapper userMapper = Mappers.getMapper(AllocationMapper.class);

	ObjectedQuestionsAllocationModel mapQuestionsToUser(Long userPk, Long statusId, Long objectedQuestionsPk,
			String subject);

	ObjectedQuestionRemarksModel mapRemarksToObjections(Long objallocpk);

	ObjectedQuestionRemarksModel updateRemarks(Long objectionRemarksPk, Long objallocpk, String subject,
			String standard, String pageNumber,String referenceUrl,String author, String remarks, List<ObjectedDocumentModel> documents);

	
	
	@Mapping(target = "remarks.standard", source = "standard")
	@Mapping(target = "remarks.docPath", source = "docPath")
	@Mapping(target = "remarks.pageNumber", source = "pageNumber")
	@Mapping(target = "remarks.remarks", source = "remarks")
	@Mapping(target = "remarks.fileNames", source = "fileNames")
	@Mapping(target = "remarks.author", source = "author")
	@Mapping(target = "remarks.referenceUrl", source = "referenceUrl")
	getAllocationDTO getallocationDTO(String userId, String subject, String action, String identifyNumber,
			String standard, List<String> docPath, String pageNumber,String author,String referenceUrl, Long allocatedQuestion, String status,
			String remarks, String name, Long allocatedPk, List<String> fileNames);

	ObjectedDocumentModel getDocumentModel(Long objectionRemarksPk, String documentPath,
			ObjectedQuestionRemarksModel remarks);

	ObjectionSummaryModel mapSummaryDetails(Integer summaryId, Long objquesId, Integer subjectId, Integer levelId,
			String currrent, Integer statusId);

	ObjectedQuestionsAllocationModel mapAllocationDetails(Long objectionAllocationPk, String action, Integer summaryId,
			Integer statusId);

	ObjectionTrackerModel mapAllocationTracker(Integer levelId, Integer subjectId, String currrent);

	
	@Mapping(target = "remarks.subject", source = "subject")
	@Mapping(target = "remarks.action", source = "action")
	@Mapping(target = "remarks.standard", source = "standard")
	@Mapping(target = "remarks.docpath", source = "docPath")
	@Mapping(target = "remarks.pageNo", source = "pageNo")
	@Mapping(target = "remarks.remarks", source = "remarks")
	@Mapping(target = "remarks.author", source = "author")
	@Mapping(target = "remarks.referenceUrl", source = "referenceUrl")
	@Mapping(target = "remarks.fileNames", source = "fileNames")
	@Mapping(target = "remarks.resSelectedBySME", source = "resSelectedBySME")
	LevelDTO smeActionDTO(String userId, Long smeNo, String smeName,String smeLastName, String level,
			String status, String allocatedDt,String actionDt, String action, String subject, String standard, String pageNo,String remarks,String author,String referenceUrl,
			List<String> docPath, List<String> fileNames, String resSelectedBySME);
	
	
	@Mapping(source = "langId", target = "langId")
	@Mapping(source = "langName", target = "langName")
	@Mapping(source = "questionsDescription", target = "questionsDescription")
	@Mapping(source = "qstImage", target = "qstImage")
	QuestionsListDTO toQuestionsListDTO(GetAllocationDetailsDTO allocation);
	 
	@Mapping(source = "smeId", target = "smeId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "statusCode", target = "status")
	@Mapping(source = "levelId", target = "levelId")
    @Mapping(source = "level", target = "level")
    @Mapping(target = "objectionAllocationPk", expression = "java(Long.valueOf(allocation.getobjectionAllocationPk()))")
    SmeDetailsDTO toSmeDetailsDTO(GetAllocationDetailsDTO allocation);
	
	@Mapping(source = "summaryId", target = "summaryId")
    @Mapping(source = "objtrackerPK", target = "trackingId")
    @Mapping(source = "subjectId", target = "subjectId")
    @Mapping(target = "levelId", expression = "java(null)")
    @Mapping(target = "level", expression = "java(null)")
    @Mapping(source = "subject", target = "subject")
    @Mapping(source = "status", target = "overallstatus")
	CurrentStatusDTO allocationToCurrentStatusDTO(GetAllocationDetailsDTO allocation);
	
	@Mapping(source = "allocation.objquesId", target = "objquesId")
    @Mapping(source = "allocation.questionsId", target = "questionsId")
    @Mapping(source = "allocation.examDate", target = "examDate")
    @Mapping(source = "allocation.examBatch", target = "examBatch")
    @Mapping(source = "allocation.clientId", target = "clientId")
    @Mapping(source = "allocation.examName", target = "examName")
    @Mapping(source = "allocation.qbId", target = "qbId")
	@Mapping(source = "statusdto", target = "status")
	@Mapping(source = "allocation.qst_pid", target = "parentId")
    QuestionAllocationDTO allocationToQuestionAllocationDTO(GetAllocationDetailsDTO allocation, StatusDTO statusdto);
	
	@Mapping(source = "langId", target = "langId")
	@Mapping(source = "langName", target = "langName")
	@Mapping(source = "questionsDescription", target = "questionsDescription")
	@Mapping(source = "qstImage", target = "qstImage")
	QuestionsListDTO QuestionsListDTO(NotificationDTO allocation);
	
	@Mapping(source = "allocation.objquesId", target = "objquesId")
    @Mapping(source = "allocation.examDate", target = "examDate")
    @Mapping(source = "allocation.examBatch", target = "examBatch")
    @Mapping(source = "allocation.clientId", target = "clientId")
	@Mapping(source = "allocation.examName", target = "examName")
	@Mapping(source = "allocation.finalObjectionStatus", target = "finalObjectionStatus")
    @Mapping(source = "allocation.sectionName", target = "sectionName")
    @Mapping(source = "allocation.topic", target = "topic")
    @Mapping(source = "allocation.authoredCorrectAns", target = "authoredCorrectAns")
	@Mapping(source = "allocation.revidesAns", target = "revisedAnsKey")
	@Mapping(source = "allocation.parentId", target = "parentId")
	@Mapping(source = "allocation.qbId", target = "qbId")
    @Mapping(source = "allocation.questionsId", target = "questionsId")
	NotificationResDTO toNotificationRes(NotificationDTO allocation);
}
