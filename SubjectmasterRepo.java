package com.nseit.subjectmasterservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nseit.subjectmasterservice.entity.MasterModel;

@Repository
public interface SubjectmasterRepo extends JpaRepository<MasterModel, Integer> {

	MasterModel findByNameOrderById(String name);
	
	List<MasterModel> findAllBynameAndStatusOrderById(String name, Integer status) ;
				
	List<MasterModel> findAllBynameOrderById(String name);
	
	List<MasterModel> findAllBystatusOrderById(Integer status);
	



}
