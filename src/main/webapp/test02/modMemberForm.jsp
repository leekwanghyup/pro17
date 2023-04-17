<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="col-6 mx-auto">
			<h1 class="text-center">회원정보수정</h1>
			<form action="${contextPath}/member/modMember.do" method="post">
				<table class="table">
					<tr>
						<td>아아디</td>
						<td><input type="text" class="form-control" name="id" value="${memInfo.id}" readonly></td>
					</tr>
					<tr>
						<td>비밀번호</td>
						<td><input type="text" class="form-control" name="pwd" value="${memInfo.pwd }"></td>
					</tr>
					<tr>
						<td>이름</td>
						<td><input type="text" class="form-control" name="name" value="${memInfo.name }" ></td>
					</tr>
					<tr>
						<td>이메일</td>
						<td><input type="text" class="form-control" name="email" value="${memInfo.email }"></td>
					</tr>								
				</table>
				<div class="text-right pr-3">
					<button class="btn btn-outline-primary">수정하기</button>
				</div>
			</form>
		</div>
	</div>
</div>
</body>
</html>