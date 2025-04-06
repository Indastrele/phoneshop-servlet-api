<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <c:set var="message" value="${param.message}"/>
    <c:if test="${not empty message}">
        <p style="color: green;">${param.message}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/cart" method="post">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td>
                    Quantity
                </td>
                <td class="price">
                    Price
                </td>
                <td></td>
            </tr>
            </thead>
            <c:forEach var="item" items="${cart.cart}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/products/${item.product.id}">
                                ${item.product.description}
                        </a>
                    </td>
                    <td>
                        <span>
                            <fmt:formatNumber var="quantity" value="${item.quantity}"/>
                            <c:set var="quantityString" value="${paramValues['quantity'][status.index]}"/>
                            <input name="quantity"
                                    value="${not empty quantityString ? quantityString : quantity}">

                            <c:if test="${not empty errors}">
                                <p style="color: red;">${errors[item.product.id]}</p>
                            </c:if>
                            <input name="productId" type="hidden" value="${item.product.id}">
                        </span>
                    </td>
                    <td class="price">
                        <a href="${pageContext.request.contextPath}/products/history/${item.product.id}">
                            <fmt:formatNumber
                                    value="${item.product.price}"
                                    type="currency"
                                    currencySymbol="${item.product.currency.symbol}"/>
                        </a>
                    </td>
                    <td>
                        <button form="deleteCartItem"
                                formaction="${pageContext.request.contextPath}/cart/deleteItem/${item.product.id}">
                            Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <p>
            <button>Update</button>
        </p>
    </form>
    <form id="deleteCartItem" method="post">
    </form>
</tags:master>