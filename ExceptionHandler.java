package com.trb.allocationservice.exception;

import java.io.IOException;

import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.trb.allocationservice.dto.ResponseDTO;


@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDTO> genericException(Exception exception) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatusCode("ERR-0001");
		responseDTO.setMessage(exception.getMessage());
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(ArrayIndexOutOfBoundsException.class)
	public ResponseEntity<ResponseDTO> ArrayIndexOutofBoundException(ArrayIndexOutOfBoundsException exception) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatusCode("ERR-0002");
		responseDTO.setMessage(exception.getMessage());
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
	}
	@org.springframework.web.bind.annotation.ExceptionHandler(HibernateException.class)
	public ResponseEntity<ResponseDTO> hibernateException(HibernateException exception) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatusCode("ERR-0003");
		responseDTO.setMessage(exception.getMessage());
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);

	}
	@org.springframework.web.bind.annotation.ExceptionHandler(ClassNotFoundException.class)
	public ResponseEntity<ResponseDTO> classNotFoundException(ClassNotFoundException exception) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatusCode("ERR-0004");
		responseDTO.setMessage(exception.getMessage());
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ResponseDTO> nullPointerException(NullPointerException exception) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatusCode("ERR-0005");
		responseDTO.setMessage(exception.getMessage());
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
	}


	@org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResponseDTO> runtimeException(RuntimeException exception) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatusCode("ERR-0006");
		responseDTO.setMessage(exception.getMessage());
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(StringIndexOutOfBoundsException.class)
	public ResponseEntity<ResponseDTO> stringIndexOutOfBoundsException(StringIndexOutOfBoundsException exception) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatusCode("ERR-0007");
		responseDTO.setMessage(exception.getMessage());
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<ResponseDTO> numberFormatException(NumberFormatException exception) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatusCode("ERR-0008");
		responseDTO.setMessage(exception.getMessage());
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(IOException.class)
	public ResponseEntity<ResponseDTO> iOExceptions(IOException exception) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatusCode("ERR-0009");
		responseDTO.setMessage(exception.getMessage());
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
	}
}

