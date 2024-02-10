package com.nseit.trb.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nseit.trb.client.AllAllocationClient;
import com.nseit.trb.client.AllocationClient;
import com.nseit.trb.client.ExamDetailClient;
import com.nseit.trb.client.ObjectionByQstnIdClient;
import com.nseit.trb.client.ObjectionBySubjectClient;
import com.nseit.trb.client.ObjectionClient;
import com.nseit.trb.client.constant.AppConstant;
import com.nseit.trb.delegate.UserDetails;
import com.nseit.trb.dto.AggregateDTO;
import com.nseit.trb.dto.AllocationDTO;
import com.nseit.trb.dto.AllocationQuestion;
import com.nseit.trb.dto.AllocationResponseContent;
import com.nseit.trb.dto.BatchInfoDTO;
import com.nseit.trb.dto.ExamDetails;
import com.nseit.trb.dto.ObjectionDTO;
import com.nseit.trb.dto.ObjectionResponseContent;
import com.nseit.trb.dto.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;

@Slf4j
@Service
@AllArgsConstructor
public class AggregatorService {

	
    private final AllocationClient allocationClient;
    private final ObjectionByQstnIdClient objectionByQstnIdClient;
    private final ObjectionBySubjectClient objectionBySubjectClient;
    private final ExamDetailClient examDetailClient ;
    private final ObjectionClient objectionClient;
    private final AllAllocationClient allAllocationClient;
    
    @Autowired
    UserDetails userDetailDelegate;
    
