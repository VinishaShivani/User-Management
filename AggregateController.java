package com.nseit.trb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nseit.trb.client.constant.AppConstant;
import com.nseit.trb.dto.AggregateDTO;
import com.nseit.trb.dto.DashboardDTO;
import com.nseit.trb.dto.ReportsDTO;
import com.nseit.trb.dto.ReportsRequestDTO;
import com.nseit.trb.dto.RequestDTO;
import com.nseit.trb.service.AggregatorService;
import com.nseit.trb.service.DashBoardService;
import com.nseit.trb.service.ReportService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/aggregate")
public class AggregateController {

    @Autowired
    private AggregatorService service;
    
    @Autowired
    private DashBoardService dashBoardService;

    @Autowired
    private ReportService reportService;

    
    @PostMapping(path = "/allocation")
	public Mono<ResponseEntity<AggregateDTO>> getObjQstnWithAllocation(@RequestBody RequestDTO requestPayload) {

    	log.info("Reached getObjQstnnWithAllocation::Payload{}",requestPayload);
    	return this.service.getObjQuestionQuestions(requestPayload, AppConstant.ALLOCATION)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
	}
    
    
    @PostMapping(path = "/approval")
	public Mono<ResponseEntity<AggregateDTO>> getObjQstnForApproval(@RequestBody RequestDTO requestPayload) {

    	log.info("Reached getObjQstnForApproval::Payload{}",requestPayload);
    	return this.service.getObjQuestionQuestions(requestPayload, AppConstant.APRROVAL)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
	}
    
    
    @PostMapping(path = "/dashboard/subject")
   	public Mono<ResponseEntity<DashboardDTO>> getDashboardBySubject() {

       	log.info("Reached getReportsForDashboard::Payload{}");
       	return this.dashBoardService.getDashboardBySubject()
                   .map(ResponseEntity::ok)
                   .defaultIfEmpty(ResponseEntity.notFound().build());
   	}
    
    
    @PostMapping(path = "/dashboard/sme")
   	public Mono<ResponseEntity<DashboardDTO>> getDashboardBySme(@RequestParam(required = false) String smeId) {

       	log.info("Reached getDashboardBySme::Payload{}",smeId);
       	return this.dashBoardService.getDashboardBySme(smeId)
                   .map(ResponseEntity::ok)
                   .defaultIfEmpty(ResponseEntity.notFound().build());
   	}
    
    
    @PostMapping(path = "/dashboard/tiles")
   	public Mono<ResponseEntity<DashboardDTO>> getDashboardTilesData() {

       	log.info("Reached getDashboardTilesData::Payload{}");
       	return this.dashBoardService.getDashboardTilesData()
                   .map(ResponseEntity::ok)
                   .defaultIfEmpty(ResponseEntity.notFound().build());
   	}

    
    @PostMapping(path = "/reports/sme")
   	public Mono<ResponseEntity<ReportsDTO>> getSmeDailyReports(@RequestBody ReportsRequestDTO requestPayload) {

       	log.info("Reached getSmeDailyReports::Payload {}",requestPayload);
       	return this.reportService.getSmeDailyReports(requestPayload)
                   .map(ResponseEntity::ok)
                   .defaultIfEmpty(ResponseEntity.notFound().build());
   	}

    
    @PostMapping(path = "/reports/objsummary")
   	public Mono<ResponseEntity<ReportsDTO>> getObjSummaryReports(@RequestBody ReportsRequestDTO requestPayload) {

       	log.info("Reached getObjSummaryReports::Payload {}",requestPayload);
       	return this.reportService.getObjSummaryReports(requestPayload)
                   .map(ResponseEntity::ok)
                   .defaultIfEmpty(ResponseEntity.notFound().build());
   	}
    
    @PostMapping(path = "/reports/finalsummary")
   	public Mono<ResponseEntity<ReportsDTO>> getFinalObjSummaryReports(@RequestBody ReportsRequestDTO requestPayload) {

       	log.info("Reached getFinalObjSummaryReports::Payload {}",requestPayload);
       	return this.reportService.getFinalObjSummaryReports(requestPayload)
                   .map(ResponseEntity::ok)
                   .defaultIfEmpty(ResponseEntity.notFound().build());
   	}
    
    @PostMapping(path = "/reports/finalObjDtl")
   	public Mono<ResponseEntity<ReportsDTO>> getFinalObjDtlReports(@RequestBody ReportsRequestDTO requestPayload) {

       	log.info("Reached getFinalObjDtlReports::Payload {}",requestPayload);
       	return this.reportService.getFinalObjDtlReports(requestPayload)
                   .map(ResponseEntity::ok)
                   .defaultIfEmpty(ResponseEntity.notFound().build());
   	}
    
}