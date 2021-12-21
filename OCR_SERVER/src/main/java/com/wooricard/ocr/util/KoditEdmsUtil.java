package com.wooricard.ocr.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class KoditEdmsUtil {

	String EDMS_URL = "http://edmtest1.shinbo.co.kr:6501/storage/servlet/blob?Method=put&BLOBType=doc&FileStatus=N&StorageType=S";
	//String EDMS_URL = "http://150.3.1.54:28088/http/fepon";
	
	
	public void sendEdms() {
		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		HttpClientBuilder builder = HttpClientBuilder.create();
		
		builder.setDefaultRequestConfig(requestBuilder.build());
		builder.setConnectionTimeToLive(180, TimeUnit.MINUTES);
		HttpClient client = builder.build();
		
		HttpPost httpost = null
				;
		try {
			httpost = new HttpPost(new URI(EDMS_URL));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		
		String boundary = UUID.randomUUID().toString().replaceAll("-", "");
		boundary = "----------------------------" + boundary.substring(0, 12);
		//----------------------------
		
		//--Start-- byteArray 에 파일 불러들이기
		FileInputStream fin = null;
		ByteArrayOutputStream bao = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayInputStream bin = null;
		
		int bytesRead = 0;
		byte[] buff = new byte[1024];
		
		String fullPath = "/pgms/oasisblue/wasroot/jsp/Ksys/KIinft/decrypt.tif";
		String fileName = "decrypt.tif";
		
		File file = new File(fullPath);
		
		try {
			fin = new FileInputStream(file);
			bao = new ByteArrayOutputStream();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			while ((bytesRead = fin.read(buff)) > 0) {
				bao.write(buff, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] fileByte = bao.toByteArray();
		//--End-- byteArray에 파일 불러들이기
		
		String CharSet = "UTF-8";
		//entityBuilder.setCharset(Charset.forName(CharSet));
		try {
			entityBuilder.addBinaryBody("sendfile", fileByte, ContentType.MULTIPART_FORM_DATA,
					URLEncoder.encode(fileName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entityBuilder.setBoundary(boundary);
		httpost.setEntity(entityBuilder.build());
		
		HttpResponse tempResponse;
		String sreturnString = "";
		
		try {
			tempResponse = client.execute(httpost);
			
			HttpEntity resEntity = tempResponse.getEntity();
			
			byte[] resBytes = EntityUtils.toByteArray(resEntity);
			
			sreturnString = new String(resBytes, "utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
