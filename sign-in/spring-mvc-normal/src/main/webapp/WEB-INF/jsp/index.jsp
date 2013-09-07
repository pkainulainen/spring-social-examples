<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="page-header">
    <h1><spring:message code="label.homepage.title"/></h1>
</div>
<div>
    <p><spring:message code="text.homepage.greeting"/> <sec:authentication property="principal.username"/></p>
</div>
</body>
</html>