<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>
<tags:master pageTitle="Advanced search">
    <h1>Advanced search</h1>
    <div>
        <form action="${pageContext.request.contextPath}/products/search" method="get">
            <span style="align-content: flex-start;">
                <label>
                    Description
                    <input type="text" name="description">
                    <select name="descriptionSearchType">
                        <option>all words</option>
                        <option>any words</option>
                    </select>
                </label>
            </span>
            <br>
            <span style="align-content: flex-start;">
                <c:set var="minPriceError" value="${errors['minPrice']}"/>
                <label>
                    Min price
                    <input type="text" name="minPrice" value="${param.minPrice}">
                </label>
                <c:if test="${not empty minPriceError}">
                    <span style="color: red;">${minPriceError}</span>
                </c:if>
            </span>
            <br>
            <span style="align-content: flex-start;">
                <c:set var="maxPriceError" value="${errors['maxPrice']}"/>
                <label>
                    Max price
                    <input type="text" name="maxPrice" value="${param.maxPrice}">
                </label>
                <c:if test="${not empty maxPriceError}">
                    <span style="color: red;">${maxPriceError}</span>
                </c:if>
            </span>
            <br>
            <button>Search</button>
        </form>
        <br>
        <br>
        <table>
            <thead style="background-color: lightgray">
                <tr>
                    <td>Image</td>
                    <td>Description</td>
                    <td>Price</td>
                </tr>
            </thead>
            <c:forEach var="product" items="${products}" varStatus="status">
                    <tr>
                        <td>
                            <img class="product-tile" src="${product.imageUrl}">
                        </td>
                        <td>
                            ${product.description}
                        </td>
                        <td class="price">
                            <a href="${pageContext.request.contextPath}/products/history/${product.id}">
                                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                            </a>
                        </td>
                    </tr>
            </c:forEach>
        </table>
    </div>
</tags:master>