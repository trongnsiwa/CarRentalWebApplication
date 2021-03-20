<%-- 
Document   : thankYou
Created on : Jan 12, 2021, 11:23:22 PM
Author     : TrongNS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="bootstrap/js/jquery-3.4.1.slim.min.js" type="text/javascript"></script>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <link href="assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/myCSS.css" rel="stylesheet" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hana Shop - Thank you to you</title>
    </head>
    <body>
        <!-- redirect if admin -->
        <c:url value="home" var="homeLink"/>
        <c:if test="${sessionScope.ORDER_INFO == null}">
            <c:redirect url="${homeLink}"/>
        </c:if>
        <!-- navbar -->
        <nav class="navbar navbar-expand-lg navbar-light mb-0 pb-0">
            <a class="navbar-brand" href="home">
                <img src="image/logo.png" class="img-fluid" style="width: 120px; height: 120px;">
            </a>
            <ul class="navbar-nav d-flex flex-row ml-auto mr-5">
                <li class="nav-item ml-3"><a class="nav-link" href="cart">View Cart <c:if test="${sessionScope.CART != null}">
                            <span class="badge badge-info">${sessionScope.CART.getItems().size()}</span>
                        </c:if></a></li>
                <li class="nav-item dropdown ml-3">
                    <a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown" style="background: #fafafa;">Welcome, ${sessionScope.FULLNAME} <b class="caret"></b></a>
                    <ul class="dropdown-menu text-right w-100">
                        <c:if test="${sessionScope.ROLE ne 'Admin'}">
                                <!-- history -->
                                <li><a class="dropdown-item" href="history">History</a></li>
                                </c:if>
                        <li>
                            <a class="dropdown-item text-danger" href="logout">Logout</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </nav>
        <!-- container -->
        <div class="container-fluid" style="padding-top: 80px;">
            <div class="text-center">
                <h1>Thank you for your order!</h1>
                <p class="lead">Thank you. Your order has been received.</p>
            </div>
            <!-- show order info -->
            <div class="container mt-5">
                <div class="row justify-content-center">
                    <div class="col-md-5">
                        <div class="card d-flex flex-column border-2 border-primary bg-light">
                            <div class="card-body">
                                <c:set value="${sessionScope.ORDER_INFO}" var="order"/>
                                <div class="mt-2">
                                    <p class="font-weight-light text-dark">${sessionScope.FULLNAME} - ${sessionScope.PHONE} - ${sessionScope.ADDRESS} </p>
                                </div>
                                <hr class="my-2">
                                <div>
                                    <p class="text-muted" style="font-size: 10px;">DATE:</p>
                                    <p class="font-weight-bold">${order.rentalDate}</p>
                                </div>
                                <hr class="my-2">
                                <div>
                                    <p class="text-muted" style="font-size: 10px;">ITEM LIST</p>
                                    <c:forEach items="${order.rentingItems}" var="item">
                                        <input type="hidden" class="tmp-price-2" value="${item.subTotal}"/>
                                        <p>- <span class="badge badge-info">${item.amount}</span> ${item.carName}: <span class="price-2">${item.subTotal}</span></p>
                                        <p class="small ml-4"><i>From <span class="date">${item.pickUpDate}</span> To <span class="date">${item.dropOffDate}</span> (${item.days} days)</i></p>
                                    </c:forEach>
                                </div>
                                <hr class="my-2">
                                <div>
                                    <p class="text-muted" style="font-size: 10px;">TOTAL:</p>
                                    <input type="hidden" class="tmp-price-2" value="${order.totalPrice}"/>
                                    <p class="font-weight-bold"><span class="price-2">${order.totalPrice}</span></p>
                                </div>
                            </div>
                        </div>
                        <p class="lead justify-content-center mt-4">
                            <c:url var="homeLink" value="home"/>
                            <a class="btn btn-primary w-100" href="${homeLink}" role="button">Continue to homepage</a>
                        </p>    
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            var listTmpPrice2 = document.getElementsByClassName("tmp-price-2");
            var listPrice2 = document.getElementsByClassName("price-2");
            for (var i = 0; i < listPrice2.length; i++) {
                var price2 = parseInt(listTmpPrice2[i].value, 10);
                price2 = price2.toLocaleString('it-IT', {style: 'currency', currency: 'VND'});
                listPrice2[i].innerHTML = price2;
            }
        </script>
    </body>
</html>

