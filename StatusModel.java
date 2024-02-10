package com.nseit.subjectmasterservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "status", schema = "thrdexams")
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

}
