<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product List">
  <form action="${pageContext.request.contextPath}/products/${product.id}" method="post">
    <tags:cart />

    <c:if test="${not empty param.message and empty error}">
      <p style="color: green;">${param.message}</p>
    </c:if>
    <c:if test="${not empty error}">
      <p style="color: red;">${error}</p>
    </c:if>

    <p>Phone page</p>
    <table>
      <tr>
        <td>Image</td>
        <td>
          <img src="${product.imageUrl}">
        </td>
      </tr>
      <tr>
        <td>Description</td>
        <td>
          <p>${product.description}</p>
        </td>
      </tr>
      <tr>
        <td>Stock</td>
        <td>
          <p>${product.stock}</p>
        </td>
      </tr>
      <tr>
        <td>Code</td>
        <td>
          <p>${product.code}</p>
        </td>
      </tr>
      <tr>
        <td>Price</td>
        <td>
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
      </tr>
      <tr>
        <td>Quantity</td>
        <td>
          <input
                  name="quantity"
                  value="${not empty param.quantity ? param.quantity : 1}">
        </td>
      </tr>
    </table>
    <br>
    <button>Add to cart</button>
  </form>

  <tags:recentlyViewed />
</tags:master>