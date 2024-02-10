package com.trb.allocationservice.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.tomcat.jni.Address;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Entity
@Table(name = "obj_alloc", schema = "thrdexams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectedQuestionsAllocationModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -442322227275302976L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "obj_allocation_seq")
	@SequenceGenerator(name = "obj_allocation_seq", sequenceName = "obj_allocation_seq", allocationSize = 1, schema = "thrdexams")
	@Column(name = "objalloc_pk")
	private Long objectionAllocationPk;
	
	@Column(name = "user_pk")
	private Long userPk;
	
//	@Column(name = "objques_pk")
//	private Long objectedQuestionsPk;
	
	@Column(name = "status_id")
	private Long statusId;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "summary_id")
	private Integer summaryId;
	
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
	
//	@JsonBackReference
//	@ManyToOne
//    @JoinColumn(name="status_id", nullable=false)
//    private StatusModel status;
	
	//@OneToOne(mappedBy = "objectedQuestions")
    //private ObjectedQuestionRemarksModel allocationRemarks;
	
//	@JsonBackReference
//	@ManyToOne
//    @JoinColumn(name="user_pk", nullable=false)
//    private UsersModel users;
	
//	@JsonBackReference
//	@OneToOne
//	@JoinColumn(name = "objques_pk", referencedColumnName = "objques_pk")
//	private ObjectedQuestionsModel objQuestions;
}
