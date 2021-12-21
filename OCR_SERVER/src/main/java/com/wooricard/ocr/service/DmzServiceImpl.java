package com.wooricard.ocr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wooricard.ocr.model.request.FileUploadRequest;
import com.wooricard.ocr.model.response.FileUploadResponse;
import com.wooricard.ocr.util.OcrUtils;

@Service
public class DmzServiceImpl implements DmzService {

	private static final Logger logger = LoggerFactory.getLogger(DmzServiceImpl.class);
	
	@Override
	public FileUploadResponse upload(FileUploadRequest fileUploadRequest) {
		fileUploadRequest.setDataKey(encrypt(fileUploadRequest.getDataKey()));

		String ocrResult = OcrUtils.sendOcrRequest(fileUploadRequest);
		
		logger.info("OCR Result : {}", ocrResult);
		
		Gson gson = new GsonBuilder().create();
		
		FileUploadResponse response = gson.fromJson(ocrResult, FileUploadResponse.class);
		
		logger.info("Response : {}", response.toString());
		
		return response;
		
	}
	
	private String encrypt(String str) {
		return "encrypted_" + str;
	}

	@Override
	public void test() {
		System.out.println("TEST!!!");
	}

}
