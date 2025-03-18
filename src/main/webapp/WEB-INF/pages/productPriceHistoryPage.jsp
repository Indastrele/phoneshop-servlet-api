<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Phone page
  </p>
  <table>
    <tr>
      <td>
        Image
      </td>
      <td>
        <img src="${product.imageUrl}">
      </td>
    </tr>
    <tr>
      <td>
        Description
      </td>
      <td>
        <p>${product.description}</p>
      </td>
    </tr>
    <c:forEach var="value" items="${product.priceHistory}">
      <tr>
        <td>
          <p>${value.date.dayOfMonth}.${value.date.monthValue}.${value.date.year}</p>
        </td>
        <td>
          <fmt:formatNumber value="${value.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>