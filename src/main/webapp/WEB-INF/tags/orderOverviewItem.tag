<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="value" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>


<tr>
    <td>
        ${label}
    </td>
    <td>
        <input name="${name}" value="${value}" readonly="readonly">
    </td>
</tr>