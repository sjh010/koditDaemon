<%@page import="org.apache.commons.io.FilenameUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>

<!DOCTYPE html>

<%
	String originFilePath	= request.getParameter("originFilePath");	// 본문 이미지 경로
	String coverFilePath 	= request.getParameter("coverFilePath");	// 커버 이미지 경로

	String param = originFilePath + "|" + coverFilePath;
	
	String sreturnString = "";
	
	Socket socket = null;
	
	try {
		socket = new Socket("127.0.0.1", 10500);
		System.out.println("socket connect..");

		OutputStream os = socket.getOutputStream();

		InputStream is = socket.getInputStream();

		os.write(param.getBytes("UTF-8"));
		os.flush();

		byte[] data = new byte[1024];
		int n = is.read(data);

		sreturnString = new String(data, 0, n);
		
		System.out.println(sreturnString);
		
		socket.close();
	} catch (UnknownHostException e) {
		sreturnString = "^^Z^E003";
		e.printStackTrace();
	} catch (IOException e) {
		sreturnString = "^^Z^E003";
		e.printStackTrace();
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FileUpload.jsp</title>
<script type="text/javascript">
	function fn_onLoad () {
		var returnString =  '<%=sreturnString%>';
		
		window.document.title = returnString;
	}
</script>
</head>
<body onLoad='fn_onLoad()'>
</body>
</html>
