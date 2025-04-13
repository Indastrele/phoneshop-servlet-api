<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>


<tr>
    <td>
        ${label}<span style="color: red;">*</span>
    </td>
    <td>
        <c:set var="error" value="${errors[name]}"/>
        <input name="${name}" value="${param[name]}">
        <c:if test="${not empty error}">
            <span style="color: red;">${error}</span>
        </c:if>
    </td>
</tr>