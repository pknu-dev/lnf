<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" uri="/WEB-INF/tags.tld" %>
<jsp:useBean id="posts" scope="request" type="java.util.List<org.pknudev.model.Post>"/>
<c:if test="${empty posts}">
  <div class="text-body-secondary text-center lead">
    아직 게시글이 없습니다.
  </div>
</c:if>
<div class="row row-cols-1 row-cols-md-3 row-cols-xl-5 g-4">
  <c:forEach var="post" items="${posts}">
    <div class="col">
      <div class="card <c:if test="${post.done}">text-bg-light</c:if> h-100">
        <c:choose>
          <c:when test="${not empty post.attachments}">
            <img src="<c:url value="/attachment?id=${post.attachments[0].id}" />" class="card-img-top object-fit-cover"
                 style="height: 10rem"/>
          </c:when>
          <c:otherwise>
            <div class="card-img-top bg-dark-subtle bg-gradient" style="height: 10rem"></div>
          </c:otherwise>
        </c:choose>
        <div class="card-body">
          <h5 class="card-title fs-6 d-flex align-items-center gap-1">
            <c:choose>
              <c:when test="${post.type == 1}">
                <span class="badge rounded-pill text-bg-danger fw-normal">분실물</span>
              </c:when>
              <c:when test="${post.type == 2}">
                <span class="badge rounded-pill text-bg-warning fw-normal">습득물</span>
              </c:when>
            </c:choose>
            <span>${post.itemName}</span>
            <c:if test="${post.done}">
              <i class="bi bi-check-circle text-success"></i>
            </c:if>
          </h5>
          <div class="card-text text-body-secondary">
            <small>${post.itemCategory.name} / ${post.itemName}</small>
            <div class="text-truncate">${post.content}</div>
          </div>
          <a href="<c:url value="/post?id=${post.id}"/>" class="stretched-link m-0 p-0"></a>
        </div>
        <div class="card-footer">
          <small class="text-body-secondary">
            <span>
            <tags:prettytime date="${post.createdAt}"/>
            </span>
            <c:if test="${post.numComments > 0}">
              <span class="text-body-tertiary">(댓글: ${post.numComments})</span>
            </c:if>
          </small>
        </div>
      </div>
    </div>
  </c:forEach>
</div>
