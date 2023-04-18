<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글상세창</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<style>
	th{text-align: center; background: #e6edf7;}
</style>
</head>
<body>

<div class="container">
	<div class="jumbotron">
		<h1 class="text-center">글 상세 내용</h1>
	</div>
	<form id="frmArticle" enctype="multipart/form-data">
	<table class="table table-bordered">
			<tr>
				<th class="col-1">글번호</td>
				<td class="col-1">
					${article.articleNO }
				</td>
				<th class="col-1">
					작성자
				</th>
				<td>${article.id}</td>
				<th class="col-1">등록일자</th>
				<td>${article.writeDate}</td>
				<th class="col-1">조회수</th>
				<td>100</td>
			</tr>
			<tr>
				<th>제목</th>
				<td colspan="7" class="article-title">${article.title}</td>
			</tr>		
			<tr>
				<th>내용</th>
				<td colspan="7">
					<div style="min-height: 365px;" class="article-content">
						${article.content}
					</div>
				</td>
			</tr>
			<tr>
				<th>이미지<br>파일</th>
				<td td colspan="7" class="artilce-imageFile">
					<c:if test="${empty article.imageFileName }">
						<p>첨부된 이미지 파일이 없습니다.</p>
					</c:if>
					<c:if test="${not empty article.imageFileName }">
						<div>
							<input type="hidden" name="originalFileName" value="${article.imageFileName}"/>
							<c:url var="imgFile" value="/download.do?articleNO=${article.articleNO}&imageFileName=${article.imageFileName}"/>
							<img style="height: 200px" src="${imgFile}" class="imgFile"><br>
							<a href="${imgFile}">이미지 파일 다운로드</a>
						</div>
					</c:if>
				</td>
			</tr>			
		</table>
		<div class="text-right boardBtnBox">
			<button type="button" class="btn btn-outline-success reply">답글쓰기</button>
			<button type="button" class="btn btn-outline-info mod">수정</button>
			<button type="button" class="btn btn-outline-danger del">삭제</button>
			<button type="button" class="btn btn-outline-primary list">글목록으로</button>
		</div>
		<div class="text-right modBtn">
			<button type="button" class="btn btn-outline-info modify">수정</button>
			<button type="button" class="btn btn-outline-info backView">취소</button>
		</div>
	</form>
	<div class="frmArticle-form-data">
		<input type="hidden" name="articleNO" value="${article.articleNO }">
		<input type="hidden" name="id" value="${article.id }">
		<input type="hidden" name="title" value="${article.title }">
		<input type="hidden" name="content" value="${article.content }">
	</div>
</div>
<script>
function readURL(input) {
	var imgFileURL = "${imgFile}"; // 글에 첨부된 이미지 파일 주소 
    if (input.files && input.files[0]) {
	      var reader = new FileReader();
	      reader.onload = function (e) {
	    	  if(imgFileURL){ // 첨부파일 있는 경우 
	  	        $('.imgFile').attr('src', e.target.result);
	    	  } else { // 첨부파일이 없는 경우
	    		  var imgTag = '<img style="height: 200px" src="'+e.target.result+'" class="imgFile">';
	    		  $('.artilce-imageFile p').remove();
	    		  $('.artilce-imageFile').append(imgTag );
	    	  }
        }
    reader.readAsDataURL(input.files[0]);
    } else {
    	 if(imgFileURL){ // 첨부파일 있는 경우 
    		 $('.imgFile').attr('src', '${imgFile}'); // 첨부 전 파일로 되돌림	 
    	 }  else { // 첨부파일이 없는 경우
    		 $('.imgFile').remove();
    	 	
    		 if($('.artilce-imageFile p').length===0){ // p태그가 존재하지 않으면 
    		 	$('.artilce-imageFile').append('<p>첨부된 이미지 파일이 없습니다.</p>'); 
    		 }
    	 }
    }
}

$(function(){
	var frmArticle = $('#frmArticle');
	// 글목록으로 
	$(".boardBtnBox .list").on('click',function(){
		frmArticle.attr('action','${contextPath}/board/listArticles.do')
				  .attr('method','get')
				  .submit();		  
	})

	// 글수정 모드 
	$('.modBtn').hide();
	var title = $('[name="title"]').val(); // 제목 저장
	var content = $('[name="content"]').val(); // 내용 저장
	var fileDownloadBtn =$('.artilce-imageFile a'); // 이미지 다운 링크 저장
	var notExistFile = $('artilce-imageFile p'); 
	
	$(".boardBtnBox .mod").on('click',function(){
		$('.article-title').html($('<input/>',{
			name : 'title',
			value : '${article.title }',
			class :'form-control'
		}));
		$('.article-content').html($('<textarea/>',{
			name : 'content',
			rows : '15', 
			class :'form-control'
		}).html('${article.content}'));
		// onchange="readURL(this);
		$('.artilce-imageFile').prepend($('<input/>',{
			type : 'file', 
			name : 'imageFileName',
			class :'form-control my-3', 
			onchange : 'readURL(this)'
		}));
		$('.artilce-imageFile a').remove();
		$('.boardBtnBox').hide(); // 뷰모드 버튼 숨김
		$('.modBtn').show(); // 수정모드 버튼 보임
	})
	
	// 뷰모드로 돌아가기
	$('.modBtn .backView').on('click',function(){ 
		$('.modBtn').hide(); // 수정모드 버튼 숨김
		$('.boardBtnBox').show(); // 부모드 버튼 보임
		$('.article-title').html(title); // 뷰모드 제목
		$('.article-content').html(content); // 뷰모드 내용
		$('[name="imageFileName"]').remove(); // 파일첨부 태그 삭제 
		$('.artilce-imageFile').append(fileDownloadBtn); // 이미지 다운 링크 보임
		readURL($('[name="imageFileName"]')); // 첨부파일 원상태로 되돌림
	})
	
	// 수정처리
	$('.modBtn .modify').on('click',function(){
		frmArticle.attr('method','post')
				.attr('action','${contextPath}/board/modArticle.do')
				.append($('[name="articleNO"]'))
				.submit();
	})
	
	// 삭제처리 
	$('.boardBtnBox .del').on('click',function(){
		frmArticle.attr('method','post')
			.attr('action','${contextPath}/board/removeArticle.do')
			.attr('enctype','utf-8') // 변경하지 않으면 request.getParameter()메소드를 사용할 수 없다.
			.append($('[name="articleNO"]'))
			.submit();
	}); 
	
	// 답글 쓰기 페이지로 이동
	$('.boardBtnBox .reply').on('click',function(){
		frmArticle.attr('method','get')
			.attr('action','${contextPath}/board/replyForm.do')
			.attr('enctype','utf-8')
			.append($('[name="articleNO"]'))
			.submit();
	});
});
</script>
</body>
</html>