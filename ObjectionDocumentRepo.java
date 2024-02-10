package com.trb.allocationservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trb.allocationservice.entity.ObjectedDocumentModel;

public interface ObjectionDocumentRepo extends JpaRepository<ObjectedDocumentModel, Long> {
	
	List<ObjectedDocumentModel> findObjectionDocumentPkByObjectionRemarksPk(Long objremarksPK);
	
	List<ObjectedDocumentModel> findObjectionDocumentPkByObjectionRemarksPkAndDocumentPath(Long objremarksPK, String filePath);
	
}
