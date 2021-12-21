package com.wooricard.ocr.type;

public enum RequestType {

    OCR("01", "OCR 및 파일업로드"),
    EDMS("02", "EDMS 전송");

    private String code;

    private String description;

    private RequestType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RequestType getByCode(String code) {
        for (RequestType value : RequestType.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
