<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="./resources/js/jquery-3.2.1.min.js"></script>
<script>
	$(document).ready(function() {
		$("#btnSubmit").click(function() {
			var form = $("#uploadForm")[0];
			var formData = new FormData(form);
			
			formData.append("requestType", "2");
			formData.append("requestChannel", "1");
			formData.append("imageType", "02");
			formData.append("imageSeq", "001")
			formData.append("dataKey", "test");
			formData.append("files", $("#files")[0].files[0]);
			
			$.ajax({
				processData : false,
				contentType : false,
				url : "/dmz/upload",
				type : "POST",
				data : formData,
				success : function(response) {
					console.log(response);
					$("#image").attr("src", "data:image/jpg;base64, " + response.imageFile);
				}
			});
		});
		
		$("#testBtn").click(function() {
			var map1 = new Map();
			
			console.log(map1);
			
			map1.put("test", "result");
			
			$.ajax({
				processData : false,
				contentType : false,
				url : "/dmz/test",
				type : "POST",
				data : JSON.stringify(map),
				success : function(response) {
					console.log("success");
				}
			});
		});
	});
</script>
</head>


<body>

	<form id="uploadForm" action="/proxy/upload" method="post" enctype="multipart/form-data">
		FILE : <input id="files" type="file" name="files" />
	</form>
	<input type="button" id="btnSubmit" value="전송" />
	
	<br><br>
	
	<img id="image" />
	
	<input type="button" id="testBtn" value="test" />

</body>
</html>