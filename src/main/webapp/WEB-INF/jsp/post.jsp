<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" uri="/WEB-INF/tags.tld" %>
<jsp:useBean id="post" scope="request" type="org.pknudev.model.Post"/>
<jsp:useBean id="comments" scope="request" type="java.util.List<org.pknudev.model.Comment>"/>
<jsp:useBean id="attachments" scope="request" type="java.util.List<org.pknudev.model.Attachment>"/>
<jsp:useBean id="showComments" scope="request" type="java.lang.Boolean"/>
<html>
<head>
  <title>${post.itemName} - ${post.type == 1 ? "분실물" : "습득물"}</title>
  <%@ include file="include/head.jsp" %>
</head>
<body>
<jsp:include page="include/menu.jsp">
  <jsp:param name="title" value="글 보기"/>
</jsp:include>
<div class="container p-3">
  <c:if test="${sessionScope.get('user') != null && post.authorEmail == sessionScope.user.email}">
    <div class="text-end mb-3">
      <div class="btn-group btn-group-sm" role="group" aria-label="Basic example">
        <c:if test="${!post.done}">
          <button type="button" class="btn btn-outline-success" onclick="markDone()">완료</button>
        </c:if>
        <a class="btn btn-outline-dark" href="<c:url value="/updatePost?id=${post.id}"/>">수정</a>
        <button type="button" class="btn btn-outline-danger" onclick="deletePost()">삭제</button>
      </div>
    </div>
  </c:if>
  <table class="table table-bordered mb-3">
    <tbody>
    <tr>
      <th class="col-3 fw-normal bg-light">상태</th>
      <td class="bg-white">
        <c:choose>
          <c:when test="${post.done}">
            <span class="text-success">완료</span>
          </c:when>
          <c:otherwise>
            <span class="text-secondary">진행 중</span>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <th class="col-3 fw-normal bg-light">작성자</th>
      <td class="bg-white">${post.author.nickname}</td>
    </tr>
    <tr>
      <th class="col-3 fw-normal bg-light">작성일</th>
      <td class="bg-white">
        <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${post.createdAt}"/>
      </td>
    </tr>
    <c:if test="${post.itemCategoryId != null}">
      <tr>
        <th class="col-3 fw-normal bg-light">카테고리</th>
        <td class="bg-white">${post.itemCategory.name}</td>
      </tr>
    </c:if>
    <c:if test="${post.itemName != null}">
      <tr>
        <th class="col-3 fw-normal bg-light">물품명</th>
        <td class="bg-white">${post.itemName}</td>
      </tr>
    </c:if>
    <c:if test="${post.itemLocation != null}">
      <tr>
        <th class="col-3 fw-normal bg-light">물품 위치</th>
        <td class="bg-white">${post.itemLocation}</td>
      </tr>
    </c:if>
    <c:if test="${post.itemDate != null}">
      <tr>
        <th class="col-3 fw-normal bg-light">분실/습득 일시</th>
        <td class="bg-white">
          <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${post.itemDate}"/>
        </td>
      </tr>
    </c:if>
    <tr>
      <th class="col-3 fw-normal bg-light">내용</th>
      <td class="bg-white" style="white-space: pre-line">${post.content}</td>
    </tr>
    <c:if test="${attachments.size() > 0}">
      <tr>
        <th class="col-3 fw-normal bg-light">첨부파일</th>
        <td class="bg-white">
          <c:forEach var="attachment" items="${attachments}">
            <a href="<c:url value="/attachment?id=${attachment.id}"/>">
              <img src="<c:url value="/attachment?id=${attachment.id}"/>" class="img-thumbnail object-fit-cover"
                   style="width: 100px; height: 100px"/>
            </a>
          </c:forEach>
        </td>
      </tr>
    </c:if>
    </tbody>
  </table>

  <h2 class="fs-5">댓글</h2>
  <c:choose>
    <c:when test="${showComments}">
      <div class="mb-3 border-top">
        <c:forEach var="comment" items="${comments}">
          <c:choose>
            <c:when test="${!comment.visible()}">
              <%-- do not show the comment --%>
            </c:when>
            <c:otherwise>
              <div class="py-2 border-bottom">
                <c:choose>
                  <c:when test="${comment.deletedAt != null}">
                    <small class="text-body-secondary">삭제된 댓글입니다.</small>
                  </c:when>
                  <c:otherwise>
                    <div class="mb-1">${comment.content}</div>
                    <div class="d-flex justify-content-between align-items-center text-body-secondary">
                      <div>
                        <small class="me-1">${comment.author.nickname}</small>
                        <small class="me-1"><tags:prettytime date="${comment.createdAt}"/></small>
                      </div>
                      <div>
                        <c:if test="${sessionScope.get('user') != null}">
                          <small onclick="toggleReplyInput(this, ${comment.id})" style="cursor: pointer">대댓글</small>
                        </c:if>
                        <c:if
                          test="${sessionScope.get('user') != null && sessionScope.user.email == comment.authorEmail}">
                          <small onclick="deleteComment(${comment.id})" style="cursor: pointer">삭제</small>
                        </c:if>
                      </div>
                    </div>
                  </c:otherwise>
                </c:choose>
                <div class="ps-4"> <!-- for children -->
                  <c:forEach var="child" items="${comment.children}">
                    <c:if test="${child.deletedAt == null}">
                      <div class="mt-1 border bg-light p-2">
                        <div class="mb-1">${child.content}</div>
                        <div class="d-flex justify-content-between align-items-center text-body-secondary">
                          <div>
                            <small class="me-1">${child.author.nickname}</small>
                            <small class="me-1"><tags:prettytime date="${child.createdAt}"/></small>
                          </div>
                          <div>
                            <c:if
                              test="${sessionScope.get('user') != null && sessionScope.user.email == child.authorEmail}">
                              <small>
                                <span onclick="deleteComment(${child.id})" style="cursor: pointer">삭제</span>
                              </small>
                            </c:if>
                          </div>
                        </div>
                      </div>
                    </c:if>
                  </c:forEach>
                </div>
              </div>
            </c:otherwise>
          </c:choose>
        </c:forEach>
      </div>
    </c:when>
    <c:otherwise>
      <p class="text-body-secondary">아직 댓글이 없습니다.</p>
    </c:otherwise>
  </c:choose>

  <c:if test="${sessionScope.get('user') != null}">
    <form method="post" action="<c:url value="/writeComment"/>">
      <input type="hidden" name="post_id" value="${post.id}"/>
      <input class="form-control" name="content" placeholder="댓글 내용을 입력하세요." autofocus required/>
    </form>
  </c:if>
