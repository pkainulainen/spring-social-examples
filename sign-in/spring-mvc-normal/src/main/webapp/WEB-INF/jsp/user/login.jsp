<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="page-header">
    <h1><spring:message code="label.user.login.page.title"/></h1>
</div>
<sec:authorize access="isAnonymous()">
    <div class="panel panel-default">
        <div class="panel-body">
            <h2><spring:message code="label.login.form.title"/></h2>
            <form action="/login/authenticate" method="POST" role="form">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="row">
                    <div id="form-group-email" class="form-group col-lg-4">
                        <label class="control-label" for="user-email"><spring:message code="label.user.email"/>:</label>
                        <input id="user-email" name="username" type="text" class="form-control"/>
                    </div>
                </div>

                <div class="row">
                    <div id="form-group-password" class="form-group col-lg-4">
                        <label class="control-label" for="user-password"><spring:message code="label.user.password"/>:</label>
                        <input id="user-password" name="password" type="password" class="form-control"/>
                    </div>
                </div>
                <button type="submit" class="btn btn-default"><spring:message code="label.user.login.submit.button"/></button>
            </form>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-body">
            <h2><spring:message code="label.social.sign.in.title"/></h2>
            <a href="<c:url value="/auth/twitter"/>"><img src="${pageContext.request.contextPath}/static/images/sign-in-with-twitter-gray.png"/></a>
        </div>
    </div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
    <p><spring:message code="text.login.page.authenticated.user.help"/></p>
</sec:authorize>
</body>
</html>