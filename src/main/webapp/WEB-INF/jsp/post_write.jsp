<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:useBean id="categories" scope="request" type="java.util.List<org.pknudev.model.Category>"/>
<html>
<head>
  <title>게시글 쓰기</title>
  <%@ include file="include/head.jsp" %>
</head>
<body>
<jsp:include page="include/menu.jsp">
  <jsp:param name="title" value="게시글 쓰기"/>
</jsp:include>
<div class="container p-3">
  <form method="post" enctype="multipart/form-data">
    <div class="form-floating mb-2">
      <select class="form-select" id="type" name="type" required>
        <option value="1" selected>분실물</option>
        <option value="2">습득물</option>
      </select>
      <label for="type">게시판</label>
    </div>
    <div class="form-floating mb-2">
      <select class="form-select" id="item-category" name="item_category_id" required>
        <option selected value="">카테고리 선택</option>
        <c:forEach var="category" items="${categories}">
          <option value="${category.id}">${category.name}</option>
        </c:forEach>
      </select>
      <label for="item-category">카테고리</label>
    </div>
    <div class="form-floating mb-2">
      <input type="text" class="form-control" id="item-name" name="item_name" required/>
      <label for="item-name">물품명</label>
    </div>
    <div class="form-floating mb-2">
      <input type="text" class="form-control" id="item-location" name="item_location" required/>
      <label for="item-location">물품 위치</label>
    </div>
    <div class="form-floating mb-2">
      <input type="datetime-local" class="form-control" id="item-date" name="item_date" required/>
      <label for="item-date">분실/습득 일시</label>
    </div>
    <div class="d-flex mb-2">
      <label for="file" class="form-label"></label>
      <input class="form-control" type="file" name="file" id="file">
    </div>
    <div class="form-floating">
      <textarea class="form-control" id="content" style="height: 10rem" name="content" required></textarea>
      <label for="content">글 내용</label>
    </div>
    <div class="d-grid">
      <button type="submit" class="btn btn-primary mt-3">완료</button>
    </div>
  </form>
</div>
</body>
</html>
