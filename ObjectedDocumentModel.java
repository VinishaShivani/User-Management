package com.trb.allocationservice.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Entity
@Table(name = "obj_doc", schema = "thrdexams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectedDocumentModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "obj_doc_seq")
	@SequenceGenerator(name = "obj_doc_seq", sequenceName = "obj_doc_seq", allocationSize = 10, schema = "thrdexams")
	@Column(name = "obj_doc_pk")
	private Long objectionDocumentPk;
	
	@Column(name = "objrmk_pk")
	private Long objectionRemarksPk;
	
	@Column(name = "doc_path")
	private String documentPath;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "objrmk_pk", referencedColumnName = "objrmk_pk",insertable = false,updatable = false,nullable=false)
    private ObjectedQuestionRemarksModel entityRemarks;
}
