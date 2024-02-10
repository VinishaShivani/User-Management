package com.nseit.trb.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nseit.trb.client.AllAllocationClient;
import com.nseit.trb.client.ObjectionClient;
import com.nseit.trb.client.SubjectDetailClient;
import com.nseit.trb.client.TotalObjectionClient;
import com.nseit.trb.client.constant.AppConstant;
import com.nseit.trb.delegate.UserDetails;
import com.nseit.trb.dto.AllocationDTO;
import com.nseit.trb.dto.AllocationResponseContent;
import com.nseit.trb.dto.BatchInfoDTO;
import com.nseit.trb.dto.DashBoardBySme;
import com.nseit.trb.dto.DashBoardBySubject;
import com.nseit.trb.dto.DashBoardTiles;
import com.nseit.trb.dto.DashboardDTO;
import com.nseit.trb.dto.ObjectionDTO;
import com.nseit.trb.dto.ObjectionResponseContent;
import com.nseit.trb.dto.TotalObjectionDTO;
import com.nseit.trb.dto.TotalObjectionResponseContent;
import com.nseit.trb.dto.UserDtlResponseContent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class DashBoardService {
	
	
	 	private final AllAllocationClient allAllocationClient;
	    private final ObjectionClient objectionClient;
	    private final TotalObjectionClient totalObjection;
	    private final SubjectDetailClient subjectClient;
	    
	    @Autowired
	    UserDetails userDetailDelegate;
	    	    
	    
		public Mono<DashboardDTO> getDashboardBySubject() {
			// TODO Auto-generated method stub
			
			log.info("Dashboard For Subject starts");
			
			//Get Allocation service response
			Map<String, Map<String, Long>> allocBySubMap =  getAllocBySubjectCount();
			System.out.println("allocBySubMap:"+allocBySubMap);
			
//			Map<String, Map<String, Long>> allocBySubMap = dump.getDumpData();
//			log.info("allocBySubMap:{}",allocBySubMap);
			
			
			//Get Objection Question service response
			Map<String, Long> objBySubMap = getObjSubjectCount();
			log.info("objBySubMap:{}",objBySubMap);
		        
		        
		        DashboardDTO respDTO = new DashboardDTO();
		        List<DashBoardBySubject> respList = new ArrayList<DashBoardBySubject>();
		      
		        for (Entry<String, Long> entry1 : objBySubMap.entrySet()) {
	        		 //System.out.println("entry2 Key = " + entry2.getKey() +", Value = " + entry2.getValue());
	        		 
		        		 DashBoardBySubject resp = new DashBoardBySubject();
		        		 log.info("Matching Key:{} {} ",entry1.getKey(),entry1.getValue());
	        			 
	        			 long uniqueQuestion =0;
	        			 String subject = entry1.getKey() ;
	        			 resp.setSubject(subject);
	        			 
	        			 uniqueQuestion = entry1.getValue();
						 resp.setPendingAllocation(uniqueQuestion);
	        			 //resp.setUniqueQuestions(uniqueQuestion);
	        			 
	        			 
	        			 //Get Total Object Questions by subject  
	        			 TotalObjectionResponseContent totObjRespList = getTotalObjectionsCount(subject);
	        			 if(totObjRespList!=null) {
	        				
	        				 resp.setUniqueQuestions(totObjRespList.getNoofUniqueQuestion());
	        				 resp.setTotalQuestions(totObjRespList.getNoofObjection());
	        			 }
	        			 
	        			 //log.info("totObjCount:{}",totObjCount);
	     		         //resp.setTotalQuestions(totObjCount);
	     		        
	     		        
	     		         if(!allocBySubMap.isEmpty()) {
	        				 
	        				 for (Entry<String, Map<String, Long>> entry2 : allocBySubMap.entrySet()) {
	        					 
	        					 if(entry2.getKey().equals(entry1.getKey()))
	        	        		 {	        						 
	        						 long approveCount =0;
	        						 long rejectCount =0;
	        						 long pendingCount =0;
	        						 long pendingAlloc =0;
	        						 
	        						 
	        						 Map<String, Long> internalMap = entry2.getValue();
	        						 //System.out.println("internal = " + internalMap);
	        						 
	        						 for (Entry<String, Long> entry3 : internalMap.entrySet()) {
	        							 
	        							 if(entry3.getKey().equals(AppConstant.STS_APPROVED)) {
	        								 approveCount = entry3.getValue();
	        							 }else if(entry3.getKey().equals(AppConstant.STS_REJECTED)) {
	        								 rejectCount = entry3.getValue();
	        							 }else if(entry3.getKey().equals(AppConstant.STS_ALLOC)) {
	        								 pendingCount = entry3.getValue();
	        							 }
	        							 
	        						 }
	        						 
	        						 System.out.println("approveCount = " + approveCount);
	        						 System.out.println("rejectCount = " + rejectCount);
	        						 System.out.println("pendingCount = " + pendingCount);
	        						 System.out.println("uniqueQuestion = " + uniqueQuestion);
        						 
	        			 			resp.setApproved(approveCount);
	        			 			resp.setRejected(rejectCount);
	        			 			resp.setPendingApproval(pendingCount);
	        			 			
	        			 			pendingAlloc = uniqueQuestion - (approveCount+rejectCount +pendingCount);
	        			 			
	        			 			resp.setPendingAllocation(pendingAlloc);
	        	        		 }
	        					 
	        				 }
	        			 }
	        			 
	        			 respList.add(resp);
	        			 respDTO.setDashBoardBySubject(respList);
	        		 }
	        		
	        
		        log.info("respDTO:{}",respDTO);
		        
			    if(Objects.isNull(respDTO.getDashBoardBySubject()))
			    {	
			    	respDTO.setStatuscode(AppConstant.STS_CD_002);
			    	respDTO.setMessage(AppConstant.STS_MSG_002);
			    }else {
			    
			    	respDTO.setStatuscode(AppConstant.STS_CD_000);
			    	respDTO.setMessage(AppConstant.STS_MSG_000);
			    }
		        
		        
		        Mono<DashboardDTO> response = Mono.just(respDTO)  ; 
			
			
			return response;
		}


		/**
		 * @return
		 */
		private Map<String, Map<String, Long>> getAllocBySubjectCount() {
			
			Mono<AllocationDTO> allocResp =  this.allAllocationClient.getAllAllocation();
			
			Map<String, Map<String, Long>> multipleFieldsMap = new HashMap<String, Map<String, Long>>();
			List<AllocationDTO> allocRespList = allocResp.flux().collectList().block();
			
			System.out.println("allocRespList::"+allocRespList);
			
			if(!allocRespList.isEmpty()) {
				
				List<AllocationResponseContent> allocResponseList =
					allocRespList.stream().findFirst().get().getResponseContent();
			
			
				
				if(allocResponseList!=null) {
					multipleFieldsMap = allocResponseList.stream()
		                .collect(
		                        Collectors.groupingBy(AllocationResponseContent::getSubject,
		                                Collectors.groupingBy(AllocationResponseContent::getStatus,
		                                        Collectors.counting())));
				}
			}
						
			return multipleFieldsMap;
		}


	


		/**
		 * @return
		 */
		private Map<String, Long> getObjSubjectCount() {
			
			Mono<ObjectionDTO> objetResp =  this.objectionClient.getobjectionQuestion();
			
			List<ObjectionDTO> objetRespList = objetResp.flux().collectList().block();
			
			List<ObjectionResponseContent> responseList =
					objetRespList.stream().findFirst().get().getResponseContent();
			
			//System.out.println("Object responseList:"+responseList);
			
			 Map<String, Long>  testCount = new HashMap<String, Long>();
			 if(responseList!=null) {
				 testCount = responseList.stream()
		        		.map(p->p.getSubject())
						.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			 }
			 
			return testCount;
		}


		/**
		 * @param subject
		 * @return
		 */
//		private long getTotalObjectionCount(String subject) {
//			
//			Mono<ObjectionDTO> totalObjQstnResp =  this.totalObjection.getobjectionQuestion(subject);
//			 List<ObjectionDTO> totalObjQstnRespList = totalObjQstnResp.flux().collectList().block();
//				
//			 List<ObjectionResponseContent> totRespContList =
//					 totalObjQstnRespList.stream().findFirst().get().getResponseContent();
//			 
//			 System.out.println("totRespContList:"+totRespContList);
//			 
//			// List<CandidateObjection> candiObjList =
//				///	 totRespContList.stream().findFirst().get().getCandidateObjctions();
//			 
//			 long totObjCount = 0;
//			 for(ObjectionResponseContent obj : totRespContList) {
//				 
//				 List<CandidateObjction> candidateObjList = obj.getCandidateObjctions();
//				 
//				 Map<String, Long>  objCountMap = candidateObjList.stream()
//			        		.map(p->p.getSubject())
//							.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//				 
//				 totObjCount += objCountMap.get(subject);
//				 System.out.println("objCountMap:"+objCountMap);
//			 }
//			 
//			
//			 System.out.println("totObjCount:"+totObjCount);
//			return totObjCount;
//		}


		public Mono<DashboardDTO>  getDashboardBySme(String smeId) {
			// TODO Auto-generated method stub
			
			log.info("Dashboard For Subject starts");
			
			DashboardDTO respDTO = new DashboardDTO();
			List<DashBoardBySme> respList = new ArrayList<DashBoardBySme>();
		     
			
			//Get Allocation service response
			Map<String, Map<String, Map<String, Long>>> smeAllocBySubMap =  getAllocByUserAndSubjectCount();
			log.info("smeAllocBySubMap:{}"+smeAllocBySubMap);
			
			
			Mono<BatchInfoDTO> subResp =  this.subjectClient.getSubjectDtls();
			List<BatchInfoDTO> subRespList = subResp.flux().collectList().block();
			List<String> subjectList = subRespList.stream().findFirst().get().getSubjects();
			log.info("subRespList:{}"+subjectList);
			if(!isNullOrEmpty(subjectList)) {
			
			 for(String subject: subjectList) {
				
				
				 //log.info("objTest:{}"+objTest);
		
			 //for(Entry<String, Map<String, Map<String, Long>>> entry1 : smeAllocBySubMap.entrySet()) {
				 
				 log.info("SUBJECT :{}",subject);
				 
				 //String subject = entry1.getKey();
				 //Get Total Object Questions by subject  
				 Map<String, UserDtlResponseContent> userDtlMapSub = userDetailDelegate.getUsersBySubject(subject);
				 //log.info("Subject - userDtlMap :{}",userDtlMapSub);
				 
				 List<String> allocUserFilter = new ArrayList<>();
				 List<String> userFilterKey = new ArrayList<>();
				 for(String key : userDtlMapSub.keySet()) {
					 userFilterKey.add(key);
			     }
				 System.out.println("userFilterKey :"+userFilterKey);
				 System.out.println("allocUserFilter :"+allocUserFilter);
			 
				 //Map<String, Map<String, Long>> internalMap1 = entry1.getValue();
				 Map<String, Map<String, Long>> internalMap1	= smeAllocBySubMap.get(subject);
				 log.info("internalMap1:{}",internalMap1);
				 
				 if(!isNullOrEmpty(internalMap1)) {
					 
					 for(Entry<String, Map<String, Long>> entry2 : internalMap1.entrySet()) {
				
					 DashBoardBySme smeDTO = new DashBoardBySme();
					 String userId = entry2.getKey();
					 
					 allocUserFilter.add(userId);
					 log.info("userId :{}",entry2.getKey());
					 log.info("status :{}",entry2.getValue());
					 
					 //smeDTO.setSubject(entry1.getKey());
					 smeDTO.setSubject(subject);
					 
					 
					 if(userFilterKey.contains(userId)) {
						 UserDtlResponseContent userDtl = userDtlMapSub.get(userId);
						 smeDTO.setSmeName(userDtl.getUserId());
						 smeDTO.setExpiryDays(userDtl.getValdityInDays());
						 
					 }else {
						 smeDTO.setSmeName(userId);
						 smeDTO.setExpiryDays(0);
					 }
						
					 
					 Map<String, Long> internalMap2 = entry2.getValue();
					 long totalAllocated =0;
					 long approveCount =0;
					 long rejectCount =0;
					 long pendingCount =0;
					 
					 for (Entry<String, Long> entry3 : internalMap2.entrySet()) {
						 					
						 System.out.println("status-count :"+entry3.getKey()+"-"+entry3.getValue());
						 System.out.println("entry3 :"+entry3.getValue());
						 
						 if(entry3.getKey().equals(AppConstant.STS_APPROVED)) {
							 approveCount = entry3.getValue();
						 }else if(entry3.getKey().equals(AppConstant.STS_REJECTED)) {
							 rejectCount = entry3.getValue();
						 }else if(entry3.getKey().equals(AppConstant.STS_ALLOC)) {
							 pendingCount = entry3.getValue();
						 }
						 	 
						 smeDTO.setApproved(approveCount);
						 smeDTO.setRejected(rejectCount);
						 smeDTO.setPendingApproval(pendingCount);
					 }
					 
					 System.out.println("approveCount = " + approveCount);
					 System.out.println("rejectCount = " + rejectCount);
					 System.out.println("pendingCount = " + pendingCount);
					 //System.out.println("uniqueQuestion = " + uniqueQuestion);
					 
					 totalAllocated = approveCount+rejectCount +pendingCount;
					 smeDTO.setAllocatedQuestions(totalAllocated);
					 log.info("totalAllocated :{}",totalAllocated);
					 
					 respList.add(smeDTO);
					
				}
				 
			  }
					 
					 
				 //setting unallocated but valid users 
				 userFilterKey.removeAll(allocUserFilter);
				 System.out.println(">>>userFilterKey:" + userFilterKey);
				 
				 if(!userFilterKey.isEmpty()) { 
					 for(String idNumber :userFilterKey) {
						 
						 DashBoardBySme nonAllocSmeDTO = new DashBoardBySme(); 
						 UserDtlResponseContent userDtl = userDtlMapSub.get(idNumber);
						 nonAllocSmeDTO.setSmeName(userDtl.getUserId());
						 nonAllocSmeDTO.setExpiryDays(userDtl.getValdityInDays());
						 nonAllocSmeDTO.setSubject(subject);
						 
						 respList.add(nonAllocSmeDTO);
					 }
				 }
				 respDTO.setDashBoardBySme(respList);
//			}
			 
			}
		
			}
			 
			if(smeId!=null && respDTO!=null ) { 
				
				 List<DashBoardBySme> dtoList = respDTO.getDashBoardBySme();
				 //log.info("dtoList:{}",dtoList);
				 
				 List<DashBoardBySme> respDtoList =
						 dtoList.stream()
						 .filter(p -> p.getSmeName().equals(smeId))
	    	             .collect(Collectors.toList());
				 
				 log.info("filteredBySme: {} :: {}",smeId,respDtoList);
				 
				 respDTO.setDashBoardBySme(respDtoList);
				 
			}

			 
			 
			 log.info("respDTO:{}",respDTO);
		        
			    if(Objects.isNull(respDTO.getDashBoardBySme()))
			    {	
			    	respDTO.setStatuscode(AppConstant.STS_CD_002);
			    	respDTO.setMessage(AppConstant.STS_MSG_002);
			    }else {
			    
			    	respDTO.setStatuscode(AppConstant.STS_CD_000);
			    	respDTO.setMessage(AppConstant.STS_MSG_000);
			    }
			        
				  
			 
			 Mono<DashboardDTO> response = Mono.just(respDTO)  ;
	
			 return response;			
			
		}
		
		
		

		
		private Map<String, Map<String, Map<String, Long>>> getAllocByUserAndSubjectCount() {
			
			Map<String, Map<String, Map<String, Long>>> multipleFieldsMap 
											= new HashMap<String, Map<String, Map<String, Long>>>();
			
			Mono<AllocationDTO> allocResp =  this.allAllocationClient.getAllAllocation();
		
			List<AllocationDTO> allocRespList = allocResp.flux().collectList().block();
			
			if(!allocRespList.isEmpty()) {
			
				List<AllocationResponseContent> allocResponseList =
				allocRespList.stream().findFirst().get().getResponseContent();
				
	//			AllocationDTO resp = dump.getAllocDumpData();
	//			List<AllocationResponseContent> allocResponseList = resp.getResponseContent();
	
				
				
				
				if(allocResponseList!=null) {
					multipleFieldsMap = allocResponseList.stream()
		                .collect(
		                        Collectors.groupingBy(AllocationResponseContent::getSubject,
		                        		Collectors.groupingBy(AllocationResponseContent::getUserId,
		                                Collectors.groupingBy(AllocationResponseContent::getStatus,
		                                        Collectors.counting()))));
				}
			
			}
			log.info("multipleFieldsMap:{}",multipleFieldsMap);
			
			 return multipleFieldsMap;
			
			 
		}


		public Mono<DashboardDTO>  getDashboardTilesData() {
			// TODO Auto-generated method stub
			
			
			//Get Allocation service response
			Map<String, Map<String, Long>> allocBySubMap =  getAllocBySubjectCount();
			System.out.println("allocBySubMap:"+allocBySubMap);
			
			//Map<String, Map<String, Long>> allocBySubMap = dump.getDumpData();
			//log.info("allocBySubMap:{}",allocBySubMap);
			
			//Get Objection Question service response
			Map<String, Long> objBySubMap = getObjSubjectCount();
			log.info("objBySubMap:{}",objBySubMap);
		    
		    DashboardDTO respDTO = new DashboardDTO();
		    DashBoardTiles resp = new DashBoardTiles();
			  
			    long approveCount =0;
				long rejectCount =0;
				long pendingCount =0;
				long pendingAlloc =0;
				long uniqueQuestion =0;
			
			    for (Entry<String, Long> entry1 : objBySubMap.entrySet()) {
			    	
			    	uniqueQuestion += entry1.getValue();
			    }
		    	
				if(!allocBySubMap.isEmpty()) {
					 
					 for (Entry<String, Map<String, Long>> entry2 : allocBySubMap.entrySet()) {
						 
							 Map<String, Long> internalMap = entry2.getValue();
							 //System.out.println("internal = " + internalMap);
							 
							 for (Entry<String, Long> entry3 : internalMap.entrySet()) {
								 
								 if(entry3.getKey().equals(AppConstant.STS_APPROVED)) {
									 approveCount += entry3.getValue();
								 }else if(entry3.getKey().equals(AppConstant.STS_REJECTED)) {
									 rejectCount += entry3.getValue();
								 }else if(entry3.getKey().equals(AppConstant.STS_ALLOC)) {
									 pendingCount += entry3.getValue();
							}
								 
						}
							
		        	}
						 
				}
		    
				pendingAlloc =  uniqueQuestion - (approveCount+rejectCount+pendingCount);
				
				log.info("uniqueQuestion :{}", uniqueQuestion);
				log.info("approveCount :{}", approveCount);
				log.info("rejectCount :{}", rejectCount);
				log.info("pendingCount :{}", pendingCount);
				log.info("pendingAlloc :{}", pendingAlloc);
		 
				
			     resp.setApproved(approveCount);
			     resp.setRejected(rejectCount);
			     resp.setPendingApproval(pendingCount);
			     resp.setPendingAllocation(pendingAlloc);
			     
			     respDTO.setDashBoardTiles(resp);
			     
			     log.info("respDTO :{}",respDTO);
		        
			     if(Objects.isNull(respDTO.dashBoardTiles))
			     {	
			    	respDTO.setStatuscode(AppConstant.STS_CD_002);
			    	respDTO.setMessage(AppConstant.STS_MSG_002);
			     }else {
			    
			    	respDTO.setStatuscode(AppConstant.STS_CD_000);
			    	respDTO.setMessage(AppConstant.STS_MSG_000);
			     }
		    
			    Mono<DashboardDTO> response = Mono.just(respDTO) ;
				
			return response;
		}
		
		
		private TotalObjectionResponseContent getTotalObjectionsCount(String subject) {
			
			Mono<TotalObjectionDTO> totalObjQstnResp =  this.totalObjection.getobjectionQuestion(subject);
			 List<TotalObjectionDTO> totalObjQstnRespList = totalObjQstnResp.flux().collectList().block();
				
			 TotalObjectionResponseContent totRespContList =
					 totalObjQstnRespList.stream().findFirst().get().getResponseContent();
			 
			 System.out.println("totRespContList:"+totRespContList);
			 
		 
			
			return totRespContList;
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