	public Mono<AggregateDTO> getObjQuestionQuestions(RequestDTO requestPayload, String domain) {
	// TODO Auto-generated method stub
	
		String userId = "";
		String subject = "";
		String status = "";
		String smeId = "";
		String date = "";
		String batch = "";
		String examName = "";
		

		//validating payload null check
		if(requestPayload.getUserId()==null) 
		{ userId = ""; }else { userId = requestPayload.getUserId(); }
		
		if(requestPayload.getSubject()==null) 
		{ subject = ""; }else { subject = requestPayload.getSubject(); }
		
		if(requestPayload.getStatus()==null) 
		{ status = ""; }else { status = requestPayload.getStatus(); }
		
		if(requestPayload.getSmeId()==null) 
		{ smeId = ""; }else { smeId = requestPayload.getSmeId(); }
		
		if(requestPayload.getSmeId()==null) 
		{ date = ""; }else { date = requestPayload.getDate(); }
		
		if(requestPayload.getSmeId()==null) 
		{ batch = ""; }else { batch = requestPayload.getBatch(); }
		
		if(requestPayload.getExamName()==null) 
		{ examName = ""; }else { examName = requestPayload.getExamName(); }
		

		//admin login scenario -userID blank and smeID has value
		if(requestPayload.getUserId()=="" && requestPayload.getSmeId()!="") {
			
			//SmeID to UserID Transition
			userId = smeIdToUserIdTransition(requestPayload);
			
			if(userId=="")
			{
				return  errorStatus();
			}
		
		}
		
		Map<String,String> mappingKey = new HashMap<>();
		mappingKey.put("smeId", smeId);
		mappingKey.put("subject", subject);
		mappingKey.put("status", status);
		mappingKey.put("date", date);
		mappingKey.put("batch", batch);
		mappingKey.put("examName", examName);
		
		
		Map<String, ExamDetails> multipleFieldsMap = new HashMap<>(); 
    	Mono<BatchInfoDTO> sessionResponse =  this.examDetailClient.getExamDtls();
		List<BatchInfoDTO> sessionRespList = sessionResponse.flux().collectList().block();
		if(!sessionRespList.isEmpty()) {
			List<ExamDetails> examDtlList = sessionRespList.stream().findFirst().get().getExamDetails();
			log.info("examDtlList.isNullOrEmpty:{}",isNullOrEmpty(examDtlList));
			if(examDtlList!=null) {
				multipleFieldsMap = examDtlList.stream()
				.collect(Collectors.toMap(ExamDetails::getQbId, Function.identity()));
				log.info(">>multipleFieldsMap:{}",multipleFieldsMap);
			}
		}
		Mono<Map<String,ExamDetails>> monoExamDtls = Mono.just(multipleFieldsMap);
		
		
		try {
		
			if(Optional.ofNullable(requestPayload).isPresent()
					&& !Optional.ofNullable(requestPayload.getUserId()).isEmpty() && requestPayload.getUserId()!="") 
			{
				
				log.info("Inside userId process ::{}",requestPayload.getUserId());
				userId = requestPayload.getUserId();
				
				//Getting Allocation service response
				Mono<AllocationDTO> allocResp =  this.allocationClient.getAllocationById(userId);
				List<AllocationDTO> logAllocList = allocResp.flux().collectList().block();
				log.info("logAllocList.isNullOrEmpty():{}",isNullOrEmpty(logAllocList));					
				if(!isNullOrEmpty(logAllocList)) {
					List<AllocationResponseContent> logAllocRespList = logAllocList.stream().findFirst().get().getResponseContent();
					log.info("logAllocRespList.isNullOrEmpty():{}",isNullOrEmpty(logAllocRespList));
				}
				
				
				List<AllocationResponseContent> responseList = getAllocResponseContent(allocResp);
				
				//Building ObjectionQuestionsPK String
				String questionList  = "";
				if(responseList!=null)
				{
					for(AllocationResponseContent obj1 : responseList) {
						questionList = questionList+ obj1.getAllocatedQuestion()+',';
					}
				}
				log.debug("questionList::{}",questionList);
				if(questionList.equals(""))
					return errorStatus();
				
				
				//Getting Objection Question response by passing ObjectionQuestionsPK String
				Mono<ObjectionDTO> objectionResp = this.objectionByQstnIdClient.getobjectionQuestion(questionList);
				List<ObjectionDTO> logObjList = objectionResp.flux().collectList().block();
				log.info(">>logObjList.isNullOrEmpty:{}",isNullOrEmpty(logObjList));
				if(!isNullOrEmpty(logObjList)) {
					List<ObjectionResponseContent> logObjRespList = logObjList.stream().findFirst().get().getResponseContent();
					log.info(">>logObjRespList.isNullOrEmpty:{}",isNullOrEmpty(logObjRespList));
				}
				
				
				mappingKey.put("domain", domain);
				mappingKey.put("combineBy", AppConstant.USER_ID);
				Mono<Map<String,String>> monoKeys = Mono.just(mappingKey);
				
				
				return Mono.zip(allocResp,objectionResp,monoExamDtls,monoKeys).map(this::combine);
				
			}
			else if (Optional.ofNullable(requestPayload).isPresent()
					&& !Optional.ofNullable(requestPayload.getSubject()).isEmpty() && requestPayload.getSubject()!="") 
			{
				
				log.info("Inside subject process ::{}",requestPayload.getSubject());
				
				subject = requestPayload.getSubject();
				
				//Getting Objection Question response by Subject
				Mono<ObjectionDTO> objectionResp = this.objectionBySubjectClient.getObjQuestionBySubjet(subject);
				List<ObjectionDTO> logObjList = objectionResp.flux().collectList().block();
				log.info(">>logObjList.isNullOrEmpty:{}",isNullOrEmpty(logObjList));
				if(!isNullOrEmpty(logObjList)) {
					List<ObjectionResponseContent> logObjRespList = logObjList.stream().findFirst().get().getResponseContent();
					log.info(">>logObjRespList.isNullOrEmpty:{}",isNullOrEmpty(logObjRespList));
				}
				
				//Getting Allocation service response by Subject
				Mono<AllocationDTO> allocResp =  this.allocationClient.getAllocationBySubject(subject);
				List<AllocationDTO> logAllocList = allocResp.flux().collectList().block();
				log.info(">>logAllocList.isNullOrEmpty:{}",isNullOrEmpty(logAllocList));
				if(!isNullOrEmpty(logAllocList)) {
					List<AllocationResponseContent> logAllocRespList = logAllocList.stream().findFirst().get().getResponseContent();
					log.info(">>logAllocRespList.isNullOrEmpty:{}",isNullOrEmpty(logAllocRespList));
				}else {
					
					AllocationDTO emptyAllocResp = new AllocationDTO();
					emptyAllocResp.setStatusCode(AppConstant.STS_CD_002);
					emptyAllocResp.setMessage(AppConstant.STS_MSG_002);
					
					allocResp = Mono.just(emptyAllocResp);
				}
				
											
				mappingKey.put("domain", domain);
				mappingKey.put("combineBy", AppConstant.SUBJECT);
				Mono<Map<String,String>> monoKeys = Mono.just(mappingKey);
				
				return Mono.zip(allocResp,objectionResp,monoExamDtls,monoKeys).map(this::combine);
				//return null;
				
			}
			else if (Optional.ofNullable(requestPayload).isPresent()
					&& !Optional.ofNullable(requestPayload.getStatus()).isEmpty() && requestPayload.getStatus()!="") 
			{
				
				log.info("Inside status process ::{}",requestPayload.getStatus());
				status = requestPayload.getStatus();
				
				//Approval process considered PENDING APPROVAL as ALLOCATED
				if(status.equals(AppConstant.STS_PENDING))
					status = AppConstant.STS_ALLOC; 
				
				//Getting Allocation service response
				Mono<AllocationDTO> allocResp =  this.allocationClient.getAllocationByStatus(status);
				List<AllocationDTO> logAllocList = allocResp.flux().collectList().block();
				log.info(">>logAllocList.isNullOrEmpty:{}",isNullOrEmpty(logAllocList));
				if(!isNullOrEmpty(logAllocList)) {
					List<AllocationResponseContent> logAllocRespList = logAllocList.stream().findFirst().get().getResponseContent();
					log.info(">>logAllocRespList.isNullOrEmpty:{}",isNullOrEmpty(logAllocRespList));
				}
		    	
				List<AllocationResponseContent> responseList = getAllocResponseContent(allocResp);
				
				//Building ObjectionQuestionsPK String
				String questionList  = "";
				if(responseList!=null)
				{
					for(AllocationResponseContent obj1 : responseList) {
						questionList = questionList+ obj1.getAllocatedQuestion()+',';
					}
				}
				
				Mono<ObjectionDTO> objectionResp = null;
				log.info("objectionResp::{}",objectionResp);
				
				List<Integer> allocFilterKey = new ArrayList<Integer>();
				if(questionList.equals("")) {	
					
					Mono<AllocationDTO>  allocResp1 =  this.allAllocationClient.getAllAllocation();
					List<AllocationDTO> allocList1 = allocResp1.flux().collectList().block();
					log.info("allocList1.isNullOrEmpty:{}",isNullOrEmpty(allocList1));
					List<AllocationResponseContent> responseList1 = new ArrayList<AllocationResponseContent>();
					if(!isNullOrEmpty(allocList1)) {
						responseList1 = allocList1.stream().findFirst().get().getResponseContent();
					}
					log.debug("responseList1.isNullOrEmpty:{}",isNullOrEmpty(responseList1));
					
					Map<Integer, AllocationResponseContent>  AllocMap = responseList1.stream()
			        		.collect(Collectors.toMap(AllocationResponseContent::getAllocatedQuestion, Function.identity()));
					//System.out.println("AllocMap  :"+AllocMap);
					
					
				    for(Integer key : AllocMap.keySet()) {
				    		allocFilterKey.add(key);
				    }
				    log.info("AllocFilter::{}",allocFilterKey);
					
					
					objectionResp = this.objectionClient.getobjectionQuestion();
					//log.debug("objectionResp::{}",objectionResp);
					//return errorStatus();
				}else {
					//Getting Objection Question response by passing ObjectionQuestionsPK String
					objectionResp = this.objectionByQstnIdClient.getobjectionQuestion(questionList); 
				}
				
				
				List<ObjectionDTO> logObjList = objectionResp.flux().collectList().block();
				log.info("logObjList.isNullOrEmpty:{}",isNullOrEmpty(logObjList));
				if(!isNullOrEmpty(logObjList)) {
					List<ObjectionResponseContent> logObjRespList = logObjList.stream().findFirst().get().getResponseContent();
					log.info("logObjRespList.isNullOrEmpty:{}",isNullOrEmpty(logObjRespList));
				}
				
				mappingKey.put("allocFilterKey", allocFilterKey.toString());
				mappingKey.put("domain", domain);
				mappingKey.put("combineBy", AppConstant.STATUS);
				Mono<Map<String,String>> monoKeys = Mono.just(mappingKey);
				
				return Mono.zip(allocResp,objectionResp,monoExamDtls,monoKeys).map(this::combine);
				
			}
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Exception occured @getObjQuestionQuestions()::{}",e.getMessage());
			e.printStackTrace();
		}  	
		
		return errorStatus();
	}




