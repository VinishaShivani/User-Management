package com.nseit.trb.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nseit.trb.dto.ReportsObjSummary;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ObjectionSummaryReportClient {
	
	
	@Autowired
    private WebClient client;
    
    
    @Value("${spring.objectionSummaryReportClient.url}")
   	public String objectionSummaryReportClientURL;

    @Value("${spring.webclient.timeout}")
   	public Integer timeout;
    
    public ObjectionSummaryReportClient(WebClient.Builder builder) {
        
    	this.client = builder.baseUrl(objectionSummaryReportClientURL).build();
    }

    public Mono<ReportsObjSummary> getObjSummaryRpt(){
    	
    	log.info("Reached getObjSummaryRpt::{}");
        return this.client.get()
        		.uri(objectionSummaryReportClientURL)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    log.error("Error while calling endpoint {} with status code {}",
                    		objectionSummaryReportClientURL.toString(), clientResponse.statusCode());
                    throw new RuntimeException("Error while calling  accounts endpoint"); })
                .bodyToMono(ReportsObjSummary.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnError(error -> log.error("Error signal detected", error));
                //.onErrorResume(ex -> Mono.empty()); // if no result, it returns 404, so switch to no data
    }


	

}
