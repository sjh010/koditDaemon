package com.wooricard.ocr.model.response;

public class TestResponse {

	private String resultCode;
	
	private String resultMessage;
	
	private OcrResponse ocrResponse;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public OcrResponse getOcrResponse() {
		return ocrResponse;
	}

	public void setOcrResponse(OcrResponse ocrResponse) {
		this.ocrResponse = ocrResponse;
	}
	
	
}
