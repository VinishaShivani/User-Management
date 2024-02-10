package com.trb.allocationservice.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface downloadService {

	ResponseEntity<Resource> downloadFile(HttpServletRequest request, String filePath) throws IOException;
}
