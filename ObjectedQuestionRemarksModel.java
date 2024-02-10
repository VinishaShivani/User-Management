package com.trb.allocationservice.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(name = "obj_remarks", schema = "thrdexams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectedQuestionRemarksModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "obj_remarks_seq")
	@SequenceGenerator(name = "obj_remarks_seq", sequenceName = "obj_remarks_seq", allocationSize = 10, schema = "thrdexams")
	@Column(name = "objrmk_pk")
	private Long objectionRemarksPk;
	
	@Column(name = "objalloc_pk")
	private Long objallocpk;
	
	@Column(name = "subject")
	private String subject;
	
	@Column(name = "standard")
	private String standard;
	
	@Column(name = "page_num")
	private String pageNumber;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name="reference_url")
	private String referenceUrl;
	
	@Column(name="author")
	private String author;
	
	@Column(name = "crte_by", updatable = false)
	private String userCreateBy;
	
	@Column(name = "updt_by", updatable = false)
	private String userUpdateBy;

	@Column(name = "crte_dt", updatable = false)
	@CreationTimestamp
	private Timestamp userCrteDate;

	@Column(name = "updt_dt")
	@CreationTimestamp
	private Timestamp userUpdtDt;
	
	//@OneToOne
    //private ObjectedQuestionsAllocationModel objectedQuestions;
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy="entityRemarks")
    private List<ObjectedDocumentModel> documents;

}
