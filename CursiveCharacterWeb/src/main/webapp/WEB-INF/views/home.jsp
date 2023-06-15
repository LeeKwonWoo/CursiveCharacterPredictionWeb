<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<link href="<c:url value="/resources/css/grid.css"/>" rel="stylesheet">

<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width">
<head>
	<title>CursiveCharacterPredictionWeb</title>
</head>

<body>
	
	<a href="/controller">HOME</a>
	<form:form modelAttribute="predictImg" action="./predict" enctype="multipart/form-data" method="post">
		
		<div id="root">
			<h2 class="title">CursiveCharacter Prediction</h2>
			<hr>
			<div class="contents">
				<div class="upload-box">
					<div id="drop-file" class="drag-file">
						<c:choose>
							<c:when test="${savedImageDir == null}">
								<img src="<c:url value="https://img.icons8.com/pastel-glyph/2x/image-file.png"/>" style="width: 224px; height: 224px;" alt="파일 아이콘">
								<p class="message">File to Upload</p>
								<img src="" alt="미리보기 이미지" class="preview">
							</c:when>
							<c:otherwise>
								<img src="<c:url value="/resources/userSavedFiles/${savedImageDir}"/>" style="width: 224px; height: 224px;" alt="파일 아이콘">
								<p class="message">Uploaded File</p>
							</c:otherwise>
						</c:choose>
						
					</div>
					<label class="file-label" for="chooseFile">Choose File</label>
					<form:input path="ccImage" class="file" id="chooseFile" type="file" onchange="dropFile.handleFiles(this.files)" accept="image/png, image/jpeg, image/gif"/>
				</div>
				<div class="upload-box">
					<div id="drop-file" class="drag-file">
						<c:choose>
							<c:when test="${predictedDir == null}">
								<img src="<c:url value="/resources/images/predictedcharacter.jpg"/>" style="width: 224px; height: 224px;" alt="파일 아이콘">
							</c:when>
							<c:otherwise>
								<img src="<c:url value="/resources/images/original (copy)/${predictedDir}"/>" style="width: 224px; height: 224px;" alt="파일 아이콘">
							</c:otherwise>
						</c:choose>
						<p class="message">Predicted CursiveCharacter</p>
					</div>
					<input class="filesecond" type="submit" value="Prediction"></input>
				</div>
			</div>	
		</div>
		
	</form:form>
	 
	<p>${savedImageDir}</p>
	 
	<%
		for (int i = 0; i <= 5; i++) {
	%>
	<br>
	<%
		}
	%>
	 
	<div align="center">
		<div>
			<%-- <c:forEach items="${bookList}" var="book"
			 </c:forEach>
			 --%>
			 <table>
			 	<thead>
			 		<tr>
			 			<th>Name</th>
			 			<%
			 			for (int i = 0; i <= 30; i++) {
			 			%>
			 			<th> </th>
			 			<%
			 			}
			 			%>
			 			<th>predict value(%)</th>
			 		</tr>
			 	</thead>
			 	<tbody>
			 		<tr>
			 			<td>han_il</td>
			 			<%
			 			for (int i = 0; i <= 30; i++) {
			 			%>
			 			<td> </td>
			 			<%
			 			}
			 			%>
			 			<td>90%</td>
			 		</tr>
			 	</tbody>
			 </table>
		</div>
	</div>
</body>
<script>
function DropFile(dropAreaId, fileListId) {
	  let dropArea = document.getElementById(dropAreaId);
	  let fileList = document.getElementById(fileListId);

	  function preventDefaults(e) {
	    e.preventDefault();
	    e.stopPropagation();
	  }

	  function highlight(e) {
	    preventDefaults(e);
	    dropArea.classList.add("highlight");
	  }

	  function unhighlight(e) {
	    preventDefaults(e);
	    dropArea.classList.remove("highlight");
	  }

	  function handleDrop(e) {
	    unhighlight(e);
	    let dt = e.dataTransfer;
	    let files = dt.files;

	    handleFiles(files);

	    const fileList = document.getElementById(fileListId);
	    if (fileList) {
	      fileList.scrollTo({ top: fileList.scrollHeight });
	    }
	  }

	  function handleFiles(files) {
	    files = [...files];
	    // files.forEach(uploadFile);
	    files.forEach(previewFile);
	  }

	  function previewFile(file) {
	    console.log(file);
	    renderFile(file);
	  }

	  function renderFile(file) {
	    let reader = new FileReader();
	    reader.readAsDataURL(file);
	    reader.onloadend = function () {
	      let img = dropArea.getElementsByClassName("preview")[0];
	      img.src = reader.result;
	      img.style.display = "block";
	    };
	  }

	  dropArea.addEventListener("dragenter", highlight, false);
	  dropArea.addEventListener("dragover", highlight, false);
	  dropArea.addEventListener("dragleave", unhighlight, false);
	  dropArea.addEventListener("drop", handleDrop, false);

	  return {
	    handleFiles
	  };
	};
	const dropFile = new DropFile("drop-file", "files");
</script>

</html>
