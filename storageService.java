package com.trb.allocationservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface storageService {
	
	  public String save(MultipartFile file,String filePath);
	  
	  public String deleteFolder(String filePath);
}
