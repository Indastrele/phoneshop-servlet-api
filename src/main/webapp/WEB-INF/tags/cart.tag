<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<p>Cart</p>
<table>
    <tr>
        <c:forEach var="item" items="${cart.itemList}">
            <td>
                <p>${item.product.description}</p>
                <p>${item.quantity}</p>
            </td>
        </c:forEach>
    </tr>
</table>