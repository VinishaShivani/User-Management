package com.trb.allocationservice.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(name = "status", schema = "thrdexams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusModel {

	@Id
	@Column(name = "status_id")
	private Long statusPk;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "domain")
	private String domain;
	
	@Column(name = "description")
	private String codeDescription;
	
//	@JsonManagedReference
//	@OneToMany(mappedBy="status")
//    private List<ObjectedQuestionsAllocationModel> allocations;
	
}
