package com.nseit.trb.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nseit.trb.dto.ReportsFinalObjDetail;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FinalObjDtlReportClient {
	
	
	@Autowired
    private WebClient client;
 
	
    @Value("${spring.finalObjDtlReportClient.url}")
   	public String finalObjDtlReportClientURL;
    
    @Value("${spring.webclient.timeout}")
   	public Integer timeout;

    
    public FinalObjDtlReportClient(WebClient.Builder builder) {
        
    	this.client = builder.baseUrl(finalObjDtlReportClientURL).build();
    }

    public Mono<ReportsFinalObjDetail> getFinalObjDtlRpt(){
    	
    	log.info("Reached getFinalObjDtlRpt::{}");
        return this.client.get()
        		.uri(finalObjDtlReportClientURL)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    log.error("Error while calling endpoint {} with status code {}",
                    		finalObjDtlReportClientURL.toString(), clientResponse.statusCode());
                    throw new RuntimeException("Error while calling endpoint"); })
                .bodyToMono(ReportsFinalObjDetail.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnError(error -> log.error("Error signal detected", error));
                //.onErrorResume(ex -> Mono.empty()); // if no result, it returns 404, so switch to no data
    }


	

}
