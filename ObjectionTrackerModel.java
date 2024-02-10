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
@Table(name = "obj_tracker", schema = "thrdexams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectionTrackerModel {
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "obj_tracker_seq")
	@SequenceGenerator(name = "obj_tracker_seq", sequenceName = "obj_tracker_seq", allocationSize = 1, schema = "thrdexams")
	
	@Column(name = "id")
	private Long id;
	
	@Column(name = "objques_pk")
	private Integer objquesPk;
	
	@Column(name = "subject_id")
	private Integer subjectId;
    
	@Column(name = "level_id")
	private Integer levelId;
	
	@Column(name = "status_id")
	private int statusId;
	
	@Column(name = "currrent")
	private String currrent;
	
	@Column(name = "crte_dt", updatable = false)
	@CreationTimestamp
	private Timestamp userCrteDate;

	@Column(name = "updt_dt")
	@UpdateTimestamp
	private Timestamp userUpdtDt;


}
