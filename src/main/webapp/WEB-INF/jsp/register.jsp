<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:useBean id="emailDomain" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html>
<head>
  <title>회원가입</title>
  <%@ include file="include/head.jsp" %>
</head>
<body>
<jsp:include page="include/menu.jsp">
  <jsp:param name="title" value="회원가입"/>
</jsp:include>
<div class="container p-3">
  <form method="post">
    <div class="input-group mb-2">
      <div class="form-floating">
        <input type="text" class="form-control" name="email" id="email" required>
        <label for="email">이메일</label>
      </div>
      <button class="btn btn-outline-secondary" type="button" id="open-email-verification-button"
              onclick="sendEmailVerificationCode()">
        인증번호 전송
      </button>
    </div>
    <div id="verification-code-wrapper" class="form-floating mb-2 d-none">
      <input type="text" class="form-control" name="verificationCode" id="verification-code" required>
      <label for="verification-code">이메일 인증번호</label>
    </div>
    <div class="form-floating mb-2">
      <input type="password" class="form-control" name="password" id="password" required/>
      <label for="password">비밀번호</label>
    </div>
    <div class="form-floating mb-3">
      <input class="form-control" name="nickname" id="nickname" required/>
      <label for="nickname">닉네임</label>
    </div>
    <div class="d-grid">
      <button id="register-button" class="btn btn-primary" disabled>회원가입</button>
    </div>
  </form>
  <div class="text-center mt-2">
    <small class="text-body-secondary">
      이미 계정이 있으신가요?
      <a href="<c:url value="/login"/>">로그인</a>
    </small>
  </div>
</div>

<script>
  function sendEmailVerificationCode() {
    const email = document.getElementById("email");
    if (!email.value) {
      alert("이메일을 입력해주세요.");
      return;
    }

    email.readOnly = true;
    document.getElementById("open-email-verification-button").disabled = true;
    document.getElementById("register-button").disabled = false;

    const form = new URLSearchParams();
    form.append("email", email.value);
    axios.post("<c:url value="/verifyEmail"/>", form, {
      headers: {"Content-Type": "application/x-www-form-urlencoded"},
    })
    new Promise(resolve => resolve())
      .then(() => document.getElementById("verification-code-wrapper").classList.remove("d-none"))
      .catch(err => {
        email.readOnly = false;
        document.getElementById("open-email-verification-button").disabled = false;

        console.error(err);
        alert("이메일 인증번호 전송에 실패했습니다.");
      });
  }
</script>
</body>
</html>