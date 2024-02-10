package com.trb.allocationservice.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Entity
@Table(name = "obj_alloc_summary", schema = "thrdexams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectionSummaryModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "obj_alloc_summary_seq")
	@SequenceGenerator(name = "obj_alloc_summary_seq", sequenceName = "obj_alloc_summary_seq", allocationSize = 1, schema = "thrdexams")
	
	@Column(name = "id")
	private Integer summaryId;
	
	@Column(name = "objques_pk")
	private Integer objquesId;
	
	@Column(name = "subject_id")
	private Integer subjectId;
    
	@Column(name = "level_id")
	private Integer levelId;
	
	@Column(name = "currrent")
	private String currrent;
	
	@Column(name = "status_id")
	private Integer statusId;
	
	@Column(name = "crte_dt", updatable = false)
	@CreationTimestamp
	private Timestamp userCrteDate;

	@Column(name = "updt_dt")
	@UpdateTimestamp
	private Timestamp userUpdtDt;

}
