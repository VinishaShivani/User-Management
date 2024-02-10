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
public class ObjectionClient {

	@Autowired
    private WebClient client;
    
    private final ObjectionDTO noData = new ObjectionDTO("no data");
    
    @Value("${spring.objectionClient.url}")
	public String objectionClientURL;

    @Value("${spring.webclient.timeout}")
   	public Integer timeout;
    
    public ObjectionClient(WebClient.Builder builder) {
      
    	this.client = builder.baseUrl(objectionClientURL).build();
    }

    public Mono<ObjectionDTO> getobjectionQuestion(){
    	
    	log.info("Reached ObjectionClient::{}",objectionClientURL);
        return this.client.get()
        		.uri(objectionClientURL)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    log.error("Error while calling endpoint {} with status code {}",
                    		objectionClientURL.toString(), clientResponse.statusCode());
                    throw new RuntimeException("Error while calling  accounts endpoint"); })
                .bodyToMono(ObjectionDTO.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnError(error -> log.error("Error signal detected", error));
                //.delaySubscription(Duration.ofMinutes(3))              
                //.onErrorResume(ex -> Mono.error(ex)); // if no result, it returns 404, so switch to no promotion
    }
    
  
}
