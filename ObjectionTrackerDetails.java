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
@Table(name = "obj_tracker_details", schema = "thrdexams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectionTrackerDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "obj_tracker_details_seq")
	@SequenceGenerator(name = "obj_tracker_details_seq", sequenceName = "obj_tracker_details_seq", allocationSize = 1, schema = "thrdexams")
	
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "obj_tracker_id")
	private Integer objTrackerId;
	
	@Column(name = "user_pk")
	private Long userPk;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "mode")
	private String mode;
	
	@Column(name = "crte_by", updatable = false)
	private String userCreateBy;
	
	@Column(name = "updt_by")
	private String userUpdateBy;
	
	@Column(name = "crte_dt", updatable = false)
	@CreationTimestamp
	private Timestamp userCrteDate;

	@Column(name = "updt_dt")
	@UpdateTimestamp
	private Timestamp userUpdtDt;

}
