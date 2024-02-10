package com.nseit.trb.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nseit.trb.dto.TotalObjectionDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class TotalObjectionClient {
	
	
	
	@Autowired
    private WebClient client;
    
    
	@Value("${spring.totalObjectionClient.url}")
   	public String totalObjectionClientURL;
	
	@Value("${spring.webclient.timeout}")
   	public Integer timeout;
    
    public TotalObjectionClient(WebClient.Builder builder) {
      
    	this.client = builder.baseUrl(totalObjectionClientURL).build();
    }

    public Mono<TotalObjectionDTO> getobjectionQuestion(String subject){
    	
    	log.info("Reached ObjectionClient::{}",subject);
        return this.client.get()
        		.uri(totalObjectionClientURL)
                .header("Subject", subject) 
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    log.error("Error while calling endpoint {} with status code {}",
                    		totalObjectionClientURL.toString(), clientResponse.statusCode());
                    throw new RuntimeException("Error while calling  accounts endpoint"); })
                .bodyToMono(TotalObjectionDTO.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnError(error -> log.error("Error signal detected", error));
                //.onErrorResume(ex -> Mono.empty()); // if no result, it returns 404, so switch to no promotion
    }
    

}
