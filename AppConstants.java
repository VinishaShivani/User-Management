package com.trb.allocationservice.util;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class AppConstants {

	public static String NOAllOCATIONS_RESPONSE_CODE = "ISS-001";
	public static String NOAllOCATIONS_RESPONSE_MESSAGE = "No Allocations Provided";
	
	public static String DELETE_AllOCATIONS_RESPONSE_CODE = "ISS-005";
	public static String DELETE_AllOCATIONS_RESPONSE_MESSAGE = "No Allocations Provided to delete";
	
	public static String UPDATE_AllOCATIONS_RESPONSE_CODE = "ISS-006";
	public static String UPDATE_AllOCATIONS_RESPONSE_MESSAGE = "No Allocations Provided to Update";
	
	public static String NAME_RESPONSE_CODE = "ISS-002";
	public static String NAME_RESPONSE_MESSAGE = "User Id is not Provided";
	
	public static String SUBJECT_RESPONSE_CODE = "ISS-003";
	public static String SUBJECT_RESPONSE_MESSAGE = "subject is not Provided";
	
	public static String OBJECTION_QUESTIONS_RESPONSE_CODE = "ISS-004";
	public static String OBJECTION_QUESTIONS_RESPONSE_MESSAGE = "objected questions not Provided";
	
	public static String SUCCESS_RESPONSE_CODE = "SUC-001";
	public static String SUCCESS_RESPONSE_MESSAGE = "Objection Questions Allocated to User";
	
	public static String DELETE_SUCCESS_RESPONSE_CODE = "SUC-002";
	public static String DELETE_SUCCESS_RESPONSE_MESSAGE = "Allocation Successfully Deleted";
	
	public static String RECORD_NOT_FOUND_RESPONSE_CODE = "ISS-006";
	public static String RECORD_NOT_FOUND_RESPONSE_MESSAGE = "Record is not available to delete";
	
	public static String Execption_RESPONSE_CODE = "EXEC-001";
	public static String Execption_RESPONSE_MESSAGE = "SQL Data Exception";
	
	public static String UPDATE_RECORD_NOT_FOUND_RESPONSE_CODE = "ISS-007";
	public static String UPDATE_RECORD_NOT_FOUND_RESPONSE_MESSAGE = "Record is not available";
	
	public static String NAME_RECORD_NOT_FOUND_RESPONSE_CODE = "ISS-007";
	public static String NAME_RECORD_NOT_FOUND_RESPONSE_MESSAGE = "Record is not available";
	
	public static String UPDATE_RECORD_SUCCESS_RESPONSE_CODE = "SUC-003";
	public static String UPDATE_RECORD_SUCCESS_RESPONSE_MESSAGE = "Record Updated Successfully";
	
	public static String NAME_SUCCESS_RESPONSE_CODE = "SUC-004";
	public static String NAME_SUCCESS_RESPONSE_MESSAGE = "Allocations were provided based on UserId";
	
	public static String STATUS_SUCCESS_RESPONSE_CODE = "SUC-005";
	public static String STATUS_SUCCESS_RESPONSE_MESSAGE = "Allocations were provided based on Status";
	
	public static String STATUS_RECORD_NOT_FOUND_RESPONSE_CODE = "ISS-008";
	public static String STATUS_RECORD_NOT_FOUND_RESPONSE_MESSAGE = "Record is not available";
	
	public static String QUESTIONID_SUCCESS_RESPONSE_CODE = "SUC-006";
	public static String QUESTIONID_SUCCESS_RESPONSE_MESSAGE = "Records were retrived based on question id";
	
	public static String SUBJECT_SUCCESS_RESPONSE_CODE = "SUC-007";
	public static String SUBJECT_SUCCESS_RESPONSE_MESSAGE = "Records were retrived based on Subject";
	
	public static String ALLOCATIONS_SUCCESS_RESPONSE_CODE = "SUC-008";
	public static String ALLOCATIONS_SUCCESS_RESPONSE_MESSAGE = "All Allocated Records were retrived";
	
	public static String NO_ALLOCATIONS_AVAILABLE_RESPONSE_CODE = "ISS-011";
	public static String NO_ALLOCATIONS_AVAILABLE_RESPONSE_MESSAGE = "No Allocated Records were retrived";
	
	public static String SUBJECT_RECORD_NOT_FOUND_RESPONSE_CODE = "ISS-009";
	public static String SUBJECT_RECORD_NOT_FOUND_RESPONSE_MESSAGE = "Record is not available";
	
	public static String RECORD_IS_NOT_AVAILABLE_CODE = "ISS-010";
	public static String RECORD_IS_NOT_AVAILABLE_CODE_MESSAGE = "No Question Ids were provided to get Allocation";
	
	public static String DELETE_ALLOCATION_RECORD_NOT_AVAILABLE_CODE = "ISS-012";
	public static String DELETE_ALLOCATION_RECORD_NOT_AVAILABLE_MESSAGE = "Please Select Valid Allocated Questions";
		
	public static String UPDATE_RESPONSE_CODE7 = "SUC-009";
	public static String UPDATE_RESPONSE_MSG7 = "subject Details Updated Successfully";
	
	public static String PARTIAL_SUCCESS_RESPONSE_CODE = "ERR-101";
	public static String PARTIAL_SUCCESS_RESPONSE_MSG = "Partial Subject(s) Added";
	
	public static String NO_RECORD_SUCCESS_RESPONSE_CODE = "ERR-102";
	public static String NO_RECORD_SUCCESS_RESPONSE_MSG = "Record(s) Contains Invalid Data";
	
	public static String NO_QUESTIONS_RESPONSE_CODE = "ERR-103";
	public static String NO_QUESTIONS_RESPONSE_MESSAGE = "objected questions not Provided";
	
	public static String NO_SUBJECT_RESPONSE_CODE = "ERR-104";
	public static String NO_SUBJECT_RESPONSE_MESSAGE = "subject is not Provided";
	
	public static String PENDING_SUBJECT_RESPONSE_CODE = "ERR-105";
	public static String PENDING_SUBJECT_RESPONSE_MESSAGE = "subject cannot change";
	
	public static String SAVE_RECORD_SUCCESS_RESPONSE_CODE = "SUC-101";
	public static String SAVE_RECORD_SUCCESS_RESPONSE_MESSAGE = "Record Saved Successfully";
	
	public static String SUCCESS_RESPONSE_CODE1 = "SUC-002";
	public static String SUCCESS_RESPONSE_MSG1 = "Questions  Retrived Successfully";
    
	public static String SUCCESS_RESPONSE_CODE2 = "SUC-003";
	public static String SUCCESS_RESPONSE_MSG2 = "No records Available in Database";
	
	public static String SUCCESS_RESPONSE_CODE3 = "SUC-004";
	public static String SUCCESS_RESPONSE_MSG3 = "Input empty";
	
	public static String SUCCESS_RESPONSE_CODE4 = "SUC-005";
	public static String SUCCESS_RESPONSE_MSG4 = "deleted successfully";
	
	
	public static Integer YET_TO_ALLOCATE = 5;
	
	public static String MODE_INSERT = "A";
	public static String MODE_MODIFY = "M";
	public static String MODE_REMOVE = "R";
	
   public static  Map<Integer, String> options = createMap();
	
	private static Map<Integer, String> createMap() {
	    Map<Integer,String> optmap = new HashMap<Integer,String>();
	    optmap.put(1, "A");
	    optmap.put(2, "B");
	    optmap.put(3, "C");
	    optmap.put(4, "D");
	    optmap.put(5, "E");
	    return optmap;
	}
}