	/**
	 * @return
	 */
	private Mono<AggregateDTO> errorStatus() {
		AggregateDTO respDTO = new AggregateDTO();
		respDTO.setStatuscode(AppConstant.STS_CD_002);
		respDTO.setMessage(AppConstant.STS_MSG_002);
		
		return Mono.just(respDTO);
	}


	/**
	 * @param requestPayload
	 */
	private String smeIdToUserIdTransition(RequestDTO requestPayload) {
		
		String id = "";
		
		if(Optional.ofNullable(requestPayload).isPresent()
				&& !Optional.ofNullable(requestPayload.getSmeId()).isEmpty() && requestPayload.getSmeId()!="") 
		{
			String smeId = requestPayload.getSmeId();
			
			id = userDetailDelegate.getUserIdBySmeId(smeId);
			System.out.println("getSmeUserId::"+id);
			
		}
		//setting newer value to payload
		requestPayload.setUserId(id);
		
		return id;
	}

	
	private AggregateDTO combine(Tuple4 <AllocationDTO,ObjectionDTO,Map<String, ExamDetails>,Map<String,String>> tuple) {
		
		System.out.println("Inside combine  :"+tuple.size());
    	
		AggregateDTO respDTO = new AggregateDTO() ;
		
		Map<String,String> mapKeys = tuple.getT4();
		
		String userKey = mapKeys.get("smeId");
		String subjectKey = mapKeys.get("subject");
		String statusKey = mapKeys.get("status");
		String domainKey = mapKeys.get("domain");
		String combineKey = mapKeys.get("combineBy");
		String allocKey = mapKeys.get("allocFilterKey");
		
		//Removing Approved/Rejected qstns for YET TO ALLOCATE status
		Map<Integer, AllocationResponseContent>  qstnsActionedList = new HashMap<>();
		if(statusKey.equals("") || statusKey.equals(AppConstant.STS_YTA)) {
			
			if(tuple.getT1().getResponseContent()!=null) 
			{
				List<AllocationResponseContent> AllocatedRespList = tuple.getT1().getResponseContent()
		    																 .stream().collect(Collectors.toList());
				//System.out.println("AllocatedRespList:"+AllocatedRespList);
				
				List<AllocationResponseContent> filteredList = AllocatedRespList.stream()
						.filter(p-> !p.getStatus().equals("ALLOCATED"))
						.collect(Collectors.toList());
				
				qstnsActionedList = filteredList.stream()
		        		.collect(Collectors.toMap(AllocationResponseContent::getAllocatedQuestion, Function.identity()));
				
				System.out.println ("#qstnsActionedList:"+qstnsActionedList);
				
			}
		}
		
    	//GroupingBy ObjectQstnPK and Filtered by ALLOCATED & YET TO ALLOCATE
    	Map<Integer, AllocationResponseContent> allocMap = getGroupByAllocList(tuple,mapKeys);
    	//System.out.println("allocMap  :"+allocMap);
		    	
    	//Only GroupingBy ObjectQstnPK
    	Map<Integer, ObjectionResponseContent> objectMap = getGroupByObjectionList(tuple,mapKeys); 
        //System.out.println("objectMap :"+objectMap);
        
        if(domainKey.equals(AppConstant.ALLOCATION) && 
        		(combineKey.equals(AppConstant.USER_ID) 
        				|| combineKey.equals(AppConstant.STATUS))) {
        	
        	respDTO = getCombinedForAllocation(allocMap, objectMap,allocKey); //ONLY merging
        }
        else if(domainKey.equals(AppConstant.ALLOCATION) && combineKey.equals(AppConstant.SUBJECT)) {
        
        	respDTO = getCombinedAndUpdate(allocMap, objectMap,statusKey,qstnsActionedList); //merging and YTA status change
        }
        
        if( domainKey.equals(AppConstant.APRROVAL)  
        				&&	( combineKey.equals(AppConstant.USER_ID) 
        					||	combineKey.equals(AppConstant.STATUS) 
        						||  combineKey.equals(AppConstant.SUBJECT) ) ) {
        	
        	respDTO = getCombinedForApproval(allocMap, objectMap, statusKey); //ONLY merging
        }
               
        //System.out.println("respDTO:"+respDTO);
        
	    if(Objects.isNull(respDTO.getAllocationQuestion()))
	    {	
	    	respDTO.setStatuscode(AppConstant.STS_CD_002);
	    	respDTO.setMessage(AppConstant.STS_MSG_002);
	    }else {
	    
	    	respDTO.setStatuscode(AppConstant.STS_CD_000);
	    	respDTO.setMessage(AppConstant.STS_MSG_000);
	    }
       
		return respDTO;
        	
    }





