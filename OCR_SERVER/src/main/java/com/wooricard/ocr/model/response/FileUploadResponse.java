package com.wooricard.ocr.model.response;

public class FileUploadResponse {

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OcrResultResponse [");
		if (resultCode != null) {
			builder.append("resultCode=");
			builder.append(resultCode);
			builder.append(", ");
		}
		if (resultMessage != null) {
			builder.append("resultMessage=");
			builder.append(resultMessage);
			builder.append(", ");
		}
		if (ocrResponse != null) {
			builder.append("ocrResponse=");
			builder.append(ocrResponse);
		}
		builder.append("]");
		return builder.toString();
	}
	
}
