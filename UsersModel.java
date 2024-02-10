package com.trb.allocationservice.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "thrdexams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersModel implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1102822883697388682L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
	@SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 50, schema = "thrdexams")
	@Column(name = "user_pk")
	private Long userPk;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "password")
	private String password;

	@Column(name = "identity_num")
	private String identityNumber;

	@Column(name = "firstname")
	private String firstName;
	
	@Column(name = "lastname")
	private String lastName;
	
	@Column(name = "subject")
	private String subject;
	
	@Column(name = "expirydate")
	private Date expiryDate;
	
	@Column(name = "institute")
	private String institute;
	
	@Column(name = "crte_by", updatable = false)
	private String userCreateBy;
	
	@Column(name = "updt_by", updatable = false)
	private String userUpdateBy;

	@Column(name = "crte_dt", updatable = false)
	@CreationTimestamp
	private Timestamp userCrteDate;

	@Column(name = "updt_dt", updatable = false)
	@CreationTimestamp
	private Timestamp userUpdtDt;
	
//	@JsonManagedReference
//	@OneToMany(mappedBy="users")
//    private List<ObjectedQuestionsAllocationModel> allocations;
}
