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

import lombok.Data;

@Data
@Entity
@Table(name = "objques_modans", schema = "thrdexams")
public class ObjectionQuesModAns {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "objques_modans_seq")
	@SequenceGenerator(name = "objques_modans_seq", sequenceName = "objques_modans_seq", allocationSize = 1, schema = "thrdexams")
	@Column(name="objques_modans_id")
	private Integer objquesModAnsId;
	
	@Column(name="objques_pk")
	private Integer objquesId;
	
	@Column(name="qst_mod_ans_id")
	private Integer modifyoptionId;
	
	@Column(name="qst_mod_ans_val") 
	private String modifyDescription;
	
	@Column(name = "crte_dt", updatable = false)
	@CreationTimestamp
	private Timestamp crteDate;
	
	@Column(name = "updt_dt", updatable = false)
	@CreationTimestamp
	private Timestamp updtDt;
	
	@Column(name="objalloc_pk")
	private Integer objallocPK;
	
}
