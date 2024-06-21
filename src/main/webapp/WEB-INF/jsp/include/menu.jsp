<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="offcanvas offcanvas-start" data-bs-scroll="true" tabindex="-1" id="menu">
  <div class="offcanvas-header">
    <h2 class="offcanvas-title fs-4">메뉴</h2>
    <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
  </div>
  <div class="offcanvas-body">
    <c:if test="${sessionScope.get('user') != null}">
      <div class="mb-3 text-body-secondary lead">
        <strong>${sessionScope.user.nickname}</strong><span class="text-body-secondary">님, 안녕하세요!</span>
      </div>
    </c:if>
    <ul class="list-unstyled">
      <li class="mb-3">
        <h3 class="fs-4 m-0">
          <a href="<c:url value="/"/>" class="text-dark text-decoration-none">홈</a>
        </h3>
      </li>
      <li class="mb-3">
        <h3 class="fs-4 m-0">
          <a href="<c:url value="/posts?type=1"/>" class="text-dark text-decoration-none">분실물</a>
        </h3>
      </li>
      <li class="mb-3">
        <h3 class="fs-4 m-0">
          <a href="<c:url value="/posts?type=2"/>" class="text-dark text-decoration-none">습득물</a>
        </h3>
      </li>
      <c:if test="${sessionScope.get('user') != null}">
        <li class="mb-3">
          <h3 class="fs-4 m-0">
            <a href="<c:url value="/writePost"/>" class="text-dark text-decoration-none">게시물 쓰기</a>
          </h3>
        </li>
      </c:if>
      <li class="mb-3">
        <h3 class="fs-4 m-0">
          <c:choose>
            <c:when test="${sessionScope.get('user') == null}">
              <a class="text-dark text-decoration-none" href="<c:url value="/login"/>">로그인</a>
            </c:when>
            <c:otherwise>
              <a class="text-dark text-decoration-none" href="<c:url value="/logout"/>">로그아웃</a>
            </c:otherwise>
          </c:choose>
        </h3>
      </li>
    </ul>
  </div>
</div>
<div class="position-relative d-flex align-items-center shadow-sm" style="height: 3rem">
  <button class="btn border-0 fs-4" type="button" data-bs-toggle="offcanvas" data-bs-target="#menu">
    <i class="bi bi-list"></i>
  </button>
  <h1 class="position-absolute fs-5 m-0 start-50 translate-middle-x">${param.title}</h1>
</div>