	private AggregateDTO getCombinedForApproval(Map<Integer, AllocationResponseContent> allocMap,
			Map<Integer, ObjectionResponseContent> objectMap, String statusKey) {
		// TODO Auto-generated method stub
		AggregateDTO respDTO = new AggregateDTO();
	       
		try {
		
			List<AllocationQuestion> respList = new ArrayList<AllocationQuestion>();
        
			System.out.println("allocMap.size():" + allocMap.size());
			System.out.println("objectMap.size():" + objectMap.size());
			
			if(!allocMap.isEmpty()) {

	        for (Entry<Integer, AllocationResponseContent> entry1 : allocMap.entrySet()) {
	        	//System.out.println("entry1 Key = " + entry1.getKey() +", Value = " + entry1.getValue());
	        	 
	        	for (Entry<Integer, ObjectionResponseContent> entry2 : objectMap.entrySet()) {
	        		 //System.out.println("entry2 Key = " + entry2.getKey() +", Value = " + entry2.getValue());
	        		 
	        		if(entry1.getKey().equals(entry2.getKey()))
	        		 {
	        			 AllocationQuestion resp = new AllocationQuestion();
	        			 System.out.println("Matching Key = " + entry1.getKey() +", Value = " + entry1.getValue());
	        			 
	        			 resp.setAllocationResponseContent(entry1.getValue());
	        			 resp.setObjectionResponseContent(entry2.getValue());
	        			
	        			 if(resp.getAllocationResponseContent().getStatus().equals(AppConstant.STS_ALLOC)) { 
	        			 //changing status ALLOCATED -> PENDING APPROVAL for Approval screen
	        				 resp.getAllocationResponseContent().setStatus(AppConstant.STS_PENDING);
	        			 }
	        			 
	        			 respList.add(resp);
	        			 respDTO.setAllocationQuestion(respList);
	        		 }
	        		
	        	 }
	        }
	        
		}
	        
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Exception occured @combinedBySME()::{}",e.getMessage());
			e.printStackTrace();
		}   
	        
		return respDTO;
	}


