<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>
<tags:master pageTitle="Product List">
  <tags:cart />

  <p>
    Welcome to Expert-Soft training!
  </p>
  <c:set var="message" value="${param.message}"/>
  <c:if test="${not empty message}">
    <p style="color: green;">${param.message}</p>
  </c:if>
  <c:set var="errorMessage" value="${param.errorMessage}"/>
  <c:if test="${not empty errorMessage}">
  <p style="color: red;">${param.errorMessage}</p>
</c:if>
  <form>
    <input name="query" type="text" value="${param.query}">
    <button>Search</button>
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
          <tags:sortLink sort="description" order="asc" symbol="&#5123"/>
          <tags:sortLink sort="description" order="desc" symbol="&#5121"/>
        </td>
        <td>
          Quantity
        </td>
        <td class="price">
          Price
          <tags:sortLink sort="price" order="asc" symbol="&#5123"/>
          <tags:sortLink sort="price" order="desc" symbol="&#5121"/>
        </td>
        <td></td>
      </tr>
    </thead>

    <c:forEach var="product" items="${products}" varStatus="status">
      <form action="${pageContext.request.contextPath}/products/addItems/${product.id}" method="post">
        <tr>
          <td>
            <img class="product-tile" src="${product.imageUrl}">
          </td>
          <td>
            <a href="${pageContext.request.contextPath}/products/${product.id}">
                ${product.description}
            </a>
          </td>
          <td>
            <span>
              <c:set var="quantityString" value="${paramValues['quantity'][status.index]}"/>
              <input name="quantity"
                     value="1">
            </span>
          </td>
          <td class="price">
            <a href="${pageContext.request.contextPath}/products/history/${product.id}">
              <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </a>
          </td>
          <td>
            <button>Add to cart</button>
          </td>
        </tr>
      </form>
    </c:forEach>

  </table>

  <p>
    <tags:recentlyViewed />
  </p>
</tags:master>