</div>

<script>
  function toggleReplyInput(el, commentId) {
    const commentDiv = el.parentElement.parentElement.parentElement;

    if (commentDiv.children.length > 3) { // already opened
      // remove
      commentDiv.lastChild.remove();
      return;
    }

    const replyInput = document.createElement("form");
    replyInput.className = "ms-4 mt-2";

    const content = document.createElement("input");
    content.className = "form-control";
    content.placeholder = "대댓글 내용을 입력하세요.";
    replyInput.appendChild(content);

    replyInput.onsubmit = e => {
      e.preventDefault();

      const form = new URLSearchParams();
      form.append("post_id", "${post.id}");
      form.append("parent_id", commentId);
      form.append("content", content.value);

      axios.post("<c:url value="/writeComment"/>", form, {
        headers: {"Content-Type": "application/x-www-form-urlencoded"},
      })
        .then(() => location.reload())
        .catch(err => alert(err));
    };

    commentDiv.appendChild(replyInput);
    content.focus();
  }

  function deleteComment(id) {
    if (!confirm("댓글을 삭제하시겠습니까?")) {
      return;
    }
    const form = new URLSearchParams();
    form.append("id", id);
    axios.post("<c:url value="/deleteComment"/>", form, {
      headers: {"Content-Type": "application/x-www-form-urlencoded"},
    })
      .then(() => location.reload())
      .catch(err => alert(err));
  }

  function deletePost() {
    if (!confirm("게시글을 삭제하시겠습니까?")) {
      return;
    }
    axios.post("<c:url value="/deletePost?id=${post.id}"/>")
      .then(() => {
        location.href = "<c:url value="/posts?type=${post.type}"/>";
      })
      .catch(err => alert(err));
  }

  function markDone() {
    if (!confirm("게시글을 완료 표시하시겠습니까?")) {
      return;
    }
    axios.post("<c:url value="/updatePostDone?id=${post.id}"/>")
      .then(() => location.reload())
      .catch(err => alert(err));
  }
</script>
</body>
</html>