	/**
	 * @param allocKey 
	 * @param AllocMap
	 * @param ObjectMap
	 * @return
	 */
	private AggregateDTO getCombinedForAllocation(Map<Integer, AllocationResponseContent> allocMap,
									Map<Integer, ObjectionResponseContent> objectMap, String allocKey) {
		
		AggregateDTO respDTO = new AggregateDTO();
       
		try {
		
			List<AllocationQuestion> respList = new ArrayList<AllocationQuestion>();
        
			System.out.println("allocMap.size():" + allocMap.size());
			System.out.println("objectMap.size():" + objectMap.size());
			
			if(!allocMap.isEmpty()) {
			
	        for (Entry<Integer, AllocationResponseContent> entry1 : allocMap.entrySet()) {
	        	//System.out.println("entry1 Key = " + entry1.getKey() +", Value = " + entry1.getValue());
	        	 
	        	for (Entry<Integer, ObjectionResponseContent> entry2 : objectMap.entrySet()) {
	        		 //System.out.println("entry2 Key = " + entry2.getKey() +", Value = " + entry2.getValue());
	        		 
	        		if(entry1.getKey().equals(entry2.getKey()))
	        		 {
	        			 AllocationQuestion resp = new AllocationQuestion();
	        			 System.out.println("Matching Key = " + entry1.getKey() +", Value = " + entry1.getValue());
	        			 
	        			 resp.setAllocationResponseContent(entry1.getValue());
	        			 resp.setObjectionResponseContent(entry2.getValue());
	        			 
	        			 respList.add(resp);
	        			 respDTO.setAllocationQuestion(respList);
	        		 }
	        		
	        	 }
	        }
	        
		}else {
			
			System.out.println("allocKey:" + allocKey);
			
			for (Entry<Integer, ObjectionResponseContent> entry1 : objectMap.entrySet()) {
				
				//System.out.println("entry1.getKey():" + entry1.getKey());
				//System.out.println("entry1.getValue():" + entry1.getValue());
				
				if(!allocKey.contains(entry1.getKey().toString())) {
					
					 AllocationQuestion respEmpty = new AllocationQuestion();
					 AllocationResponseContent allocRespSTS = new AllocationResponseContent();
					 
					 //Assigning YET_TO_ALLOCATE Status for non matching pairs
					 allocRespSTS.setStatus(AppConstant.STS_YTA);
					 respEmpty.setAllocationResponseContent(allocRespSTS);
					 
					 respEmpty.setObjectionResponseContent(entry1.getValue()); 
					 
					 respList.add(respEmpty);
					 respDTO.setAllocationQuestion(respList);
				}
			}
			
		}
	        
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Exception occured @combinedBySME()::{}",e.getMessage());
			e.printStackTrace();
		}   
	        
		return respDTO;
	}
	

