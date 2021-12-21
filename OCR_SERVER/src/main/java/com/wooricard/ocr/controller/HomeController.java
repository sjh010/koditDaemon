package com.wooricard.ocr.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wooricard.ocr.model.request.EdmsRequest;
import com.wooricard.ocr.model.request.FileUploadRequest;
import com.wooricard.ocr.model.response.FileUploadResponse;
import com.wooricard.ocr.service.DmzService;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private DmzService dmzService;
	
	/**
     * OCR 및 파일 저장
     * 
     * @param OcrProxyRequest
     * @param bindingResult
     * @return {@link OcrResultResponse}
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<FileUploadResponse> uploadPost(FileUploadRequest fileUploadRequest, BindingResult bindingResult) {
        logger.info("DMZ Server -- request file upload.......");
        logger.info(fileUploadRequest.toString());
        return new ResponseEntity<FileUploadResponse>(dmzService.upload(fileUploadRequest), HttpStatus.OK);
    }
   
    /**
     * EDMS 전송 요청
     * 
     * @param confirmYn
     * @param encno
     * @return
     */
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> confirm(@ModelAttribute @Valid EdmsRequest edmsRequest) {
        logger.info("DMZ Server -- request confirm.............");
        logger.info(edmsRequest.toString());
        
        return new ResponseEntity<String>("test", HttpStatus.OK);
    } 
    
    // TODO : 테스트
    @RequestMapping(value = "/uploadPage", method = RequestMethod.GET)
    public String uploadPage() {
		return "upload";
	}
	
}
