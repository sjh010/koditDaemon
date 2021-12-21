<%@page import="org.apache.commons.io.FilenameUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.nio.*"%>
<%@ page import="java.util.concurrent.TimeUnit"%>
<%@ page import="org.apache.http.HttpEntity"%>
<%@ page import="org.apache.http.HttpResponse"%>
<%@ page import="org.apache.http.client.HttpClient"%>
<%@ page import="org.apache.http.client.config.RequestConfig"%>
<%@ page import="org.apache.http.client.methods.HttpPost"%>
<%@ page import="org.apache.http.entity.ContentType"%>
<%@ page import="org.apache.http.entity.mime.MultipartEntityBuilder"%>
<%@ page import="org.apache.http.impl.client.HttpClientBuilder"%>
<%@ page import="org.apache.http.util.EntityUtils"%>

<%@ page import="com.inzisoft.crypto.ARIACryptoJNI" %>
<%@ page import="com.inzisoft.server.codec.ImageIOJNI" %>

<!DOCTYPE html>

<%
	//////////////////////////////////////////////////////////////////////////////
	// 1. watermark 삽입
	// 2. 이미지 병합(multi tiff)
	// 3. 이미지 암호화
	// 4. EDMS 전송
	//////////////////////////////////////////////////////////////////////////////

	StringBuilder sb;
	
	//String filePath = "/pgms/oasisblue/wasroot/jsp/Ksys/KIinft/decrypt.tif";
	//String fileName = "decrypt.tif";	
	
	String originFilePath = request.getParameter("originFilePath");	// 본문 이미지 경로
	String coverFilePath = request.getParameter("coverFilePath");	// 커버 이미지 경로
	
	//////////////////////////////////////////////////////////////////////////////
	// 이미지 병합 start
	sb = new StringBuilder();
	sb.append(FilenameUtils.getFullPath(originFilePath));
	sb.append(FilenameUtils.getBaseName(originFilePath));
	sb.append("_Merge.tif");
	
	int result = 0;
	
	ImageIOJNI imageIOJNI = new ImageIOJNI();
	
	result = imageIOJNI.mergeTIFF_FILE(coverFilePath, originFilePath);
	
	if (result < 0) {
		System.out.println("tif merge fail - result : " + result);
	}
	
	String mergeFilePath = sb.toString(); // 병합 이미지 경로
	
	boolean isSuccess = new File(originFilePath).renameTo(new File(mergeFilePath));
	
	// 이미지 병합 end
	//////////////////////////////////////////////////////////////////////////////
	
	//////////////////////////////////////////////////////////////////////////////
	// 이미지 암호화 start
	sb = new StringBuilder(); 
	sb.append(FilenameUtils.getFullPath(originFilePath));
	sb.append(FilenameUtils.getBaseName(originFilePath));
	sb.append("_Secu.tif");
	
	String encFilePath = sb.toString();
	
	long cryptoObj = ARIACryptoJNI.CreateObj();

	if (cryptoObj == 0) {
		System.out.println("Create cryptoObj failed");
		return;
	}
	
	try {
		// 신용보증기금 키
		if (!ARIACryptoJNI.SetStringKey(cryptoObj, "#@10DNJFDP RKSEK@#")) {
			System.out.println("Failed to set keys, errNo = " + ARIACryptoJNI.GetErrNo(cryptoObj));
			return;
		}
		
		// 파일 암호화
		if (!ARIACryptoJNI.Encrypt(cryptoObj, mergeFilePath, encFilePath, false)) {
			System.out.println("Encrypt failed, errNo = " + ARIACryptoJNI.GetErrNo(cryptoObj));
			return;
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		ARIACryptoJNI.DestroyObj(cryptoObj);
	}
	// 이미지 암호화 end
	//////////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////////
	// EDMS 전송 start
	
	// 로컬 테스트
	String EDMS_URL = "http://127.0.0.1:8090/storage/servlet/blob?Method=put&BLOBType=doc&FileStatus=N&StorageType=S";

	// 개발
	//String EDMS_URL = "http://edmtest1.shinbo.co.kr:6501/storage/servlet/blob?Method=put&BLOBType=doc&FileStatus=N&StorageType=S";

	// 운영
	//String EDMS_URL = "";

	//String EDMS_URL = "http://150.3.1.54:28088/http/fepon";

	
	RequestConfig.Builder requestBuilder = RequestConfig.custom();
	HttpClientBuilder builder = HttpClientBuilder.create();
	builder.setDefaultRequestConfig(requestBuilder.build());
	builder.setConnectionTimeToLive(180, TimeUnit.MINUTES);
	HttpClient client = builder.build();
	
	HttpPost httpost = new HttpPost(new URI(EDMS_URL));
	MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
	String boundary = java.util.UUID.randomUUID().toString().replaceAll("-", "");
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
	
	
	
	File file = new File(encFilePath);
	
	fin = new FileInputStream(file);
	bao = new ByteArrayOutputStream();
	while ((bytesRead = fin.read(buff)) > 0) {
		bao.write(buff, 0, bytesRead);
	}
	byte[] fileByte = bao.toByteArray();
	//--End-- byteArray에 파일 불러들이기
	
	//String CharSet = "UTF-8";
	//entityBuilder.setCharset(Charset.forName(CharSet));
	entityBuilder.addBinaryBody("sendfile", fileByte, ContentType.MULTIPART_FORM_DATA, URLEncoder.encode(encFilePath, "UTF-8"));
	entityBuilder.setBoundary(boundary);
	httpost.setEntity(entityBuilder.build());
	
	HttpResponse tempResponse = client.execute(httpost);
	HttpEntity resEntity = tempResponse.getEntity();
	
	byte[] resBytes = EntityUtils.toByteArray(resEntity);
	
	String sreturnString = new String(resBytes, "utf-8");
	
	bao.close();
	bos.close();
	bin.close();
	fin.close();
	fos.close();
	
	// EDMS 전송 end
	//////////////////////////////////////////////////////////////////////////////
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FileUpload.jsp</title>
<script type="text/javascript">
	function fn_onLoad () {
		var returnString =  '<%=sreturnString%>';
		alert(returnString);
	}
</script>
</head>
<body onLoad='fn_onLoad()'>
</body>
</html>
