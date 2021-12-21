<%@page import="org.apache.commons.io.FilenameUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.kodit.util.KoditEdmsUploadUtil" %>

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
	
	String originFilePath	= request.getParameter("originFilePath");	// 본문 이미지 경로
	String coverFilePath 	= request.getParameter("coverFilePath");	// 커버 이미지 경로
	
	KoditEdmsUploadUtil koditEdmsUploadUtil = new KoditEdmsUploadUtil();
	
	//////////////////////////////////////////////////////////////////////////////
	// 이미지 병합 start
	
	sb = new StringBuilder();
	sb.append(FilenameUtils.getFullPath(originFilePath));
	sb.append(FilenameUtils.getBaseName(originFilePath));
	sb.append("_Merge.tif");
	
	String[] fileList = {originFilePath, coverFilePath};
	
	String mergeFilePath = koditEdmsUploadUtil.mergeTiff(fileList, sb.toString());
	
	// 이미지 병합 end
	//////////////////////////////////////////////////////////////////////////////
	
	//////////////////////////////////////////////////////////////////////////////
	// 이미지 암호화 start
	sb = new StringBuilder(); 
	sb.append(FilenameUtils.getFullPath(originFilePath));
	sb.append(FilenameUtils.getBaseName(originFilePath));
	sb.append("_Secu.tif");
	
	//String encFilePath = koditEdmsUploadUtil.encryptImageFile(mergeFilePath, sb.toString());
	
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

	
	String sreturnString = koditEdmsUploadUtil.sendEdms(EDMS_URL, originFilePath);
	//String sreturnString = koditEdmsUploadUtil.sendEdms(EDMS_URL, encFilePath);
	
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