	/**
	 * @param statusKey 
	 * @param onlyAllocatedQstn 
	 * @param onlyAllocatedQstn 
	 * @param userKey 
	 * @param AllocMap
	 * @param ObjectMap
	 * @return
	 */
	private AggregateDTO getCombinedAndUpdate(Map<Integer, AllocationResponseContent> allocMap,
											Map<Integer, ObjectionResponseContent> objectMap, String statusKey, Map<Integer, AllocationResponseContent> qstnsActionedList) {
		
		AggregateDTO respDTO = new AggregateDTO();
		
		
		try {
			
			List<AllocationQuestion> respList = new ArrayList<AllocationQuestion>();

			List<Integer> result = new ArrayList<Integer>();
		    for(Integer key : objectMap.keySet()) {
			    if (allocMap.containsKey(key)) {
			    	result.add(key);
			    }
		    }
		    System.out.println("Alloc Key -> = " +result);
		    
		    List<Integer> nonAllocFilter = new ArrayList<Integer>();
		    for(Integer key : objectMap.keySet()) {
			    if (qstnsActionedList.containsKey(key)) {
			    	nonAllocFilter.add(key);
			    }
		    }
		    System.out.println("nonAllocFilter Key -> = " +nonAllocFilter);
			
			if(!result.isEmpty()) {
				
				for (Entry<Integer, ObjectionResponseContent> entry1 : objectMap.entrySet()) {
					// System.out.println("entry1 Key = " + entry1.getKey() +", Value = " + entry1.getValue());
				
					if(statusKey.equals(AppConstant.STS_ALLOC) && result.contains(entry1.getKey())) {
						
						 AllocationQuestion resp = new AllocationQuestion();
						 System.out.println("Matching Key = " + entry1.getKey() +", Value = " + entry1.getValue());
						 
						 resp.setAllocationResponseContent(allocMap.get(entry1.getKey()));
						 resp.setObjectionResponseContent(entry1.getValue());
						 
						 respList.add(resp);
						 respDTO.setAllocationQuestion(respList);
						
					}else if(statusKey.equals(AppConstant.STS_YTA)) {
						
						if(!result.contains(entry1.getKey())) {
						//System.out.println("NON Matching Key = " + entry1.getKey());
						
						 AllocationQuestion respEmpty = new AllocationQuestion();
						 AllocationResponseContent allocRespSTS = new AllocationResponseContent();
						 
						 //Assigning YET_TO_ALLOCATE Status for non matching pairs
						 allocRespSTS.setStatus(AppConstant.STS_YTA);
						 respEmpty.setAllocationResponseContent(allocRespSTS);
						 
						 respEmpty.setObjectionResponseContent(entry1.getValue()); 
						 
						 respList.add(respEmpty);
						 respDTO.setAllocationQuestion(respList);
						
						}
						
					}
					else if(statusKey.equals("")) {
						
						
						if(result.contains(entry1.getKey())) {
							
							 AllocationQuestion resp = new AllocationQuestion();
							 System.out.println("Matching Alloc Key = " + entry1.getKey());
							 
							 resp.setAllocationResponseContent(allocMap.get(entry1.getKey()));
							 resp.setObjectionResponseContent(entry1.getValue());
							 
							 respList.add(resp);
							 respDTO.setAllocationQuestion(respList);
							
						}
						else {
							
							if(!nonAllocFilter.contains(entry1.getKey())) {
							
						    System.out.println("Non Matching Alloc Key = " + entry1.getKey());
							
							AllocationQuestion respEmpty = new AllocationQuestion();
							AllocationResponseContent allocRespSTS = new AllocationResponseContent();
							 
							 //Assigning YET_TO_ALLOCATE Status for non matching pairs
							 allocRespSTS.setStatus(AppConstant.STS_YTA);
							 respEmpty.setAllocationResponseContent(allocRespSTS);
							 
							 respEmpty.setObjectionResponseContent(entry1.getValue()); 
							 
							 respList.add(respEmpty);
							 respDTO.setAllocationQuestion(respList);
							 
							}
						}
					}
						
				
				}
			}
			else {
				
				for (Entry<Integer, ObjectionResponseContent> entry1 : objectMap.entrySet()) {
					
					if(!nonAllocFilter.contains(entry1.getKey()) && !statusKey.equals(AppConstant.STS_ALLOC)) {
						 AllocationQuestion respEmpty = new AllocationQuestion();
						 AllocationResponseContent allocRespSTS = new AllocationResponseContent();
						 
						 //Assigning YET_TO_ALLOCATE Status for non matching pairs
						 allocRespSTS.setStatus(AppConstant.STS_YTA);
						 respEmpty.setAllocationResponseContent(allocRespSTS);
						 
						 respEmpty.setObjectionResponseContent(entry1.getValue()); 
						 
						 respList.add(respEmpty);
						 respDTO.setAllocationQuestion(respList);
					}
					
				}
				
			}

			
			
//			for (Entry<Integer, ObjectionResponseContent> entry1 : objectMap.entrySet()) {
//				 System.out.println("entry2 Key = " + entry1.getKey() +", Value = " + entry1.getValue());
//
//				if(!allocMap.isEmpty()) {
//				
//				    for (Entry<Integer, AllocationResponseContent> entry2 : allocMap.entrySet()) {
//				    	System.out.println("entry1 Key = " + entry2.getKey() +", Value = " + entry2.getValue());
//				    	
//						if(entry1.getKey().equals(entry2.getKey()))
//						 {
//							 AllocationQuestion resp = new AllocationQuestion();
//							 System.out.println("Matching Key = " + entry1.getKey() +", Value = " + entry1.getValue());
//							 
//							 resp.setAllocationResponseContent(entry2.getValue());
//							 resp.setObjectionResponseContent(entry1.getValue());
//							 
//							 respList.add(resp);
//							 respDTO.setAllocationQuestion(respList);
//							 continue;
//							 
//						 }
//						 else if(!statusKey.equals(AppConstant.STS_ALLOC))
//						 {
//							 AllocationQuestion respEmpty = new AllocationQuestion();
//							 AllocationResponseContent allocRespSTS = new AllocationResponseContent();
//							 
//							 //Assigning YET_TO_ALLOCATE Status for non matching pairs
//							 allocRespSTS.setStatus(AppConstant.STS_YTA);
//							 respEmpty.setAllocationResponseContent(allocRespSTS);
//							 
//							 respEmpty.setObjectionResponseContent(entry1.getValue()); 
//							 
//							 respList.add(respEmpty);
//							 respDTO.setAllocationQuestion(respList);
//						 }
//						
//					 }
//				}
//				else
//				{					
//					 AllocationQuestion respEmpty = new AllocationQuestion();
//					 AllocationResponseContent allocRespSTS = new AllocationResponseContent();
//					 
//					 //Assigning YET_TO_ALLOCATE Status for non matching pairs
//					 allocRespSTS.setStatus(AppConstant.STS_YTA);
//					 respEmpty.setAllocationResponseContent(allocRespSTS);
//					 
//					 respEmpty.setObjectionResponseContent(entry1.getValue()); 
//					 
//					 respList.add(respEmpty);
//					 respDTO.setAllocationQuestion(respList);
//				 }
//				
//				
//			}
			
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Exception occured @combinedBySubject()::{}",e.getMessage());
			e.printStackTrace();
		}	
	
		
		return respDTO;
	}
	

