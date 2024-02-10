package com.nseit.trb.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nseit.trb.client.UserDetailClient;
import com.nseit.trb.dto.UserDetailsDTO;
import com.nseit.trb.dto.UserDtlResponseContent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class UserDetails {

	private final UserDetailClient userDetailClient;
	
	  
	
	public String getUserIdBySmeId(String smeId)  {
	
			String smeUserId = "";
		
			Mono<UserDetailsDTO> userResp = this.userDetailClient.getUserIdBySmeId(smeId);
			
			List<UserDetailsDTO> userList = userResp.flux().collectList().block();
			userList.forEach(System.out::println);
	  	
			if(!userList.isEmpty()) {
				
				System.out.println("inside userList:");
				List<UserDtlResponseContent> responseList =
						userList.stream().findFirst().get().getResponseContent();

				
				if(responseList!=null) {
					
					responseList.forEach(System.out::println);
					for(UserDtlResponseContent obj: responseList) {
						
						smeUserId = obj.getUserId();
						System.out.println("smeUserId:"+smeUserId);
					}
				}
				
			}
			
			
		return smeUserId;
	}



	public Map<String, Object> getValidityDays(String smeId) {
		// TODO Auto-generated method stub
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		Mono<UserDetailsDTO> userResp = this.userDetailClient.getUserIdBySmeId(smeId);
		
		List<UserDetailsDTO> userList = userResp.flux().collectList().block();
		
		userList.forEach(System.out::println);
		
		for(UserDetailsDTO obj : userList) {
			if(obj.getResponseContent()==null)
				return map;
		}
		
  	
		if(!userList.isEmpty()) {
			
			//System.out.println("inside userList:");
			List<UserDtlResponseContent> responseList =
					userList.stream().findFirst().get().getResponseContent();
			//responseList.forEach(System.out::println);
			
			if(!responseList.equals("")) {
			
				//System.out.println("inside responseList:");
				for(UserDtlResponseContent obj: responseList) {
					
					String userId = obj.getUserId();
					int expDays = obj.getValdityInDays();
					
					map.put("userId",userId);
					map.put("expDays",expDays);
					
					System.out.println("map:"+map);
				}
				
			}
			
		}
		
		return map;
	}
	
	
	public Map<String, UserDtlResponseContent> getUsersBySubject(String subject) {
		// TODO Auto-generated method stub
		
		Map<String, UserDtlResponseContent>  UserDtlMap = new HashMap<>();
		
		Mono<UserDetailsDTO> userResp = this.userDetailClient.getUsersBySubject(subject);
		
//		Mono<UserDetailsDTO> userResp = getUsersDump();
		
		List<UserDetailsDTO> userList = userResp.flux().collectList().block();
		
		userList.forEach(System.out::println);
		
		for(UserDetailsDTO obj : userList) {
			if(obj.getResponseContent()==null)
				return UserDtlMap;
		}
		
  	
		if(!userList.isEmpty()) {
			
			//System.out.println("inside userList:");
			List<UserDtlResponseContent> responseList =
					userList.stream().findFirst().get().getResponseContent();
			//responseList.forEach(System.out::println);
			
			UserDtlMap = responseList.stream()
			.collect(Collectors.toMap(UserDtlResponseContent::getUserId, Function.identity()));
			
			System.out.println("UserDtlMap:"+UserDtlMap);
			
		}
		
		return UserDtlMap;
	}
	
	
	 public Mono<UserDetailsDTO> getUsersDump() {
	    	
	    	UserDetailsDTO dto = new UserDetailsDTO();
	    	List<UserDtlResponseContent> responseContent = new ArrayList<>();
	    	UserDtlResponseContent respDTO = new UserDtlResponseContent(); 
	    
	    	
	    	dto.setStatusCode("SUC-001");
	    	dto.setMessage("User Details based on Identity Number");
	    	
	    	respDTO.setUserId("1629");
	    	respDTO.setUserId("1808930Senthil");
	    	respDTO.setPassword(null);
	    	
	    	respDTO.setPassword("YBakr(2+4b"); 
	    	respDTO.setIdentityNumber("1808930"); 
	    	respDTO.setFirstName("Senthil"); 
	    	respDTO.setSubject("Child Development and Pedagogy"); 
	    	respDTO.setExpiryDate("2022-09-30T00:00:00.000+00:00"); 
	    	respDTO.setUserCrteDate("2022-09-19T09:20:52.994+00:00"); 
	    	respDTO.setUserUpdtDt("2022-09-19T09:20:52.994+00:00");
	    	respDTO.setValdityInDays(10);
	    	
	    	responseContent.add(respDTO);
	    	dto.setResponseContent(responseContent);
	    	
	    	 Mono<UserDetailsDTO> response = Mono.just(dto)  ;
	    	
			return response;
	    	
	    	
	    }
	
	
}
