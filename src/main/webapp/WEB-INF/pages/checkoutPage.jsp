<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<jsp:useBean id="paymentMethods" type="java.util.List" scope="request"/>
<tags:master pageTitle="Cart">
    <c:set var="message" value="${param.message}"/>
    <c:if test="${not empty message}">
        <p style="color: green;">${param.message}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/checkout" method="post">
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
            </tr>
            </thead>
            <c:forEach var="item" items="${order.itemList}" varStatus="status">
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
                            ${quantity}
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
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td>

                </td>
                <td>
                    Subtotal
                </td>
                <td class="price">
                    <fmt:formatNumber
                            value="${order.subtotal}"
                            type="currency"
                            currencySymbol="${order.itemList[0].product.currency}"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>

                </td>
                <td>
                    Delivery
                </td>
                <td class="price">
                    <fmt:formatNumber
                            value="${order.deliveryCost}"
                            type="currency"
                            currencySymbol="${order.itemList[0].product.currency}"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>

                </td>
                <td>
                    Total
                </td>
                <td class="price">
                    <fmt:formatNumber
                            value="${order.totalCost}"
                            type="currency"
                            currencySymbol="${order.itemList[0].product.currency}"/>
                </td>
            </tr>
        </table>

        <br>

        <table>
            <tags:orderItem name="firstName"
                            label="First Name"
                            order="${order}"
                            errors="${errors}"/>

            <tags:orderItem name="lastName"
                            label="Last Name"
                            order="${order}"
                            errors="${errors}"/>

            <tags:orderItem name="phone"
                            label="Phone"
                            order="${order}"
                            errors="${errors}"/>

            <tr>
                <td>Delivery Date<span style="color: red;">*</span></td>
                <td>
                    <c:set var="error" value="${errors['deliveryDate']}"/>
                    <input name="deliveryDate"
                           type="date"
                           value="${not empty error ? param['deliveryDate'] : date}">
                    <c:if test="${not empty error}">
                        <span style="color: red;">${error}</span>
                    </c:if>
                </td>
            </tr>

            <tags:orderItem name="deliveryAddress"
                            label="Delivery Address"
                            order="${order}"
                            errors="${errors}"/>

            <tr>
                <td>Payment method<span style="color: red;">*</span></td>
                <td>
                    <c:set var="error" value="${errors['paymentMethod']}"/>
                    <select name="paymentMethod">
                        <option></option>
                        <c:forEach var="method" items="${paymentMethods}">
                            <option ${method == param['paymentMethod'] ? 'selected="selected"' : ''}>${method}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty error}">
                        <span style="color: red;">${error}</span>
                    </c:if>
                </td>
            </tr>
        </table>

        <p>
            <button>Place order</button>
        </p>
    </form>
</tags:master>