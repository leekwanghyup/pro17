<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
   
 <c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
 
 <!-- 세션 확인 후 삭제  -->
 <c:if test="${not empty sessionScope.feedback}">
 	<c:set var="feedback" value="${sessionScope.feedback}"/>
 	<c:remove var="feedback" scope="session"/> 
 </c:if>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글목록창</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>

<div class="container">
	<h1>게시글 목록</h1>
	<table class="table table-sm table-bordered">
		<tr>
			<th>글번호</th>
			<th>작성자</th>
			<th>제목</th>
			<th>작성일</th>
		</tr>
		<c:choose>
			<c:when test="${empty articleList}">
				<tr>
					<td colspan="4"><b>등록된 글이 없습니다.</b></td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach var="article" items="${articleList}" varStatus="articleNum">
				<tr>
					<td>${articleNum.count}</td>
					<td>${article.id }</td>
					<td>
						<c:if test="${article.level>1}">
							<c:forEach begin="1" end="${article.level }" step="1">
								<span class="ml-4"></span>
							</c:forEach>
							<a href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}">
								[답변] : ${article.title}
							</a>
						</c:if>
						<c:if test="${article.level eq 1}">
							<a href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}">${article.title}</a>
						</c:if>
					</td>
					<td>
						<fmt:formatDate value="${article.writeDate}" />
					</td>
				</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>
  	<ul class="pagination">
  		<c:if test="${p.prev}">
	    	<li class="page-item"><a class="page-link" href="${p.startPageLink-1}">Previous</a></li>
	    </c:if>
	    <c:forEach var="pageLink" begin="${p.startPageLink}" end="${p.endPageLink}">
	    	<li class="page-item"><a class="page-link" href="${pageLink}">${pageLink}</a></li>
	    </c:forEach>
	    <c:if test="${p.next}">
	    	<li class="page-item"><a class="page-link" href="${p.endPageLink+1}">Next</a></li>
	    </c:if>
  	</ul>
	<div class="text-right">
		<a class="btn btn-sm btn-outline-primary" href="${contextPath}/board/articleForm.do">글쓰기</a>
	</div>
</div>
</body>

<script>
// 새글이 추가 된 경우 피드백 출력 
$(function(){
	// 피드백
	var feedback = "${feedback}";
	if(feedback=='addNewArticle'){
		alert("새로운 글을 작성하였습니다.");
	} else if(feedback=='modArticle'){
		alert("글을 수정하였습니다.");
	} else if(feedback=='delArticle'){
		alert("글을 삭제하였습니다.");
	} else if(feedback=='addReplyarticle'){
		alert("답글을 작성하였습니다.")
	}

	
	var listForm = $('<form/>',{
		action : '${contextPath}/board/listArticles.do'
	})
	
	// 페이지 이동 이벤트 
	$('.pagination a').on('click',function(e){
		e.preventDefault();
		var pageNum = $(this).attr('href'); // 이동할 페이지 번호 
		var pageNumObj = $('<input>',{ // input태그로 pageNum전달
			name : 'pageNum', 
			type : 'hidden', 
			value : pageNum
		}); 
		listForm.append(pageNumObj);
		listForm.appendTo($('body'));
		listForm.submit();
	}); 
});

</script>

</html>