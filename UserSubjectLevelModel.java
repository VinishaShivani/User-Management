package com.nseit.subjectmasterservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table(name = "usr_subj_lvl_map", schema = "thrdexams")
public class UserSubjectLevelModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8966265815689406261L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_id")
	private Long pkId;

	@Column(name = "user_pk")
	private Long userPk;
		
	@Column(name = "ref_id")
	private Long refId;
	
	@Column(name = "lvl_id")
	private Long lvlId;

}
