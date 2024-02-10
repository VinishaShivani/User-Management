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
public class ObjectionByQstnIdClient {

	@Autowired
    private WebClient client;
		 
	@Value("${spring.objectionByQstnIdClient.url}")
	public String objectionByQstnIdClientURL;
		    	    
	@Value("${spring.webclient.timeout}")
   	public Integer timeout;
		    		    
	public ObjectionByQstnIdClient(WebClient.Builder builder) {
		      
		this.client = builder.baseUrl(objectionByQstnIdClientURL).build();    	
	}

    public Mono<ObjectionDTO> getobjectionQuestion(String questionList){
    	
    	log.info("Reached ObjectionClient::{}",questionList);
    	
        return this.client.get()
        		.uri(objectionByQstnIdClientURL)
                .header("listOfobjquesId", questionList)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    log.error("Error while calling endpoint {} with status code {}",
                    		objectionByQstnIdClientURL.toString(), clientResponse.statusCode());
                    throw new RuntimeException("Error while calling endpoint"); })
                .bodyToMono(ObjectionDTO.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnError(error -> log.error("Error signal detected", error));
                //.onErrorResume(ex -> Mono.empty()); // if no result, it returns 404, so switch to no promotion
    }
		    
		
}
