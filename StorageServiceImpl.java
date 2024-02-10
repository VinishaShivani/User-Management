package com.trb.allocationservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.trb.allocationservice.service.storageService;
import com.trb.allocationservice.util.FileStorage;

@Service
public class StorageServiceImpl implements storageService {
	  
	  @Autowired
	  FileStorage files;
	  
	  @Override
	  public String save(MultipartFile file,String filePath) {
		  String fileName = "";
		  try {
	    //  Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
	     fileName = files.storeFile(file, filePath);
	    } catch (Exception e) {
	    	e.getStackTrace();
	      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
	    }
		return fileName;
	  }
	  
	  @Override
	  public String deleteFolder(String filePath) {
		  String fileName = "";
		try {
	     fileName = files.deleteFilesAndDirectory(filePath);
	    } catch (Exception e) {
	    	e.getStackTrace();
	      throw new RuntimeException("Could not delete the folder. Error: " + e.getMessage());
	    }
		return fileName;
	  }
	
}
