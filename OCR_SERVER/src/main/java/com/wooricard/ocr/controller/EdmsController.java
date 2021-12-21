package com.wooricard.ocr.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class EdmsController {
	
	private static final Logger logger = LoggerFactory.getLogger(EdmsController.class);
    
    @RequestMapping(value = "/sendEdms", method = RequestMethod.GET)
    public String test(HttpServletRequest request) {
    	
    	logger.info("filePath : " + request.getParameter("originFilePath"));
    	logger.info("fileName : " + request.getParameter("coverFilePath"));
    	
		return "edmsUpload3";
	}
    
    
    
    @RequestMapping(value = "/storage/servlet/blob", method = RequestMethod.POST)
    @ResponseBody
    public String edmsTest(HttpServletResponse response, MultipartHttpServletRequest request) {
//    	String EDMS_URL = "http://127.0.0.1:8090/storage/servlet/blob?Method=put&BLOBType=doc&FileStatus=N&StorageType=S";
    	
    	logger.info("Method      : " + request.getParameter("Method"));
    	logger.info("BLOBType    : " + request.getParameter("BLOBType"));
    	logger.info("FileStatus  : " + request.getParameter("FileStatus"));
    	logger.info("StorageType : " + request.getParameter("StorageType"));
    	
    	
    	MultipartFile file = request.getFile("sendfile");
    	
    	logger.info("sendfile    : " + file.getOriginalFilename());
    	
    	return "success";
    }
	

}