	/**
	 * @param allocResp
	 * @return
	 */
	private List<AllocationResponseContent> getAllocResponseContent(Mono<AllocationDTO> allocResp) {
		
		List<AllocationDTO> allocList = allocResp.flux().collectList().block();
		//allocList.forEach(System.out::println);
  	
		List<AllocationResponseContent> responseList = new ArrayList<AllocationResponseContent>();
		if(!allocList.isEmpty()) {
			responseList = allocList.stream().findFirst().get().getResponseContent();
		}
		
		return responseList;
	}
	
	
	/**
	 * @param tuple
	 * @param mapKeys 
	 * @return
	 */
	private Map<Integer, ObjectionResponseContent> getGroupByObjectionList(Tuple3<AllocationDTO, ObjectionDTO,Map<String, ExamDetails>> tuple, Map<String, String> mapKeys) {
		
		String examFilter = mapKeys.get("examName");
		String dateFilter = mapKeys.get("date");
		String batchFilter = mapKeys.get("batch");
		
		List<ObjectionResponseContent> resultList = new ArrayList<>();	
		 
	 	Predicate<ObjectionResponseContent> isDate = null,isBatch = null,isExamName = null;

		List<Predicate<ObjectionResponseContent>> predicatelist = new ArrayList<>();
		
		Map<Integer, ObjectionResponseContent>  ObjectMap = new HashMap<>();
		
		System.out.println(">>tuple.getT2().getResponseContent().size():"+tuple.getT2().getResponseContent().size());
		
		if(tuple.getT2().getResponseContent()!=null) 
		{
			List<ObjectionResponseContent> ObjectRespList = tuple.getT2().getResponseContent()
	    																 .stream().collect(Collectors.toList());
			System.out.println(">>ObjectRespList:"+ObjectRespList);
			
//			Map<String, ExamDetails> examDtls = tuple.getT3();
//			List<String> result = examDtls.keySet().stream()
//	                .collect(Collectors.toList());
//			for (ObjectionResponseContent obj : ObjectRespList) {
//				if(result.contains(obj.getQbId()+"")) {
//					ExamDetails exmObj = examDtls.get(obj.getQbId()+"");
//					//System.out.println ("###exmObj:"+exmObj);
//					obj.setDate(exmObj.getDate());
//					obj.setMdmName(exmObj.getExamName());
//					
//					String batch = exmObj.getBatch().stream().findFirst().get().toString();
//					String fmtBatch = batch.replaceAll("[{ }]*","");
//					obj.setBatch(fmtBatch);
//				}
//			}
			//System.out.println ("ObjectRespList:"+ObjectRespList);
			
			//Filter by exam name
			if(!isNullOrEmpty(examFilter)) 
			{ 
				isExamName = fr -> fr.getModuleName().equals(examFilter);
				predicatelist.add(isExamName);
				log.info("#predicatelist.add(isExamName)");
			}
			
			//Filter by Date and Batch
			if(!isNullOrEmpty(dateFilter)) 
			{ 
				isDate = fr -> fr.getExamDate().equals(dateFilter);
				predicatelist.add(isDate);
				log.info("#predicatelist.add(isDate)");
			}
			
			if(!isNullOrEmpty(batchFilter)) 
			{ 
				isBatch = fr -> fr.getExamBatch().equals(batchFilter);
				predicatelist.add(isBatch);
				log.info("#predicatelist.add(isBatch)");
			}
			
			log.info("#predicatelist::{}",predicatelist.size());
			
			if(predicatelist.size() > 0) {
				Predicate<ObjectionResponseContent> framePredicateFilters = p -> !p.getSubject().equals(null);
				for(Predicate<ObjectionResponseContent> filterObj: predicatelist) {
					framePredicateFilters = framePredicateFilters.and(filterObj);
				}
				resultList = ObjectRespList.stream()
					      .filter( framePredicateFilters )
					      .collect(Collectors.toList());
				log.info("#resultList::{}",resultList);
			}else {
				resultList = ObjectRespList ;
			}
			
			

			ObjectMap = resultList.stream()
	        		.collect(Collectors.toMap(ObjectionResponseContent::getObjquesId, Function.identity()));
	        
	        
//	        Map<String, Long>  testCount = ObjectRespList.stream()
//	        		.map(p->p.getSubject())
//					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//	        
//	        System.out.println("ObjectRespList:"+testCount);
	      
		}
	        
		return ObjectMap;
		
	}


	/**
	 * @param tuple
	 * @param mapKeys 
	 * @param mapKeys 
	 * @return
	 */
	private Map<Integer, AllocationResponseContent> getGroupByAllocList(Tuple2<AllocationDTO, ObjectionDTO> tuple, Map<String, String> mapKeys) {
				
		String userFilter = mapKeys.get("smeId");
		String subjectFilter = mapKeys.get("subject");
		String statusFilter = mapKeys.get("status");
		String domainKey = mapKeys.get("domain");
		System.out.println("mapKeysFilters:"+mapKeys);
		
		//Approval process considered PENDING APPROVAL as ALLOCATED
		if(statusFilter.equals(AppConstant.STS_PENDING))
			statusFilter = AppConstant.STS_ALLOC;
		
		
		Map<Integer, AllocationResponseContent>  AllocMap = new HashMap<Integer, AllocationResponseContent>();
		
		System.out.println("tuple.getT1():"+tuple.getT1());
	
		if(tuple.getT1().getResponseContent()!=null) 
		{
			List<AllocationResponseContent> AllocRespList = tuple.getT1().getResponseContent()
	    																 .stream().collect(Collectors.toList());
			System.out.println("AllocRespList:"+AllocRespList);
			
			List<AllocationResponseContent> filteredList = new ArrayList<AllocationResponseContent>();
			if(!domainKey.equals(AppConstant.APRROVAL)) {
				
				if(statusFilter.equals(AppConstant.STS_ALLOC) || statusFilter.equals("")) {
				
					System.out.println("STS_ALLOC || BLANK  :: statusFilter:"+statusFilter);
					filteredList = getFilteredByAllocStatus(AllocRespList);
				}
				else if(statusFilter.equals(AppConstant.STS_YTA) && userFilter.equals("")) {
				
					System.out.println("STS_ALLOC :: statusFilter:"+statusFilter);
					filteredList = AllocRespList ;
					
					 AllocMap = filteredList.stream()
				        		.collect(Collectors.toMap(AllocationResponseContent::getAllocatedQuestion, Function.identity()));
				
					 return AllocMap;
				}
				else
				{
					filteredList = AllocRespList;
				}
				
					
			}
			else
			{
				filteredList = AllocRespList;
			}
			
			//filter by UI controls
			List<AllocationResponseContent> finalList = filterByInputControl(userFilter, subjectFilter, statusFilter, filteredList,domainKey);
			
			
	         AllocMap = finalList.stream()
	        		.collect(Collectors.toMap(AllocationResponseContent::getAllocatedQuestion, Function.identity()));
		}
		
        return AllocMap;
	}



