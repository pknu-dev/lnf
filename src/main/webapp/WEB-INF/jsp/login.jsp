<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <title>로그인</title>
  <%@ include file="include/head.jsp" %>
</head>
<body>
<jsp:include page="include/menu.jsp">
  <jsp:param name="title" value="로그인"/>
</jsp:include>
<div class="container p-3">
  <form method="post">
    <div class="form-floating mb-2">
      <input type="text" class="form-control" name="email" id="email" required autofocus>
      <label for="email">이메일</label>
    </div>
    <div class="form-floating mb-3">
      <input type="password" class="form-control" name="password" id="password" required/>
      <label for="password">비밀번호</label>
    </div>
    <div class="d-grid">
      <button class="btn btn-primary">로그인</button>
    </div>
  </form>
  <div class="text-center mt-2">
    <small class="text-body-secondary">
      아직 회원이 아니신가요?
      <a href="<c:url value="/register"/>">회원가입</a>
    </small>
  </div>
</div>
</body>
</html>