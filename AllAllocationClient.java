package com.nseit.trb.client;



import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nseit.trb.dto.AllocationDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class AllAllocationClient {
	
	@Autowired
    private WebClient client;
    
    @Value("${spring.allAllocationClient.url}")
	public String allAllocationClientURL;
    
    @Value("${spring.webclient.timeout}")
   	public Integer timeout;
    

    public AllAllocationClient(WebClient.Builder builder) {
       
    	this.client = builder.baseUrl(allAllocationClientURL).build();
    }

    public Mono<AllocationDTO> getAllAllocation(){

    	log.info("Reached getAllAllocation::{}");
        return this.client
                .get()
                .uri(allAllocationClientURL)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    log.error("Error while calling endpoint {} with status code {}",
                    		allAllocationClientURL.toString(), clientResponse.statusCode());
                    throw new RuntimeException("Error while calling  accounts endpoint"); })
                .bodyToMono(AllocationDTO.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnError(error -> log.error("Error signal detected", error));
                //.onErrorResume(ex -> Mono.empty()); // switch it to empty in case of error
    }	
	


}
