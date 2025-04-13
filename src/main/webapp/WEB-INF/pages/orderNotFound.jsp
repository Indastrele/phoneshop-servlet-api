<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Order not found">
    <h1>Order not found</h1>
    <h2>${ pageContext.request.getAttribute("jakarta.servlet.error.exception").getMessage() }</h2>
</tags:master>