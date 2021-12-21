package com.wooricard.ocr.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.wooricard.ocr.model.request.FileUploadRequest;

public class OcrUtils {
	
//	private static Logger logger = LoggerFactory.getLogger(OcrUtils.class);
    
    public static String sendOcrRequest(FileUploadRequest ocrRequest) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/proxy/upload");
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("requestChannel", ocrRequest.getRequestChannel());
        builder.addTextBody("imageType", ocrRequest.getImageType());
        builder.addTextBody("imageSeq", ocrRequest.getImageSeq());
        builder.addTextBody("dataKey", ocrRequest.getDataKey());

        try {
			builder.addBinaryBody("files", ocrRequest.getFiles().get(0).getBytes(), ContentType.APPLICATION_OCTET_STREAM, "test");
		} catch (IOException e) {
			e.printStackTrace();
		}

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);

            //OcrResultResponse ocrResponse = (OcrResultResponse) response.getEntity();
            
           // Gson gson = new GsonBuilder().create();
            
//            Map<String,Object> map 
//            	= gson.fromJson(EntityUtils.toString(response.getEntity()), new TypeToken<HashMap<String, Object>>() {}.getType());
  
            return EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
}
