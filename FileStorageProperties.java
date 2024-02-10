package com.trb.allocationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {
	
	    private String uploadDir;
}
