<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:useBean id="type" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="posts" scope="request" type="java.util.List<org.pknudev.model.Post>"/>
<!DOCTYPE html>
<html>
<head>
  <title>게시글 목록</title>
  <%@ include file="include/head.jsp" %>
</head>
<body>
<jsp:include page="include/menu.jsp">
  <jsp:param name="title" value="${type == 1 ? '분실물' : '습득물'}"/>
</jsp:include>
<div class="container p-3">
  <div class="text-end mb-3">
    <a class="btn btn-primary" href="<c:url value="/writePost"/>"><i class="bi bi-pencil-fill me-1"></i> 게시글 쓰기</a>
  </div>
  <c:set var="posts" value="${posts}" scope="request"/>
  <jsp:include page="include/posts.jsp"/>
</div>
</body>
</html>