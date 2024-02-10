package com.nseit.trb.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nseit.trb.dto.UserDetailsDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserDetailClient {
	
	
	@Autowired
    private WebClient client;
	    
	    
	    @Value("${spring.userDetailClient.url}")
	   	public String userDetailClientURL;
	    
	    @Value("${spring.admin.user}")
	   	public String adminUser;
	    
	    @Value("${spring.admin.pwd}")
	   	public String adminPass;
	    
	    @Value("${spring.webclient.timeout}")
	   	public Integer timeout;

	    
	    public UserDetailClient(WebClient.Builder builder) {
	        
	    	this.client = builder.baseUrl(userDetailClientURL).build();
	    }

	    public Mono<UserDetailsDTO> getUserIdBySmeId(String smeId){
	    	
	    	log.info("Reached getUserIdBySmeId::{}",smeId);
	        return this.client.get()
	        		.uri(userDetailClientURL)
	                .header("identityNumber", smeId) 
	                .headers(headers -> headers.setBasicAuth(adminUser, adminPass))
	                .retrieve()
	                .onStatus(HttpStatus::isError, clientResponse -> {
	                    log.error("Error while calling endpoint {} with status code {}",
	                    		userDetailClientURL.toString(), clientResponse.statusCode());
	                    throw new RuntimeException("Error while calling  accounts endpoint"); })
	                .bodyToMono(UserDetailsDTO.class)
	                .timeout(Duration.ofMillis(timeout))
	                .doOnError(error -> log.error("Error signal detected", error));
	                //.onErrorResume(ex -> Mono.empty()); // if no result, it returns 404, so switch to no data
	        
	    }
	    
	    public Mono<UserDetailsDTO> getUsersBySubject(String subject){
	    	
	    	log.info("Reached getUsersBySubject::{}",subject);
	        return this.client.get()
	        		.uri(userDetailClientURL)
	                .header("subject", subject) 
	                .headers(headers -> headers.setBasicAuth(adminUser, adminPass))
	                .retrieve()
	                .onStatus(HttpStatus::isError, clientResponse -> {
	                    log.error("Error while calling endpoint {} with status code {}",
	                    		userDetailClientURL.toString(), clientResponse.statusCode());
	                    throw new RuntimeException("Error while calling  accounts endpoint"); })
	                .bodyToMono(UserDetailsDTO.class)
	                .timeout(Duration.ofMillis(timeout))
	                .doOnError(error -> log.error("Error signal detected", error));
	                //.onErrorResume(ex -> Mono.empty()); // if no result, it returns 404, so switch to no data
	        
	    }
	    
	   

}
