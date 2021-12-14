package com.kodit.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.inzisoft.crypto.ARIACryptoJNI;
import com.inzisoft.server.codec.ImageIOJNI;

@Component
public class KoditEdmsUploadUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(KoditEdmsUploadUtil.class);

	private ImageIOJNI imageIOJNI;
	
	/**
	 * TIFF 파일 병합
	 * 
	 * @param fileList		병합할 파일 경로 배열
	 * @param resultPath	병합 결과 파일 경로
	 * @return
	 * @throws Exception
	 */
	public String mergeTiff(String[] fileList, String resultPath) {
		
		logger.info("===== mergeTiff ====");
		
		int count = fileList.length;
		
		if (count == 1) {
			return fileList[0];
		} else if (count > 1) {
			imageIOJNI = new ImageIOJNI();
			
			int result = -1;
			
			String firstFilePath = fileList[0];
			
			File firstFile = new File(firstFilePath);
			
			try {
				FileUtils.copyFile(firstFile, new File(resultPath));
			} catch (IOException e) {
				logger.error("File copy fail");
				e.printStackTrace();
			}
			
			for (int i=1; i < fileList.length; i++) {
				logger.info("merge {}", fileList[i]);
				result = imageIOJNI.mergeTIFF_FILE(resultPath, fileList[i]);
				
				if (result > 0) {
					logger.error("fail merge - errorCode : {}", result);
				}
			}	
			
			return resultPath;
			
		}
		return null;
	}
	
	/**
	 * 이미지 암호화
	 * 
	 * @param filePath		암호화 대상 이미지 파일 경로
	 * @param resultPath	결과 파일 경로
	 * @return
	 */
	public String encryptImageFile(String filePath, String resultPath) {
	
		logger.info("===== encryptImageFile =====");
		
		long cryptoObj = ARIACryptoJNI.CreateObj();

		if (cryptoObj == 0) {
			logger.error("Create cryptoObj failed");
			return null;
		}
		
		try {
			// 신용보증기금 키
			if (!ARIACryptoJNI.SetStringKey(cryptoObj, "#@10DNJFDP RKSEK@#")) {
				logger.error("Failed to set keys, errNo = " + ARIACryptoJNI.GetErrNo(cryptoObj));
				return null;
			}
			
			// 파일 암호화
			if (!ARIACryptoJNI.Encrypt(cryptoObj, filePath, resultPath, false)) {
				logger.error("Encrypt failed, errNo = " + ARIACryptoJNI.GetErrNo(cryptoObj));
				return null;
			}
			
			return resultPath;
		} catch (Exception e) {
			logger.error("Exception", e);
			return null;
		} finally {
			ARIACryptoJNI.DestroyObj(cryptoObj);
		}
	}

	public String sendEdms(String url, String filePath) {
		logger.info("===== Send edms =====");

		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setDefaultRequestConfig(requestBuilder.build());
		builder.setConnectionTimeToLive(180, TimeUnit.MINUTES);
		HttpClient client = builder.build();
		
		HttpPost httpost = null;
		try {
			httpost = new HttpPost(new URI(url));
		} catch (URISyntaxException e) {
			logger.error("Exception", e);
		}
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		String boundary = java.util.UUID.randomUUID().toString().replaceAll("-", "");
		boundary = "----------------------------" + boundary.substring(0, 12);
		//----------------------------
		
		//--Start-- byteArray 에 파일 불러들이기
		FileInputStream fin = null;
		ByteArrayOutputStream bao = null;

		int bytesRead = 0;
		byte[] buff = new byte[1024];
		
		File file = new File(filePath);
		
		if (!file.exists()) {
			logger.error("file not found : {}", filePath);
			return null;
		}
		
		try {
			fin = new FileInputStream(file);
			
			bao = new ByteArrayOutputStream();
			while ((bytesRead = fin.read(buff)) > 0) {
				bao.write(buff, 0, bytesRead);
			}
			byte[] fileByte = bao.toByteArray();
			//--End-- byteArray에 파일 불러들이기
			
			entityBuilder.addBinaryBody("sendfile", fileByte, ContentType.MULTIPART_FORM_DATA, URLEncoder.encode(FilenameUtils.getName(filePath), "UTF-8"));
			entityBuilder.setBoundary(boundary);
			httpost.setEntity(entityBuilder.build());
			
			HttpResponse tempResponse = client.execute(httpost);
			HttpEntity resEntity = tempResponse.getEntity();
			
			byte[] resBytes = EntityUtils.toByteArray(resEntity);
			
			String response = new String(resBytes, "utf-8");
			
			return response;

		} catch (FileNotFoundException e) {
			logger.error("Exception", e);
		} catch (IOException e) {
			logger.error("Exception", e);
		} finally {
			try {
				if (fin != null) {
					fin.close();
				}
				
				if (bao != null) {
					bao.close();	
				}
			} catch (IOException e) {
				logger.error("Exception", e);
			}
		}
		
		return null;
	}

	
	public static String sendEdmsCustom(String edmsUrl, String filePath) {
		logger.info("===== sendEdmsCustom =====");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(edmsUrl);
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
       
        try {
        	byte[] fileBytes = FileUtils.readFileToByteArray(new File(filePath));
        	
			builder.addBinaryBody("sendfile", fileBytes, ContentType.APPLICATION_OCTET_STREAM, FilenameUtils.getName(filePath));
		} catch (IOException e) {
			logger.error("Exception", e);
		}

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            logger.error("Exception", e);
        } catch (IOException e) {
            logger.error("Exception", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("Exception", e);
            }
        }
        
        return null;
	}
	
}
