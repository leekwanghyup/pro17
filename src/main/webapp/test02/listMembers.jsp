<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보리스트</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container">
	<h1>회원정보</h1>
	<table class="table">
		<tr>
			<th>아이디</th>
			<th>비밀번호</th>
			<th>이름</th>
			<th>이메일</th>
			<th>가입일</th>
			<th>수정</th>
			<th>삭제</th>
		</tr>
		<c:choose>
    		<c:when test="${ empty membersList}" >
				<tr>
					<td colspan="6">
						<b>등록된 회원이 없습니다.</b>
					</td>
				</tr>
			</c:when>  
   			<c:when test="${not empty membersList }" >
      			<c:forEach  var="mem" items="${membersList }" >
					<tr>
						<td>${mem.id }</td>
						<td>${mem.pwd }</td>
						<td>${mem.name}</td>
						<td>${mem.email }</td>
						<td>${mem.joinDate}</td>	
						<td><a href="${contextPath}/member/modMemberForm.do?id=${mem.id}">수정</a></td>
						<td><a href="${contextPath}/member/delMember.do?id=${mem.id}">삭제</a></td>	
					</tr>
				</c:forEach>	
			</c:when>
		</c:choose>
	</table>  
 	<a href="${contextPath}/member/memberForm.do">회원 가입하기</a>
</div>
<script>
$(function(){
	var msg = "${msg}";
	if(msg=='addMember'){
		alert('회원을 등록했습니다.');
	} else if(msg=='modified'){
		alert('회원 정보를 수정했습니다.');
	} else if(msg=='deleted'){
		alert('회원 정보를 삭제했습니다.');
	}
});
</script>
</body>
</html>