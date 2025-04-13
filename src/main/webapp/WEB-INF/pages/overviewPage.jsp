<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Cart">
    <c:set var="message" value="${param.message}"/>
    <c:if test="${not empty message}">
        <p style="color: green;">${param.message}</p>
    </c:if>
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
            <td></td>
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
            <td></td>
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
            <td></td>
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
        <tags:orderOverviewItem name="firstName"
                                label="First Name"
                                value="${order.firstName}"
                                order="${order}"/>

        <tags:orderOverviewItem name="lastName"
                                label="Last Name"
                                value="${order.lastName}"
                                order="${order}"/>

        <tags:orderOverviewItem name="phone"
                                label="Phone"
                                value="${order.phone}"
                                order="${order}"/>

        <tr>
            <td>Delivery Date</td>
            <td>
                <input name="deliveryDate"
                       type="date"
                       value="${order.deliveryDate}"
                       readonly="readonly">
            </td>
        </tr>

        <tags:orderOverviewItem name="deliveryAddress"
                                label="Delivery Address"
                                value="${order.deliveryAddress}"
                                order="${order}"/>

        <tags:orderOverviewItem name="paymentMethod"
                                label="Paymnet method"
                                value="${order.paymentMethod}"
                                order="${order}"/>
    </table>
</tags:master>