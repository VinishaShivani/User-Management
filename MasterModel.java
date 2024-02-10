package com.nseit.subjectmasterservice.entity;

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

import lombok.Data;

@Data
@Entity
@Table(name = "ref_master", schema = "thrdexams")
public class MasterModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ref_master_seq")
	@SequenceGenerator(name = "ref_master_seq", sequenceName = "ref_master_seq", allocationSize = 1, schema = "thrdexams")
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
//	@Column(name = "description")
//	private String description;
	
	@Column(name = "status")
	private int status;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "crte_dt", updatable = false)
	@CreationTimestamp
	private Timestamp questionCrteDate;
	
	@Column(name = "updt_dt", updatable = false)
	@CreationTimestamp
	private Timestamp questionUpdtDt;
		
}
