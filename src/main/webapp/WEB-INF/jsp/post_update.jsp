<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:useBean id="categories" scope="request" type="java.util.List<org.pknudev.model.Category>"/>
<jsp:useBean id="post" scope="request" type="org.pknudev.model.Post"/>
<jsp:useBean id="attachments" scope="request" type="java.util.List<org.pknudev.model.Attachment>"/>
<html>
<head>
  <title>게시글 수정</title>
  <%@ include file="include/head.jsp" %>
</head>
<body>
<jsp:include page="include/menu.jsp">
  <jsp:param name="title" value="게시글 수정"/>
</jsp:include>
<div class="container p-3">
  <form method="post" enctype="multipart/form-data">
    <div class="form-floating mb-2">
      <select class="form-select" id="type" name="type" required disabled>
        <option value="1" <c:if test="${post.type == 1}">selected</c:if>>분실물</option>
        <option value="2" <c:if test="${post.type == 2}">selected</c:if>>습득물</option>
      </select>
      <label for="type">게시판</label>
    </div>
    <div class="form-floating mb-2">
      <select class="form-select" id="item-category" name="item_category_id">
        <option
          <c:if test="${post.itemCategoryId == null}">selected</c:if> value="">카테고리 선택
        </option>
        <c:forEach var="category" items="${categories}">
          <option
            <c:if test="${post.itemCategoryId == category.id}">selected</c:if>
            value="${category.id}">
              ${category.name}
          </option>
        </c:forEach>
      </select>
      <label for="item-category">카테고리</label>
    </div>
    <div class="form-floating mb-2">
      <input type="text" class="form-control" id="item-name" name="item_name" value="${post.itemName}"/>
      <label for="item-name">물품명</label>
    </div>
    <div class="form-floating mb-2">
      <input type="text" class="form-control" id="item-location" name="item_location" value="${post.itemLocation}"/>
      <label for="item-location">물품 위치</label>
    </div>
    <div class="form-floating mb-2">
      <input type="datetime-local" class="form-control" id="item-date" name="item_date" value="${post.itemDate}"/>
      <label for="item-date">분실/습득 일시</label>
    </div>
    <%--    <div class="mb-3">--%>
    <%--      <label for="file" class="form-label"></label>--%>
    <%--      <input class="form-control" type="file" name="file" id="file">--%>
    <%--    </div>--%>
    <c:if test="${attachments.size() > 0}">
      <div class="mb-2">
        <div class="text-secondary mb-1">첨부파일은 수정이 불가능합니다.</div>
        <c:forEach var="attachment" items="${attachments}">
          <img src="<c:url value="/attachment?id=${attachment.id}"/>" class="img-thumbnail"
               style="width: 100px; height: 100px"/>
        </c:forEach>
      </div>
    </c:if>
    <div class="form-floating">
      <textarea class="form-control" id="content" style="height: 10rem" name="content"
                required>${post.content}</textarea>
      <label for="content">글 내용</label>
    </div>
    <div class="d-grid">
      <button type="submit" class="btn btn-primary mt-3">완료</button>
    </div>
  </form>
</div>
</body>
</html>
