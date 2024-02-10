package com.trb.allocationservice.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "levels", schema = "thrdexams")
public class LevelsModel {
	
	 
		private static final long serialVersionUID = -4406495839953339531L;
		
		
		@Id
		private Long lvlId;
		private String lvlCode;
		private String description;


}
