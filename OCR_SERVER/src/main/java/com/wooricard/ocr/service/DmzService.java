package com.wooricard.ocr.service;

import com.wooricard.ocr.model.request.FileUploadRequest;
import com.wooricard.ocr.model.response.FileUploadResponse;

public interface DmzService {

	public FileUploadResponse upload(FileUploadRequest fileUploadRequest);
	
	public void test();
}