	/**
	 * @param AllocRespList
	 * @return
	 */
	private List<AllocationResponseContent> getFilteredByAllocStatus(List<AllocationResponseContent> RespList) {
		
		List<AllocationResponseContent> filteredList = RespList.stream()
				.filter(p-> !p.getStatus().equals("APPROVED"))
				.filter(q-> !q.getStatus().equals("REJECTED"))
				.collect(Collectors.toList());
		System.out.println ("filteredList:"+filteredList);
		
		return filteredList;
	}


	


	private List<AllocationResponseContent> filterByInputControl(String userFilter, String subjectFilter, String statusFilter,
			List<AllocationResponseContent> filteredList, String domainKey) {
		
		
		List<AllocationResponseContent> finalList = new ArrayList<AllocationResponseContent>();
		
		if (filteredList!=null) {
				
		
			if ((userFilter != "" && subjectFilter == "" && statusFilter == "")
					|| (userFilter == "" && subjectFilter != "" && statusFilter == "")
					|| (userFilter == "" && subjectFilter == "" && statusFilter != "")) {

				System.out.println("pattern#0 -> no filters");
				finalList = filteredList; //No filter combination found

			}
			if (userFilter != "" && subjectFilter != "" && statusFilter != "") {

				System.out.println("pattern#1 -> all filters");
				
				List<AllocationResponseContent> filteredBySmeID = filteredList.stream()
						.filter(p -> p.getIdentifyNumber().equals(userFilter))
						.collect(Collectors.toList());
				System.out.println("filteredBySmeID:" + filteredBySmeID + " :" + userFilter);
				
				if (!filteredBySmeID.isEmpty()) {
				
					List<AllocationResponseContent> filteredBySubject = filteredBySmeID.stream()
							.filter(p -> p.getSubject().equals(subjectFilter)).collect(Collectors.toList());
					System.out.println("filteredBySubject:" + filteredBySubject + " :" + subjectFilter);
	
					if (!filteredBySubject.isEmpty()) {
						finalList = filteredBySubject.stream().filter(p -> p.getStatus().equals(statusFilter))
								.collect(Collectors.toList());
						System.out.println("filteredByStatus:" + finalList + " :" + statusFilter);
						
						return finalList;
					} else {
						finalList = filteredBySubject;
						return finalList;
					}
				
				}else {
					finalList = filteredBySmeID;
					return finalList;
				}
				
			}
			if (userFilter != "" && subjectFilter != "" && statusFilter == "") {

				System.out.println("pattern#2 -> user-subject combo");
				
				List<AllocationResponseContent> filteredBySmeID = filteredList.stream()
						.filter(p -> p.getIdentifyNumber().equals(userFilter))
						.collect(Collectors.toList());
				System.out.println("filteredBySmeID:" + filteredBySmeID + " :" + userFilter);
				
				if (!filteredBySmeID.isEmpty()) {
					finalList = filteredBySmeID.stream().filter(p -> p.getSubject().equals(subjectFilter))
							.collect(Collectors.toList());
					System.out.println("filteredBySubject:" + finalList + " :" + subjectFilter);
				} else {
					finalList = filteredBySmeID;
				}

			}
			if (userFilter != "" && statusFilter != "" && subjectFilter == "") {

				System.out.println("pattern#3 -> user-status combo");
				

				List<AllocationResponseContent> filteredBySmeID = filteredList.stream()
						.filter(p -> p.getIdentifyNumber().equals(userFilter))
						.collect(Collectors.toList());
				System.out.println("filteredBySmeID:" + filteredBySmeID + " :" + userFilter);
				
				if (!filteredBySmeID.isEmpty()) {
				
				finalList = filteredBySmeID.stream().filter(p -> p.getStatus().equals(statusFilter))
						.collect(Collectors.toList());
				System.out.println("filteredByStatus:" + finalList + " :" + statusFilter);
				
				}else {
					finalList = filteredBySmeID;
				}
			}
			if (userFilter == "" && subjectFilter != "" && statusFilter != "") {

				System.out.println("pattern#4 -> subject-status combo");
				finalList = filteredList.stream().filter(p -> p.getStatus().equals(statusFilter))
						.collect(Collectors.toList());
				System.out.println("filteredByStatus:" + finalList + " :" + statusFilter);
			}
		
			
		}
		
		if(finalList.isEmpty() && !domainKey.equals(AppConstant.APRROVAL)) {
			finalList = filteredList;
		}
		
		System.out.println("finalFilterList::" + finalList);
		
		return finalList;
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
