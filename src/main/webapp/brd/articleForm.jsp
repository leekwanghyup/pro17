<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
   function readURL(input) {
      if (input.files && input.files[0]) {
	      var reader = new FileReader();
	      reader.onload = function (e) {
	        $('#preview').attr('src', e.target.result);
          }
         reader.readAsDataURL(input.files[0]);
      } else {
    	  $('#preview').attr('src', '');
      }
  }  
  function backToList(obj){
    obj.action="${contextPath}/board/listArticles.do";
    obj.submit();
  }
</script>
</head>
<body>
<div class="container">
	<div class="jumbotron">
		<h1 class="text-center">새글 쓰기</h1>
	</div>
	<form name="articleForm" method="post" action="${contextPath}/board/addArticle.do" enctype="multipart/form-data">
		<table class="table table-bordered">
			<tr>
				<th class="col-2">글제목</th>
				<td>
					<input type="text" name="title" class="form-control">
				</td>
			</tr>
			<tr>
				<th>글내용</th>
				<td>
					<textarea  rows="15" name="content" class="form-control"></textarea>
				</td>
			</tr>
			<tr>
				<th>이미지파일첨부</th>
				<td>
					<input type="file" name="imageFileName" class="form-control" onchange="readURL(this);">
					<div style="height: 200px">
						<img style="height:100%" id="preview" >
					</div>
				</td>
			</tr>
			<tr>
				<td></td>
				<td colspan="2">
					<button>글쓰기</button>
					<button type="button" onclick="backToList(this.form)">글목록으로</button>
				</td>
			</tr>
		</table>
	</form>
</div> 
</body>
</html>