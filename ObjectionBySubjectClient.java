package com.nseit.trb.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nseit.trb.dto.ObjectionDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ObjectionBySubjectClient {

	@Autowired
    private WebClient client;
	    
	    @Value("${spring.objectionBySubjectClient.url}")
		public String objectionBySubjectClientURL;
	    
	    @Value("${spring.webclient.timeout}")
	   	public Integer timeout;

	    
	    public ObjectionBySubjectClient(WebClient.Builder builder) {
	        
	    	this.client = builder.baseUrl(objectionBySubjectClientURL).build();
	    }

	    public Mono<ObjectionDTO> getObjQuestionBySubjet(String subject){
	    	
	    	log.info("Reached getObjQuestionBySubjet::{}",subject);
	        return this.client.get()
	        		.uri(objectionBySubjectClientURL)
	                .header("Subject", subject) 
	                .retrieve()
	                .onStatus(HttpStatus::isError, clientResponse -> {
	                    log.error("Error while calling endpoint {} with status code {}",
	                    		objectionBySubjectClientURL.toString(), clientResponse.statusCode());
	                    throw new RuntimeException("Error while calling endpoint"); })
	                .bodyToMono(ObjectionDTO.class)
	                .timeout(Duration.ofMillis(timeout))
	                .doOnError(error -> log.error("Error signal detected", error));
	                //.onErrorResume(ex -> Mono.empty()); // if no result, it returns 404, so switch to no data
	    }
	
	
}
