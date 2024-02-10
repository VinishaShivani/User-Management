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
public class AllocationClient {

	@Autowired
    private WebClient client;
    
    
    @Value("${spring.allocationClient.url}")
	public String allocationClientURL;
    
    @Value("${spring.webclient.timeout}")
   	public Integer timeout;
    

    public AllocationClient(WebClient.Builder builder) {
        
    	this.client = builder.baseUrl(allocationClientURL).build();
    }

    public Mono<AllocationDTO> getAllocationById(String smeId){

    	log.info("Reached getAllocationById::{}",smeId);
        return this.client
                .get()
                .uri(allocationClientURL)
                .header("userId", smeId)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    log.error("Error while calling endpoint {} with status code {}",
                    		allocationClientURL.toString(), clientResponse.statusCode());
                    throw new RuntimeException("Error while calling  accounts endpoint"); })
                .bodyToMono(AllocationDTO.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnError(error -> log.error("Error signal detected", error));
                //.onErrorResume(ex -> Mono.empty()); // switch it to empty in case of error
    }
    
    public Mono<AllocationDTO> getAllocationBySubject(String subject){

    	log.info("Reached getAllocationBySubject::{}",subject);
        return this.client
                .get()
                .uri(allocationClientURL)
                .header("subject", subject)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    log.error("Error while calling endpoint {} with status code {}",
                    		allocationClientURL.toString(), clientResponse.statusCode());
                    throw new RuntimeException("Error while calling  accounts endpoint"); })
                .bodyToMono(AllocationDTO.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnError(error -> log.error("Error signal detected", error));
                //.onErrorResume(ex -> Mono.empty()); // switch it to empty in case of error
    }
    
    public Mono<AllocationDTO> getAllocationByStatus(String status){

    	log.info("Reached getAllocationBySubject::{}",status);
        return this.client
                .get()
                .uri(allocationClientURL)
                .header("status", status)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    log.error("Error while calling endpoint {} with status code {}",
                    		allocationClientURL.toString(), clientResponse.statusCode());
                    throw new RuntimeException("Error while calling  accounts endpoint"); })
                .bodyToMono(AllocationDTO.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnError(error -> log.error("Error signal detected", error));
                //.onErrorResume(ex -> Mono.empty()); // switch it to empty in case of error
    }

}
