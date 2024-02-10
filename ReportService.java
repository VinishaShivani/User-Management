package com.nseit.trb.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nseit.trb.client.AllAllocationClient;
import com.nseit.trb.client.AllocationClient;
import com.nseit.trb.client.FinalObjDtlReportClient;
import com.nseit.trb.client.ObjectionByQstnIdClient;
import com.nseit.trb.client.ObjectionClient;
import com.nseit.trb.client.ObjectionSummaryReportClient;
import com.nseit.trb.client.constant.AppConstant;
import com.nseit.trb.dto.AllocationDTO;
import com.nseit.trb.dto.AllocationResponseContent;
import com.nseit.trb.dto.FinalReportResponse;
import com.nseit.trb.dto.FinalReportSmry;
import com.nseit.trb.dto.ObjectionDTO;
import com.nseit.trb.dto.ObjectionResponseContent;
import com.nseit.trb.dto.ReportsBySme;
import com.nseit.trb.dto.ReportsDTO;
import com.nseit.trb.dto.ReportsFinalObjDetail;
import com.nseit.trb.dto.ReportsObjSummary;
import com.nseit.trb.dto.ReportsRequestDTO;
import com.nseit.trb.dto.ResponseContent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class ReportService {
	
	private final AllocationClient allocationClient;
	private final AllAllocationClient allAllocationClient;
	private final ObjectionClient objectionClient;
	private final ObjectionSummaryReportClient objSummaryRptClient;
	private final FinalObjDtlReportClient finalObjDtlReportClient;
	private final ObjectionByQstnIdClient objectionByQstnIdClient;
	
//	public Mono<ReportsDTO> getSmeDailyReports(ReportsRequestDTO requestPayload) {
//				
//		log.debug("#requestPayload:{}",requestPayload);
//		
//		Map<String, Map<String, Long>> objSubjectMapping = new HashMap<>();
//		Map<String,List<String>> exmSubjectMap = new HashMap<>();
//		Map<String, Map<String, Map<String, Map<String, Long>>>> objSubjectBatchMapping = new HashMap<>();
//		
//		//fetching Objection response
//		Mono<ObjectionDTO> objetionResponse =  this.objectionClient.getobjectionQuestion();
//		List<ObjectionDTO> objetionRespList = objetionResponse.flux().collectList().block();
//		log.info("isNullOrEmpty(objetionRespList):{}",isNullOrEmpty(objetionRespList));
//		
//		if(!isNullOrEmpty(objetionRespList)) {
//			objSubjectMapping = getObjectionBySubjectCount(objetionRespList);
//			log.info("objSubjectMapping ::" +objSubjectMapping);
//			
//			exmSubjectMap = getExamNameSubjectMap(objSubjectMapping);
//			objSubjectBatchMapping = getObjBySubjectAndDateAndBatch(objetionRespList);
//		}
//		
//		
//		//fetching Allocation response
//		Mono<AllocationDTO> allocationResponse =  this.allAllocationClient.getAllAllocation();
//		List<AllocationDTO> allocRespList = allocationResponse.flux().collectList().block();
//		log.info("isNullOrEmpty(allocRespList):{}",isNullOrEmpty(allocRespList));
//		
//		//Allocation response - groupBy smeId and UserName
//		Map<String, List<AllocationResponseContent>> smeAllocByUserId =  getAllocByUserId(allocRespList);
//		//log.info("smeAllocByUserId:{}"+smeAllocByUserId);
//		
//		//Allocation response - groupBy Subject and SmeId and Action counts
//		Map<String, Map<String, Map<String, Long>>> smeAllocBySubjectMap =  getAllocBySubjectAndSmeIdAndAction(allocRespList);
//		//log.info("smeAllocBySubjectMap:{}"+smeAllocBySubjectMap);
//		
//		
//		 ReportsDTO respDTO = new ReportsDTO();
//		 List<ReportsBySme> respList = new ArrayList<ReportsBySme>();
//		//srlno incrementing
//		 int srlno = 0;
//		 
//		 if(!isNullOrEmpty(allocRespList) && !isNullOrEmpty(objetionRespList)) {
//		 
//			 for(Entry<String, Map<String, Map<String, Long>>> entry1 : smeAllocBySubjectMap.entrySet()) {
//				 
//				 log.info("SUBJECT :{}",entry1.getKey());
//				 log.info("names-status :{}",entry1.getValue());
//				 
//				 String subject = entry1.getKey();
//				 List<String> examNamesList = exmSubjectMap.get(subject);
//				 log.info("examNamesList :{}",examNamesList);
//				 
//				 for(String examNameObj : examNamesList) {
//					 
//					 String exmDate ="";
//					 String exmBatch = "";
//					 Map<String, Map<String, Map<String, Long>>> subjectBatchMapping = objSubjectBatchMapping.get(examNameObj);
//					 Map<String, Map<String, Long>> dateBatchMapping = subjectBatchMapping.get(subject);
//					 Set<String> exmDateKeys = dateBatchMapping.keySet();
//					 for (String dateKey : exmDateKeys) {
//						 exmDate = dateKey;
//						 Map<String, Long> exmBatches = dateBatchMapping.get(dateKey);
//						 Set<String> exmBatchKeys = exmBatches.keySet();
//						 for (String batchKey : exmBatchKeys) {
//							 exmBatch = batchKey;
//						 }
//					 }
//					 
//					 Map<String, Map<String, Long>> internalMap1 = entry1.getValue();
//				 
//					 for(Entry<String, Map<String, Long>> entry2 : internalMap1.entrySet()) {
//				
//						 ReportsBySme smeDTO = new ReportsBySme();
//						 String idNum = entry2.getKey();
//					
//						 smeDTO.setSrlno(++srlno);
//						 smeDTO.setSubject(subject);
//						 smeDTO.setExamName(examNameObj);
//						 smeDTO.setDate(exmDate);
//						 smeDTO.setBatch(exmBatch);
//						
//						 List<AllocationResponseContent> allocUserList = smeAllocByUserId.get(idNum);
//						 allocUserList.forEach(p-> {
//							 smeDTO.setSmeName(p.getUserId()); 
//							 });
//						 
//						 Map<String, Long> internalMap2 = entry2.getValue();
//						 long totalAllocated =0;
//						 long approveCount =0;
//						 long rejectCount =0;
//						 long pendingCount =0;
//						 long totalcompleted =0;
//						 
//						 for (Entry<String, Long> entry3 : internalMap2.entrySet()) {
//							 
//							  totalAllocated =0;
//							  					 
//							 if(entry3.getKey().equals(AppConstant.STS_APPROVED)) {
//								 approveCount = entry3.getValue();
//							 }else if(entry3.getKey().equals(AppConstant.STS_REJECTED)) {
//								 rejectCount = entry3.getValue();
//							 }else if(entry3.getKey().equals(AppConstant.STS_ALLOC)) {
//								 pendingCount = entry3.getValue();
//							 }
//							
//							 totalAllocated += approveCount+rejectCount +pendingCount;
//							 	 
//						 }
//						 
//						 smeDTO.setQstnAssigned(totalAllocated);
//		
//						 totalcompleted = totalAllocated - pendingCount ;
//						 smeDTO.setQstnCompleted(totalcompleted);
//						 
//						 respList.add(smeDTO);
//						 respDTO.setReportsBySme(respList);
//					 
//					 }
//					 
//				 }
//		
//			 }
//		 
//		
//			//filter changes
//			List<ReportsBySme> responseList = respDTO.getReportsBySme();
//			log.info("respFilterList:{}",responseList);
//			 
//			List<ReportsBySme> resultList = new ArrayList<>();
//			
//		 
//		 	Predicate<ReportsBySme> isSubject = null,isSmeName = null,isExmName = null,isDate = null,isBatch = null;
//
//			List<Predicate<ReportsBySme>> predicatelist = new ArrayList<>();
//			
//
//			if(!isNullOrEmpty(requestPayload.getSubject())) 
//			{
//				isSubject = fr -> fr.getSubject().equals(requestPayload.getSubject()); 
//				predicatelist.add(isSubject);
//				log.info("#predicatelist.add(isSubject)");
//			}
//			
//			if(!isNullOrEmpty(requestPayload.getSmeName())) 
//			{   
//				isSmeName = fr -> fr.getSmeName().equals(requestPayload.getSmeName());
//				predicatelist.add(isSmeName);
//				log.info("#predicatelist.add(isSmeName)");
//			}
//			
//			if(!isNullOrEmpty(requestPayload.getExamName())) 
//			{ 
//				isExmName = fr -> fr.getExamName().equals(requestPayload.getExamName()); 
//				predicatelist.add(isExmName);
//				log.info("#predicatelist.add(isExmName)");
//			}
//			
//			if(!isNullOrEmpty(requestPayload.getDate())) 
//			{ 
//				isDate = fr -> fr.getDate().equals(requestPayload.getDate());
//				predicatelist.add(isDate);
//				log.info("#predicatelist.add(isDate)");
//			}
//			
//			if(!isNullOrEmpty(requestPayload.getBatch())) 
//			{ 
//				isBatch = fr -> fr.getBatch().equals(requestPayload.getBatch());
//				predicatelist.add(isBatch);
//			}
//			
//			log.info("#predicatelist::{}",predicatelist.size());
//			
//		 
//			if(predicatelist.size() > 0) {
//				
//				Predicate<ReportsBySme> framePredicateFilters = p -> !p.getSubject().equals(null);
//				for(Predicate<ReportsBySme> filterObj: predicatelist) {
//					framePredicateFilters = framePredicateFilters.and(filterObj);
//				}
//				resultList = responseList.stream()
//					      .filter( framePredicateFilters )
//					      .collect(Collectors.toList());
//				
//				log.info("#resultList::{}",resultList);
//				
//			}else {
//				resultList = responseList ;
//			}
//		 
//		 
//			if(!isNullOrEmpty(resultList))
//				respDTO.setReportsBySme(resultList);
//			else
//				respDTO.setReportsBySme(null);
//		
//		}
//		 
//	    if(Objects.isNull(respDTO.getReportsBySme()))
//	    {	
//	    	respDTO.setStatuscode(AppConstant.STS_CD_002);
//	    	respDTO.setMessage(AppConstant.STS_MSG_002);
//	    }else {
//	    
//	    	respDTO.setStatuscode(AppConstant.STS_CD_000);
//	    	respDTO.setMessage(AppConstant.STS_MSG_000);
//	    }
//		        
//	     log.info("respDTO:{}",respDTO);
//		 Mono<ReportsDTO> response = Mono.just(respDTO)  ;
//
//		 return response;			
//		
//	}

	
	
	public Mono<ReportsDTO> getSmeDailyReports(ReportsRequestDTO requestPayload) {
		
		log.debug("#requestPayload:{}",requestPayload);
		
		Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Long>>>>>> examSubjectUserStatusMap = new HashMap<>();
		
		//fetching Allocation response
		Mono<AllocationDTO> allocationResponse =  this.allAllocationClient.getAllAllocation();
		List<AllocationDTO> allocRespList = allocationResponse.flux().collectList().block();
		log.info("isNullOrEmpty(allocRespList):{}",isNullOrEmpty(allocRespList));
		List<AllocationResponseContent> allocResponseList = allocRespList.stream().findFirst().get().getResponseContent();
		log.info("isNullOrEmpty(allocResponseList):{}",isNullOrEmpty(allocResponseList));
		
		//Setting ExamName to the Allocation List
		String questionList  = "";
		if(!isNullOrEmpty(allocResponseList))
		{
			for(AllocationResponseContent obj1 : allocResponseList) {
				questionList = questionList+ obj1.getAllocatedQuestion()+',';
			}
		
			if(!questionList.equals("")) {		
				Mono<ObjectionDTO> objectionResp = this.objectionByQstnIdClient.getobjectionQuestion(questionList);
				List<ObjectionDTO> objExamNameList = objectionResp.flux().collectList().block();
				List<ObjectionResponseContent> objExamNameRespList = objExamNameList.stream().findFirst().get().getResponseContent();
				log.info("#objExamNameRespList:{}",objExamNameRespList);
				objExamNameRespList.stream().forEach(p->{
					allocResponseList.stream().filter(q->q.getAllocatedQuestion().equals(p.getObjquesId())).forEach(obj->{
					obj.setExamName(p.getModuleName());
					obj.setExamDate(p.getExamDate());
					obj.setExamBatch(p.getExamBatch());
					});
				});	
			 }
		}
		log.info("isNullOrEmpty(allocResponseList):{}",isNullOrEmpty(allocResponseList));
		
		//Grouping By ExamName, Subject, UserId, Status count
		if(!isNullOrEmpty(allocResponseList)) {
			examSubjectUserStatusMap = allocResponseList.stream()
                .collect(
                        Collectors.groupingBy(AllocationResponseContent::getExamName,
                        	Collectors.groupingBy(AllocationResponseContent::getSubject,
                        		Collectors.groupingBy(AllocationResponseContent::getUserId,
                        			Collectors.groupingBy(AllocationResponseContent::getExamDate,
                        				Collectors.groupingBy(AllocationResponseContent::getExamBatch,
                        						Collectors.groupingBy(AllocationResponseContent::getStatus,
                                        Collectors.counting())))))));
		}
		log.info("examSubjectUserStatusMap:{}",examSubjectUserStatusMap);

		
		 ReportsDTO respDTO = new ReportsDTO();
		 List<ReportsBySme> respList = new ArrayList<ReportsBySme>();
		 //srlno incrementing
		 int srlno = 0;
		 
		 if(!isNullOrEmpty(allocResponseList)) {
		 
			 for(Entry<String, Map<String, Map<String, Map<String, Map<String, Map<String, Long>>>>>> entry1 : examSubjectUserStatusMap.entrySet()) {
				 
				 Map<String, Map<String, Map<String, Map<String, Map<String, Long>>>>> subjectUserStatusMap = entry1.getValue();
				 
				 //log.info("ExamName :{}",entry1.getKey());
				 //log.info("Subject-User-Status :{}",entry1.getValue());
				 
				 for(Entry<String, Map<String, Map<String, Map<String, Map<String, Long>>>>> entry2 : subjectUserStatusMap.entrySet()) {
					 
					 Map<String, Map<String, Map<String, Map<String, Long>>>> userStatusMap = entry2.getValue();
					 
					 //log.info("Subject :{}",entry2.getKey());
					 //log.info("User-Status :{}",entry2.getValue());
					 
					 for(Entry<String, Map<String, Map<String, Map<String, Long>>>> entry3 : userStatusMap.entrySet()) {
						 
						 //log.info("User :{}",entry3.getKey());
						 //log.info("Status :{}",entry3.getValue());
					
						 Map<String, Map<String, Map<String, Long>>> dateBatchStatusMap = entry3.getValue();
						 
						 for(Entry<String, Map<String, Map<String, Long>>> entry4 : dateBatchStatusMap.entrySet()) {
						
							 //log.info("Date :{}",entry3.getKey());
							 //log.info("Batch-Status :{}",entry3.getValue());
							 Map<String, Map<String, Long>> batchStatusMap = entry4.getValue();
							 
							 for(Entry<String, Map<String, Long>> entry5 : batchStatusMap.entrySet()) {
								 
								 
								 ReportsBySme smeDTO = new ReportsBySme();
								 String examName = entry1.getKey();
								 String subject = entry2.getKey();
								 String userName = entry3.getKey();
								 String examDate = entry4.getKey();
								 String examBatch = entry5.getKey();
								 
								
								 smeDTO.setSrlno(++srlno);
								 smeDTO.setExamName(examName);
								 smeDTO.setSubject(subject);
								 smeDTO.setSmeName(userName);
								 smeDTO.setDate(examDate);
								 smeDTO.setBatch(examBatch);
								 
								 Map<String, Long> internalObjMap3 = entry5.getValue();
								 long totalAllocated =0;
								 long approveCount =0;
								 long rejectCount =0;
								 long pendingCount =0;
								 long totalcompleted =0;
								 
								 for (Entry<String, Long> entry6 : internalObjMap3.entrySet()) {
									 
									  totalAllocated =0;
									  					 
									 if(entry6.getKey().equals(AppConstant.STS_APPROVED)) {
										 approveCount = entry6.getValue();
									 }else if(entry6.getKey().equals(AppConstant.STS_REJECTED)) {
										 rejectCount = entry6.getValue();
									 }else if(entry6.getKey().equals(AppConstant.STS_ALLOC)) {
										 pendingCount = entry6.getValue();
									 }
									
									 totalAllocated += approveCount+rejectCount +pendingCount;
									 	 
								 }
								 
								 smeDTO.setQstnAssigned(totalAllocated);
								 totalcompleted = totalAllocated - pendingCount ;
								 smeDTO.setQstnCompleted(totalcompleted);
								 
								 respList.add(smeDTO);
								 respDTO.setReportsBySme(respList);
								 
							 } 
						 }		 
					 }				 
				 }
			 }
		 }
		 
		 
			//filter changes
			List<ReportsBySme> responseList = respDTO.getReportsBySme();
			log.info("respFilterList:{}",responseList);
			 
			List<ReportsBySme> resultList = new ArrayList<>();
			
		 
		 	Predicate<ReportsBySme> isSubject = null,isSmeName = null,isExmName = null,isDate = null,isBatch = null;

			List<Predicate<ReportsBySme>> predicatelist = new ArrayList<>();
			

			if(!isNullOrEmpty(requestPayload.getSubject())) 
			{
				isSubject = fr -> fr.getSubject().equals(requestPayload.getSubject()); 
				predicatelist.add(isSubject);
				log.info("#predicatelist.add(isSubject)");
			}
			
			if(!isNullOrEmpty(requestPayload.getSmeName())) 
			{   
				isSmeName = fr -> fr.getSmeName().equals(requestPayload.getSmeName());
				predicatelist.add(isSmeName);
				log.info("#predicatelist.add(isSmeName)");
			}
			
			if(!isNullOrEmpty(requestPayload.getExamName())) 
			{ 
				isExmName = fr -> fr.getExamName().equals(requestPayload.getExamName()); 
				predicatelist.add(isExmName);
				log.info("#predicatelist.add(isExmName)");
			}
			
			if(!isNullOrEmpty(requestPayload.getDate())) 
			{ 
				isDate = fr -> fr.getDate().equals(requestPayload.getDate());
				predicatelist.add(isDate);
				log.info("#predicatelist.add(isDate)");
			}
			
			if(!isNullOrEmpty(requestPayload.getBatch())) 
			{ 
				isBatch = fr -> fr.getBatch().equals(requestPayload.getBatch());
				predicatelist.add(isBatch);
			}
			
			log.info("#predicatelist::{}",predicatelist.size());
			
		 
			if(predicatelist.size() > 0) {
				
				Predicate<ReportsBySme> framePredicateFilters = p -> !p.getSubject().equals(null);
				for(Predicate<ReportsBySme> filterObj: predicatelist) {
					framePredicateFilters = framePredicateFilters.and(filterObj);
				}
				resultList = responseList.stream()
					      .filter( framePredicateFilters )
					      .collect(Collectors.toList());
				
				log.info("#resultList::{}",resultList);
				
			}else {
				resultList = responseList ;
			}
		 
		 
			if(!isNullOrEmpty(resultList))
				respDTO.setReportsBySme(resultList);
			else
				respDTO.setReportsBySme(null);
		
		 
	    if(Objects.isNull(respDTO.getReportsBySme()))
	    {	
	    	respDTO.setStatuscode(AppConstant.STS_CD_002);
	    	respDTO.setMessage(AppConstant.STS_MSG_002);
	    }else {
	    
	    	respDTO.setStatuscode(AppConstant.STS_CD_000);
	    	respDTO.setMessage(AppConstant.STS_MSG_000);
	    }
		        
	     log.info("respDTO:{}",respDTO);
		 Mono<ReportsDTO> response = Mono.just(respDTO)  ;

		 return response;			
		
	}


	/**
	 * @param objSubjectMapping 
	 * @return 
	 * 
	 */
	private Map<String, List<String>> getExamNameSubjectMap(Map<String, Map<String, Long>> objSubjectMapping) {
	
		Map<String,List<String>> exmSubjectMap = new HashMap<>();
		for(Entry<String, Map<String, Long>> entry00 : objSubjectMapping.entrySet()) {
			 
			 Map<String, Long> obj = entry00.getValue();
			 List<String> moduleList = new ArrayList<>();
			 for(Entry<String, Long> objEntry : obj.entrySet()) {
				 moduleList.add(objEntry.getKey());
			 }
			
			 exmSubjectMap.put(entry00.getKey(), moduleList);
		}
		log.info("exmSubjectMap:{}",exmSubjectMap);
		
		return exmSubjectMap;
	}

	
	
	private Map<String, Map<String, Map<String, Long>>> getAllocBySubjectAndSmeIdAndAction(List<AllocationDTO> allocRespList) {
		
		Map<String, Map<String, Map<String, Long>>> multipleFieldsMap = new HashMap<>();
		
		if(!allocRespList.isEmpty()) {
		
			List<AllocationResponseContent> allocResponseList =
			allocRespList.stream().findFirst().get().getResponseContent();
			
//			AllocationDTO resp = dump.getAllocDumpData();
//			List<AllocationResponseContent> allocResponseList = resp.getResponseContent();
	
			
			if(allocResponseList!=null) {
				multipleFieldsMap = allocResponseList.stream()
	                .collect(
	                        Collectors.groupingBy(AllocationResponseContent::getSubject,
	                        		Collectors.groupingBy(AllocationResponseContent::getIdentifyNumber,
	                                Collectors.groupingBy(AllocationResponseContent::getStatus,
	                                        Collectors.counting()))));
			}
		
		}
		
		log.info("getAllocBySubjectAndSmeIdAndAction:{}",multipleFieldsMap);
		
		 return multipleFieldsMap;
		
		 
	}
	
	
	private Map<String, Map<String, Long>> getObjectionBySubjectCount(List<ObjectionDTO> objetionRespList) {
		
		Map<String, Map<String, Long>> objSubjectMap = new HashMap<>();
		
		List<ObjectionResponseContent> responseList =
				objetionRespList.stream().findFirst().get().getResponseContent();
		
		//System.out.println("Object responseList:"+responseList);
		
		 if(responseList!=null) {
			 objSubjectMap = responseList.stream()
					 .collect(
							 Collectors.groupingBy(ObjectionResponseContent::getSubject,
		                        		Collectors.groupingBy(ObjectionResponseContent::getModuleName,
		                                        Collectors.counting())));
					 
		 }
		 
		 log.info(">>objSubjectMap:{}",objSubjectMap);
		 
		return objSubjectMap;
	}
	
	
private Map<String, Map<String, Map<String, Map<String, Long>>>> getObjBySubjectAndDateAndBatch(List<ObjectionDTO> objetionRespList) {
		
		Map<String, Map<String, Map<String, Map<String, Long>>>> objSubjectMap = new HashMap<>();
		
		List<ObjectionResponseContent> responseList =
				objetionRespList.stream().findFirst().get().getResponseContent();
		
		//System.out.println("Object responseList:"+responseList);
		
		 if(responseList!=null) {
			 objSubjectMap = responseList.stream()
					 .collect(
							 Collectors.groupingBy(ObjectionResponseContent::getModuleName,
		                        		Collectors.groupingBy(ObjectionResponseContent::getSubject,
		                        				Collectors.groupingBy(ObjectionResponseContent::getExamDate,
		                        						Collectors.groupingBy(ObjectionResponseContent::getExamBatch,
		                                        Collectors.counting())))));
							 
					 
		 }
		 
		 log.info(">>objSubjectMap:{}",objSubjectMap);
		 
		return objSubjectMap;
	}
	
	
private Map<String, List<AllocationResponseContent>> getAllocByUserId(List<AllocationDTO> allocRespList) {
		
		Map<String, List<AllocationResponseContent>> multipleFieldsMap = new HashMap<>();
		
		
		if(!allocRespList.isEmpty()) {
		
			List<AllocationResponseContent> allocResponseList =
			allocRespList.stream().findFirst().get().getResponseContent();
			
//			AllocationDTO resp = dump.getAllocDumpData();
//			List<AllocationResponseContent> allocResponseList = resp.getResponseContent();
	
			
			if(allocResponseList!=null) {
				multipleFieldsMap = allocResponseList.stream()
	                .collect(
	                        Collectors.groupingBy(AllocationResponseContent::getIdentifyNumber));
			}
		
		}
		
		log.info("multipleFieldsMap:{}",multipleFieldsMap);
		
		 return multipleFieldsMap;
		
		 
	}



public Mono<ReportsDTO> getObjSummaryReports(ReportsRequestDTO requestPayload) {
	// TODO Auto-generated method stub
	
		log.debug("#requestPayload:{}",requestPayload);
	
		ReportsDTO respDTO = new ReportsDTO();
	
		//fetching Allocation response
		Mono<ReportsObjSummary> objReportsResp =  this.objSummaryRptClient.getObjSummaryRpt();
		
		List<ReportsObjSummary> objReportList = objReportsResp.flux().collectList().block();
		log.info("#objReportList:{}",objReportList);
		
		List<ResponseContent> responseList = objReportList.stream().findFirst().get().getResponseContent();

		if(!objReportList.isEmpty()) {
		
			//respDTO.setReportsObjSummary(responseList);
			
			//filter changes
			List<ResponseContent> resultList = new ArrayList<>();
		 
		 	Predicate<ResponseContent> isSubject = null,isExmName = null;

			List<Predicate<ResponseContent>> predicatelist = new ArrayList<>();

			if(!isNullOrEmpty(requestPayload.getSubject())) 
			{
				isSubject = fr -> fr.getSubject().equals(requestPayload.getSubject()); 
				predicatelist.add(isSubject);
				log.info("#predicatelist.add(isSubject)");
			}
			
			if(!isNullOrEmpty(requestPayload.getExamName())) 
			{ 
				isExmName = fr -> fr.getExamName().equals(requestPayload.getExamName()); 
				predicatelist.add(isExmName);
				log.info("#predicatelist.add(isExmName)");
			}
			
			log.info("#predicatelist::{}",predicatelist.size());
			
		 
		 if(predicatelist.size() > 0) {
				
				Predicate<ResponseContent> framePredicateFilters = p -> !p.getSubject().equals(null);
				for(Predicate<ResponseContent> filterObj: predicatelist) {
					framePredicateFilters = framePredicateFilters.and(filterObj);
				}
				resultList = responseList.stream()
					      .filter( framePredicateFilters )
					      .collect(Collectors.toList());
				
				log.info("#resultList::{}",resultList);
				
			}else {
				resultList = responseList ;
			}
		 
		 
		 if(!isNullOrEmpty(resultList))
			 respDTO.setReportsObjSummary(resultList);
		 else
			 respDTO.setReportsObjSummary(null);
			
		}
		
		if(Objects.isNull(respDTO.getReportsObjSummary()))
	    {	
	    	respDTO.setStatuscode(AppConstant.STS_CD_002);
	    	respDTO.setMessage(AppConstant.STS_MSG_002);
	    }else {
	    
	    	respDTO.setStatuscode(AppConstant.STS_CD_000);
	    	respDTO.setMessage(AppConstant.STS_MSG_000);
	    }
		
		Mono<ReportsDTO> response = Mono.just(respDTO)  ;

	return response;
}



public Mono<ReportsDTO> getFinalObjSummaryReports(ReportsRequestDTO requestPayload) {
	// TODO Auto-generated method stub
	
	log.debug("#requestPayload:{}",requestPayload);
	
	ReportsDTO respDTO = new ReportsDTO();
	
	//fetching Allocation response
	Mono<AllocationDTO> allocationResponse =  this.allocationClient.getAllocationByStatus("APPROVED");
	
	List<AllocationDTO> allocRespList = allocationResponse.flux().collectList().block();
	log.info("#allocRespList:{}",allocRespList);
	
	//Allocation response - groupBy Subject and SmeId and Action counts
	Map<String, Map<String, Map<String, Long>>> smeAllocBySubjectMap =  getAllocBySubjectAndAction(allocRespList);
	log.info("smeAllocBySubjectMap:{}"+smeAllocBySubjectMap);

	
	//fetching Allocation response
	Mono<ReportsObjSummary> objReportsResp =  this.objSummaryRptClient.getObjSummaryRpt();
	
	List<ReportsObjSummary> objReportList = objReportsResp.flux().collectList().block();
	log.info("#objReportList:{}",objReportList);
	
	List<ResponseContent> responseList = objReportList.stream().findFirst().get().getResponseContent();
	
	Map<String, Map<String, Long>> objSubjectMap1 = responseList.stream()
			 .collect(
					 Collectors.groupingBy(ResponseContent::getExamName,
                       		Collectors.groupingBy(ResponseContent::getSubject,                       				
                                       Collectors.counting())));
	log.info("#objSubjectMap1:{}",objSubjectMap1);
	
	
	 

	if(!objReportList.isEmpty()) {
		
		List<FinalReportSmry> finalList = new ArrayList<>();
		
		for (Entry<String, Map<String, Long>> entry0 : objSubjectMap1.entrySet()) {
			 System.out.println("entry0 Key = " + entry0.getKey() +", Value = " + entry0.getValue());
			
		String exmName = entry0.getKey();
		List<ResponseContent> filteredRespList = responseList.stream()
			.filter(p-> p.getExamName().equals(exmName))
			.collect(Collectors.toList());
	
		Map<String, ResponseContent>  ObjectMap = filteredRespList.stream()
	    		.collect(Collectors.toMap(ResponseContent::getSubject, Function.identity()));
		log.info("#ObjectMap:{}",ObjectMap);
		
		
		 for (Entry<String, ResponseContent> entry1 : ObjectMap.entrySet()) {
			 System.out.println("entry1 Key = " + entry1.getKey() +", Value = " + entry1.getValue());
		
			 
			    FinalReportSmry finalDTO = new FinalReportSmry();
			 	ResponseContent obj = entry1.getValue();
			
				finalDTO.setExamName(obj.getExamName());
				finalDTO.setSubject(obj.getSubject());
				finalDTO.setNoofQuestions(obj.getNoofQuestion());
				finalDTO.setNoofObjectionRcvd(obj.getNoofObjection());
				finalDTO.setNoofUniqueCandidates(obj.getNoofUniqueCandidateObjection());
				finalDTO.setNoofUniqueQuestions(obj.getNoofUniqueObjection());
				
				 long approvedCount =0;
				 long noAnswerKey =0;
				 long ansKeyChange =0;
				 long multiAnswerKey =0;
				 long wrongQuestion =0;
				
				 if(smeAllocBySubjectMap.containsKey(obj.getExamName()))
        		 {	
					 System.out.println("Matching::"+obj.getExamName());
					 
					 Map<String, Map<String, Long>> subjectMap = smeAllocBySubjectMap.get(obj.getExamName());
					 
					 System.out.println("subjectMap::"+subjectMap);
					 
					 if(subjectMap.containsKey(obj.getSubject()))
	        		 {
						 Map<String, Long> actionMap = subjectMap.get(obj.getSubject());
					 
						 for (Entry<String, Long> entry3 : actionMap.entrySet()) {
							 
							 
							 if(entry3.getKey().equals(AppConstant.STS_NO_ANS_KEY)) {
								 noAnswerKey = entry3.getValue();
							 }else if(entry3.getKey().equals(AppConstant.STS_ANS_KEY_CHANGE)) {
								 ansKeyChange = entry3.getValue();
							 }else if(entry3.getKey().equals(AppConstant.STS_MULTI_ANS_KEY)) {
								 multiAnswerKey = entry3.getValue();
							 }else if(entry3.getKey().equals(AppConstant.STS_WRONG_QSTN)) {
								 wrongQuestion = entry3.getValue();
							 }
							 
						 }
	        		 }
        		 }
				 
				 approvedCount = noAnswerKey+ansKeyChange+multiAnswerKey+wrongQuestion;
				 finalDTO.setApproved(approvedCount);
				 finalDTO.setNoAnswerKey(noAnswerKey);
				 finalDTO.setAnsKeyChange(ansKeyChange);
				 finalDTO.setMultipleAnsKey(multiAnswerKey);
				 finalDTO.setWrongQuestion(wrongQuestion);
				 
				 finalList.add(finalDTO);
	 
		 }
		 
		 respDTO.setFinalReportSmry(finalList);
	}
	
	}

	//filter changes
	List<FinalReportSmry> respList = respDTO.getFinalReportSmry();
	log.info("respFilterList:{}",respList);
	
	List<FinalReportSmry> resultList = new ArrayList<>();
 
 	Predicate<FinalReportSmry> isSubject = null,isExmName = null;

	List<Predicate<FinalReportSmry>> predicatelist = new ArrayList<>();

	if(!isNullOrEmpty(requestPayload.getSubject())) 
	{
		isSubject = fr -> fr.getSubject().equals(requestPayload.getSubject()); 
		predicatelist.add(isSubject);
		log.info("#predicatelist.add(isSubject)");
	}
	
	if(!isNullOrEmpty(requestPayload.getExamName())) 
	{ 
		isExmName = fr -> fr.getExamName().equals(requestPayload.getExamName()); 
		predicatelist.add(isExmName);
		log.info("#predicatelist.add(isExmName)");
	}
	
	log.info("#predicatelist::{}",predicatelist.size());
	
 
	if(predicatelist.size() > 0) {
		
		Predicate<FinalReportSmry> framePredicateFilters = p -> !p.getSubject().equals(null);
		for(Predicate<FinalReportSmry> filterObj: predicatelist) {
			framePredicateFilters = framePredicateFilters.and(filterObj);
		}
		resultList = respList.stream()
			      .filter( framePredicateFilters )
			      .collect(Collectors.toList());
		
		log.info("#resultList::{}",resultList);
		
	}else {
		resultList = respList ;
	}
 
 
	 if(!isNullOrEmpty(resultList))
		 respDTO.setFinalReportSmry(resultList);
	 else
		 respDTO.setFinalReportSmry(null);
	

	if(Objects.isNull(respDTO.getFinalReportSmry()))
    {	
    	respDTO.setStatuscode(AppConstant.STS_CD_002);
    	respDTO.setMessage(AppConstant.STS_MSG_002);
    }else {
    
    	respDTO.setStatuscode(AppConstant.STS_CD_000);
    	respDTO.setMessage(AppConstant.STS_MSG_000);
    }
	
	Mono<ReportsDTO> response = Mono.just(respDTO)  ;

	return response;

}


private Map<String, Map<String, Map<String, Long>>> getAllocBySubjectAndAction(List<AllocationDTO> allocRespList) {
	
	Map<String, Map<String, Map<String, Long>>> multipleFieldsMap = new HashMap<>();
	
	if(!allocRespList.isEmpty()) {
	
		List<AllocationResponseContent> allocResponseList =
		allocRespList.stream().findFirst().get().getResponseContent();

		//log.info("allocResponseList:{}",allocResponseList);
		
		String questionList  = "";
		if(allocResponseList!=null)
		{
			for(AllocationResponseContent obj1 : allocResponseList) {
				questionList = questionList+ obj1.getAllocatedQuestion()+',';
			}
		}
		
		
		if(!questionList.equals("")) {	
			
			Mono<ObjectionDTO> objectionResp = this.objectionByQstnIdClient.getobjectionQuestion(questionList);
			
			List<ObjectionDTO> objExamNameList = objectionResp.flux().collectList().block();
			
			List<ObjectionResponseContent> objExamNameRespList = objExamNameList.stream().findFirst().get().getResponseContent();
			log.info("#objExamNameRespList:{}",objExamNameRespList);
			
				
				objExamNameRespList.stream().forEach(p->{
					//log.info("#p:{}",p.getObjquesId());
					allocResponseList.stream().filter(q->q.getAllocatedQuestion().equals(p.getObjquesId())).forEach(obj->{
						//log.info("#obj:{}",obj.getAllocatedQuestion());
					obj.setExamName(p.getModuleName());
					});
				});
						
					
					
				}
		
		log.info("allocResponseList:{}",allocResponseList);
		if(allocResponseList!=null) {
		multipleFieldsMap = allocResponseList.stream()
                .collect(
                        Collectors.groupingBy(AllocationResponseContent::getExamName,
                        		Collectors.groupingBy(AllocationResponseContent::getSubject,
                                Collectors.groupingBy(AllocationResponseContent::getAction,
                                        Collectors.counting()))));
		
		//log.info("multipleFieldsMap:{}",multipleFieldsMap);
		}
		
	}

	 return multipleFieldsMap;
	 
}


public Mono<ReportsDTO> getFinalObjDtlReports(ReportsRequestDTO requestPayload) {
	// TODO Auto-generated method stub

	log.debug("#requestPayload:{}",requestPayload);
	
	ReportsDTO respDTO = new ReportsDTO();

	//fetching Allocation response
	Mono<ReportsFinalObjDetail> objReportsResp =  this.finalObjDtlReportClient.getFinalObjDtlRpt();
	
	List<ReportsFinalObjDetail> objReportList = objReportsResp.flux().collectList().block();
	log.info("#objReportList.isNullOrEmpty:{}",isNullOrEmpty(objReportList));
	

	if(!isNullOrEmpty(objReportList)) {
	
		
		List<FinalReportResponse> responseList = objReportList.stream().findFirst().get().getResponseContent();
		
		List<FinalReportResponse> resultList = new ArrayList<>();	
	 
	 	Predicate<FinalReportResponse> isSubject = null,isExmName = null,isDate = null,isBatch = null,isClient = null;

		List<Predicate<FinalReportResponse>> predicatelist = new ArrayList<>();
		
		log.info("#resultList.isNullOrEmpty:{}",isNullOrEmpty(resultList));
	 	
	if(!isNullOrEmpty(responseList)) {
		//Displaying only Approved and Rejected records
	 	responseList = responseList.stream()
	 			.filter( p-> p.getStatus()!=null && (p.getStatus().equals(AppConstant.STS_APPROVED) 
	 							|| p.getStatus().equals(AppConstant.STS_REJECTED)) )
	 			.collect(Collectors.toList());
	 	

		if(!isNullOrEmpty(requestPayload.getSubject())) 
		{
			isSubject = fr -> fr.getSubject().equals(requestPayload.getSubject()); 
			predicatelist.add(isSubject);
			log.info("#predicatelist.add(isSubject)");
		}
		
		if(!isNullOrEmpty(requestPayload.getExamName())) 
		{ 
			isExmName = fr -> fr.getModuleName().equals(requestPayload.getExamName()); 
			predicatelist.add(isExmName);
			log.info("#predicatelist.add(isExmName)");
		}
		
		if(!isNullOrEmpty(requestPayload.getDate())) 
		{ 
			isDate = fr -> fr.getExamDate().equals(requestPayload.getDate());
			predicatelist.add(isDate);
			log.info("#predicatelist.add(isDate)");
		}
		
		if(!isNullOrEmpty(requestPayload.getBatch())) 
		{ 
			isBatch = fr -> fr.getExamBatch().equals(requestPayload.getBatch());
			predicatelist.add(isBatch);
			log.info("#predicatelist.add(isBatch)");
		}
		
		log.info("#predicatelist::{}",predicatelist.size());
		
		if(!isNullOrEmpty(requestPayload.getMasterQP())) 
		{ 
			//log.info("#requestPayload.getMasterQP():{}",requestPayload.getMasterQP());
			isClient = fr -> fr.getClientId().toString().equals(requestPayload.getMasterQP());
			predicatelist.add(isClient);
			log.info("#predicatelist.add(isClient)");
		}
		
		
		if(predicatelist.size() > 0) {
			
			Predicate<FinalReportResponse> framePredicateFilters = p -> !p.getSubject().equals(null);
			for(Predicate<FinalReportResponse> filterObj: predicatelist) {
				framePredicateFilters = framePredicateFilters.and(filterObj);
			}
			resultList = responseList.stream()
				      .filter( framePredicateFilters )
				      .collect(Collectors.toList());
			
			log.info("#resultList::{}",resultList);
			
		}else {
			resultList = responseList ;
		}
	 
	}
	 
	 if(!isNullOrEmpty(resultList))
		 respDTO.setReportsFinalObjDtl(resultList);
	 else
		 respDTO.setReportsFinalObjDtl(null);
	
		
	
	}
	
	if(Objects.isNull(respDTO.getReportsFinalObjDtl()))
    {	
    	respDTO.setStatuscode(AppConstant.STS_CD_002);
    	respDTO.setMessage(AppConstant.STS_MSG_002);
    }else {
    
    	respDTO.setStatuscode(AppConstant.STS_CD_000);
    	respDTO.setMessage(AppConstant.STS_MSG_000);
    }
	
	Mono<ReportsDTO> response = Mono.just(respDTO)  ;

	return response;
	
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
}
