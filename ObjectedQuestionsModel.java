package com.trb.allocationservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(name = "objques", schema = "thrdexams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectedQuestionsModel {

	@Id
	@Column(name = "objques_pk")
	private Long objectedQuestionsPk;
	
	@Column(name = "qst_pk")
	private Integer questionId;
	
	@Column(name = "qst_mdm_id")
	private Integer questionMdmId;
	
	@Column(name = "qst_mdm_name")
	private String questionMdmName;
	
	@Column(name = "qst_qb_id")
	private Integer questionBankId;
	
	@Column(name = "qst_crct_ans_id")
	private Integer questionCrctAnsId;
	
	@Column(name = "subject")
	private String subject;
//	
//	@JsonManagedReference
//	@OneToOne(mappedBy = "objQuestions")
//   private ObjectedQuestionsAllocationModel allocations;
	
}
