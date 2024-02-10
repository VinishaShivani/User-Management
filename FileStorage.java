package com.trb.allocationservice.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.trb.allocationservice.service.downloadService;

@Service
public class FileStorage implements downloadService{
	
	@Value("${ftp_server}")
	String ftpServer;

	@Value("${ftp_port}")
	int ftpPort;

	@Value("${ftp_username}")
	String ftpUsername;
	
	@Value("${ftp_password}")
	String ftpPassword;

	@Value("${file.upload-dir}")
	String ftpUploadDir;
	
	@Value("${local_download_path}")
	String localDownloadPath;

    public String storeFile(MultipartFile file,String targetpath) {
    	
    		FTPClient ftpClient = new FTPClient();
    		String fileName = "";
    		try {
				
    			ftpClient.connect(ftpServer, ftpPort);
				Boolean connection =ftpClient.login(ftpUsername, ftpPassword);
				System.out.println("connected or not :: "+ connection);
				String reply = ftpClient.getReplyString();
				ftpClient.enterLocalPassiveMode();
				ftpClient.setBufferSize(1024 * 1024);
				makeDirectories(ftpClient,targetpath);
				
				String currFileName = file.getOriginalFilename().toString();
				Long fileSizeInKB = file.getSize() / 1024;  //converting bytes to KB
				String parentDir = ftpClient.printWorkingDirectory();
				System.out.println("directory isExist::" + ftpClient.cwd(parentDir)+" ::parentDir:"+parentDir);
				System.out.println("CurrFileName :: "+currFileName + " and Size:"+fileSizeInKB);
				
				//Files already exist in this directory validation
				FTPFile[] subFiles = ftpClient.listFiles(parentDir);
				if(subFiles != null && subFiles.length > 0) {
					
					List<String> fileList = new ArrayList<>();
					for (FTPFile aFile : subFiles) {
						fileList.add(aFile.getName());
					}
					
					System.out.println("File Already Exist:"+fileList.contains(currFileName));					
					if(!fileList.contains(currFileName) && fileSizeInKB > 0) {
						
						ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
						System.out.println("Mk :: "+ftpClient.getReplyString());
						ftpClient.storeFile(Path.of(file.getOriginalFilename()).toString(),file.getInputStream());
						System.out.println("Mk :: "+ftpClient.getReplyString());
						System.out.println("Mk :: "+ftpClient.getReplyCode());
						
						if(ftpClient.getReplyCode()!=226) //226 Transfer complete
						{
							fileName = "FAILED";
						}else {
							System.out.println("Download File :: " + targetpath+file.getOriginalFilename());
							fileName = targetpath+file.getOriginalFilename();
						}
						System.out.println("reply or not :: "+ reply);
						
					}
					else {
						
						fileName = "FILE_EXIST";
					}
				}else {
					
					//No files in this directory - create new file
					ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
					System.out.println("Mk :: "+ftpClient.getReplyString());
					ftpClient.storeFile(Path.of(file.getOriginalFilename()).toString(),file.getInputStream());
					System.out.println("Mk :: "+ftpClient.getReplyString());
					System.out.println("Mk :: "+ftpClient.getReplyCode());
					
					if(ftpClient.getReplyCode()!=226) //226 Transfer complete
					{
						fileName = "FAILED";
					}else {
						System.out.println("Download File :: " + targetpath+file.getOriginalFilename());
						fileName = targetpath+file.getOriginalFilename();
					}
					System.out.println("reply or not :: "+ reply);
				}
				
				System.out.println("result:"+fileName);
				ftpClient.logout();  
		        ftpClient.disconnect();
				
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
    		
            return fileName;
       }
    
    public boolean makeDirectories(FTPClient ftpClient, String dirPath)
            throws IOException {
        String[] pathElements = dirPath.split("/");
        if (pathElements != null && pathElements.length > 0) {
            for (String singleDir : pathElements) {
                boolean existed = ftpClient.changeWorkingDirectory(singleDir);
                if (!existed) {
                    boolean created = ftpClient.makeDirectory(singleDir);
                    if (created) {
                        System.out.println("CREATED directory: " + singleDir);
                        ftpClient.changeWorkingDirectory(singleDir);
                    } else {
                        System.out.println("COULD NOT create directory: " + singleDir);
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request, String filePath) throws IOException {
		
    	FTPClient ftpClient = new FTPClient();
    	boolean connection = false;
			
			try {
				ftpClient.connect(ftpServer, ftpPort);
				connection =ftpClient.login(ftpUsername, ftpPassword);
				ftpClient.enterLocalActiveMode();
				ftpClient.setBufferSize(1024 * 1024);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
         File downloadFile2 = new File(filePath);
         System.out.println("connection :: " + connection);
         System.out.println("Name ::"+downloadFile2.getName());
         
         String contentType = null;
         
         try {
             contentType = request.getServletContext().getMimeType(loadFileAsResource(filePath).getFile().getAbsolutePath());
         } catch (IOException ex) {
            System.out.println("Could not determine file type.");
         }
         
         if(contentType == null) {
             contentType = "application/octet-stream";
         }
         
         InputStreamResource resource = new InputStreamResource(getStream(ftpClient,filePath,downloadFile2));
         
    	return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).
        		header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+downloadFile2.getName()+"\"").
        		body(resource);
    	
    	
    	//return response;
    }
    
    public InputStream getStream(FTPClient ftpClient, String filePath, File downloadFile2) {
    	
    	InputStream input=null;
    	byte[] inputStreamInByte = null; 
    	try {
			input = ftpClient.retrieveFileStream(filePath);
			inputStreamInByte = IOUtils.toByteArray(input);
			System.out.println("PWD " +  ftpClient.getReplyString());
			System.out.println("PWD " +  ftpClient.printWorkingDirectory());
			ftpClient.completePendingCommand();
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return new ByteArrayInputStream(inputStreamInByte);
    }
    
    public Resource loadFileAsResource(String fileName) {
    	 Resource resource = null;
    	try {
            resource = new UrlResource(Path.of(fileName).toUri());
            if(resource.exists()) {
                return resource;
            } else {
                //throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            //throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
		return resource;
    }
    
    public String deleteFilesAndDirectory(String currentDirPath) {
    	
    	FTPClient ftpClient = new FTPClient();
        Boolean deleted = false;
        try {
        	
            ftpClient.connect(ftpServer, ftpPort);
            ftpClient.login(ftpUsername, ftpPassword);
            ftpClient.enterLocalPassiveMode();
            String parentDir = ftpClient.printWorkingDirectory(); 
            deleted = removeDirectory(ftpClient,parentDir,currentDirPath);
	        ftpClient.logout();  
	        ftpClient.disconnect();
        
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if(deleted) {
            return "SUCCESS";
        }
        else {
            return "FAILED";
        }
   }

	/**
	 * @param ftpClient
	 * @param currentDir
	 * @param deleted
	 * @param dirToRemove
	 * @return
	 * @throws IOException
	 */
	private Boolean removeDirectory(FTPClient ftpClient, String parentDir, String currentDirPath)
			throws IOException {
		
		boolean isDeleted = false;
		String dirToRemove = "";
		
		if (!currentDirPath.equals("")) {
			dirToRemove = parentDir +"/" + currentDirPath;
		}
		System.out.println("directoryToRemove::" + dirToRemove);
		System.out.println("directory isExist::" + ftpClient.cwd(dirToRemove));
		
		int dirIsExist = ftpClient.cwd(dirToRemove);
		if(dirIsExist == 250)  //250 - File Exists, 550 - File Not Exists
		{
			FTPFile[] subFiles = ftpClient.listFiles(dirToRemove);
			if(subFiles != null && subFiles.length > 0) {
				for (FTPFile aFile : subFiles) {
					
					System.out.println("List of files ::" + aFile);					
					String currentFileName = aFile.getName();
					if(currentFileName.equals(".") || currentFileName.equals("..")) {
						// skip parent directory and the directory itself
						continue;
					}
					String filePath = parentDir + "/" + currentDirPath + "/"+ currentFileName;
					if (currentDirPath.equals("")) {
						filePath = parentDir + "/" + currentFileName;
					}
			
					if (aFile.isDirectory()) {
					// remove the sub directory
						removeDirectory(ftpClient,dirToRemove, filePath);
					} 
					else {
						// delete the file
						boolean fileDeleted = ftpClient.deleteFile(filePath);
			    		if (fileDeleted) {
			    			System.out.println("DELETED the file: " + filePath);
			    		} else {
			    			System.out.println("CANNOT delete the file: "+ filePath);
			    		}            	
					}				
				}
	        }
			
			// finally, remove the directory itself
			isDeleted = ftpClient.removeDirectory(dirToRemove);
			if (isDeleted) {
				System.out.println("REMOVED the directory: " + dirToRemove);
			} else {
				System.out.println("CANNOT remove the directory: " + dirToRemove);
			}
			
		}else {
			
			//Not Directory Exists
			isDeleted = true;
		}
		
		return isDeleted;
	}


	public String deleteFile(String filePath) {
		// TODO Auto-generated method stub
		FTPClient ftpClient = new FTPClient();
        Boolean deleted = false;
        String result = "";
        try {
        	
            ftpClient.connect(ftpServer, ftpPort);
            ftpClient.login(ftpUsername, ftpPassword);
            ftpClient.enterLocalPassiveMode();
            
            FTPFile[] remoteFiles = ftpClient.listFiles(filePath);
            //System.out.println("remoteFiles[0].getSize():" +remoteFiles[0].getSize() / 1024 );
            
            if(remoteFiles.length > 0) { 
            	
            	System.out.println("File " + remoteFiles[0].getName() + " exists"); 
            	deleted = ftpClient.deleteFile(filePath);
            	
            } else { 
            	System.out.println("File " + filePath + " does not exists"); 
            	result = "FILE_NOT_EXIST";
            }
                  
            System.out.println(">>deleted:"+deleted);
	        ftpClient.logout();  
	        ftpClient.disconnect();
	        
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if(deleted) {
            return "SUCCESS";
        }
        else if(!deleted && result == "FILE_NOT_EXIST") {
        	return result ;
        }
        else {
            return "FAILED";
        }
	}
	
}
