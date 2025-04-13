<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="recentlyViewed" type="com.es.phoneshop.model.product.price_history.RecentlyViewedProducts" scope="request"/>
<table>
    <tr>
        <c:forEach var="item" items="${recentlyViewed.products}">
            <td>
                <spam>
                <img class="product-tile" src="${item.imageUrl}"/>
                <a href="${pageContext.request.contextPath}/products/${item.id}">
                        ${item.description}
                </a>
                <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="${item.currency.symbol}"/>
                </spam>
            </td>
        </c:forEach>
    </tr>
</table>