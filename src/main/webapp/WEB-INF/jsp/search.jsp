<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:useBean id="type" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="query" scope="request" type="java.lang.String"/>
<jsp:useBean id="posts" scope="request" type="java.util.List<org.pknudev.model.Post>"/>
<!DOCTYPE html>
<html>
<head>
  <title>게시글 검색</title>
  <%@ include file="include/head.jsp" %>
</head>
<body>
<jsp:include page="include/menu.jsp">
  <jsp:param name="title" value="검색"/>
</jsp:include>
<div class="container p-3">
  <form method="post" class="mb-4">
    <div class="row g-2">
      <div class="col-sm-4 col-lg-2">
        <select class="form-select" name="type" required>
          <option value="0" <c:if test="${type == 0}">selected</c:if>>전체</option>
          <option value="1" <c:if test="${type == 1}">selected</c:if>>분실물</option>
          <option value="2" <c:if test="${type == 2}">selected</c:if>>습득물</option>
        </select>
      </div>
      <div class="col-sm-8 col-lg-10">
        <input class="form-control col" name="query" placeholder="물품 이름, 장소, 게시글 내용에서 검색" required value="${query}"/>
      </div>
    </div>
  </form>

  <h2 class="fs-5 mb-2">검색 결과</h2>
  <c:set var="posts" value="${posts}" scope="request"/>
  <jsp:include page="include/posts.jsp"/>
</div>
</body>
</html>