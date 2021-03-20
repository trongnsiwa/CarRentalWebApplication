<%-- 
    Document   : cart
    Created on : Feb 21, 2021, 11:56:20 PM
    Author     : TrongNS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <script src='https://www.google.com/recaptcha/api.js?hl=en'></script>
        <script src="bootstrap/js/jquery-3.4.1.slim.min.js" type="text/javascript"></script>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/myCSS.css" rel="stylesheet" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Your cart</title>
    </head>
    <body>
        <style>
            form.decrease {
                margin-right: -4px;
                border-radius: 8px 0 0 8px;
            }

            form.increase {
                margin-left: -4px;
                border-radius: 0 8px 8px 0;
            }

            input.amount {
                text-align: center;
                border: none;
                border-top: 1px solid #ddd;
                border-bottom: 1px solid #ddd;
                margin: 0px;
                width: 40px;
            }

            input[type=number]::-webkit-inner-spin-button,
            input[type=number]::-webkit-outer-spin-button {
                -webkit-appearance: none;
                margin: 0;
            }
        </style>
        <nav class="navbar navbar-expand-lg navbar-light mb-0 pb-0">
            <a class="navbar-brand" href="home">
                <img src="image/logo.png" class="img-fluid" style="width: 120px; height: 120px;">
            </a>
            <ul class="navbar-nav d-flex flex-row ml-auto mr-5">
                <c:if test="${sessionScope.ROLE ne 'Admin'}">
                    <li class="nav-item ml-3"><a class="nav-link" href="cart">View Cart <c:if test="${sessionScope.CART != null}">
                                <span class="badge badge-info">${sessionScope.CART.getItems().size()}</span>
                            </c:if></a></li></c:if>
                        <c:if test="${sessionScope.FULLNAME == null}">
                    <li class="nav-item ml-3"><a class="nav-link" href="login-page">Login</a></li>
                    <li class="nav-item ml-3"><a class="nav-link" href="register-page">Sign Up</a></li>
                    </c:if>
                    <c:if test="${sessionScope.FULLNAME != null}">
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
                </c:if>
            </ul>
        </nav>
        <div class="content ml-0 p-0 mt-5">
            <div class="content-wrapper m-0 p-0">

                <div class="container padding-bottom-3x mb-1">
                    <!-- Shopping Cart-->
                    <div class="table-responsive shopping-cart">
                        <form action="update-cart" method="POST">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <c:set var="cart" value="${sessionScope.CART}"/>
                                        <c:if test="${cart != null}">
                                            <c:set var="items" value="${cart.getItems()}"/>
                                        </c:if>
                                        <th>Car</th>
                                        <th class="text-center">Amount</th>
                                        <th class="text-center">Subtotal</th>
                                        <th class="text-center"><a class="btn btn-sm btn-outline-danger" href="#" data-abc="true" data-href="clear-cart" data-toggle="modal" data-target="#confirm-delete-all" <c:if test="${empty items}">style="pointer-events: none; background-color: gray; color: white;"</c:if>>Clear Cart</a></th>
                                        </tr>
                                    </thead>
                                    <!-- Confirm delete product modal -->
                                    <div class="modal fade in" id="confirm-delete-all" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                        <div class="modal-dialog shadow-lg border-danger">
                                            <div class="modal-content">
                                                <div class="modal-header border-bottom-0 m-0 p-0">
                                                    <div class="d-flex flex-column w-100 text-center">
                                                        <span class="modal-title text-danger" style="font-size: 80px;"><i class="fa fa-exclamation-circle" aria-hidden="true"></i></span>
                                                        <h3 class="modal-title text-center text-danger">Confirm delete</h3>
                                                    </div>
                                                </div>
                                                <div class="modal-body text-center">
                                                    Are you sure want to remove all the cars from your cart?
                                                </div>
                                                <div class="modal-footer d-flex justify-content-center border-top-0">
                                                    <button type="button" class="btn btn-secondary mr-3" data-dismiss="modal">Cancel</button>
                                                    <a class="btn btn-danger" id="btn-ok-1">Remove</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <tbody>
                                    <c:set var="cart" value="${sessionScope.CART}"/>
                                    <c:if test="${cart != null}">
                                        <c:set var="items" value="${cart.getItems()}"/>
                                        <c:if test="${not empty items}">
                                            <c:forEach var="item" items="${items}">
                                                <tr>
                                                    <td>
                                                        <input type="hidden" name="txtCarModelId" value="${item.key.carModelId}"/>
                                                        <div class="product-item">
                                                            <c:url var="viewDetailLink" value="view-detail">
                                                                <c:param name="id" value="${item.key.carModelId}"/>
                                                                <c:param name="goBackPage" value="cart"/>
                                                                <c:param name="rentalDate" value="${item.key.userRentalDate}"/>
                                                                <c:param name="rentalTime" value="${item.key.userRentalTime}"/>
                                                                <c:param name="returnDate" value="${item.key.userReturnDate}"/>
                                                                <c:param name="returnTime" value="${item.key.userReturnTime}"/>
                                                            </c:url>
                                                            <a class="product-thumb" href="${viewDetailLink}"><img src="${item.key.smallImageLink}" alt="Car"></a>
                                                            <div class="product-info">
                                                                <h4 class="product-title"><a href="${viewDetailLink}">${item.key.carName}</a></h4><span><em>Type:</em> ${item.key.typeName}</span><span><em>Seat:</em> ${item.key.seat}</span><span><em>Color:</em> ${item.key.color}</span>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td class="text-center">
                                                        <div class="d-flex w-100 justify-content-center">
                                                            <button type="button" class="btn btn-outline-secondary" class="decrease" onclick="decreaseValue(this.value)" value="${item.key.carModelId}" style="cursor: pointer;">-</button>
                                                            <input type="number" class="mx-2 amount" id="${item.key.carModelId}" name="txtAmountOfCar" value="${item.value}" readonly="" />
                                                            <button type="button" class="btn btn-outline-secondary" class="increase" onclick="increaseValue(this.value)" value="${item.key.carModelId}" style="cursor: pointer;">+</button>
                                                        </div>
                                                    </td>
                                            <input type="hidden" class="price" value="${item.key.userPrice}"/>
                                            <td class="text-center w-25"><span class="total" style="font-size: 20px;">${item.key.userPrice}</span></td>
                                                <c:url var="removeFromCartLink" value="remove-from-cart">
                                                    <c:param name="id" value="${item.key.carModelId}"/>
                                                </c:url>
                                            <td class="text-center"><a class="remove-from-cart" href="#" data-abc="true" data-href="${removeFromCartLink}" data-toggle="modal" data-target="#confirm-delete" style="font-size: 30px;"><i class="fa fa-trash"></i></a></td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    <!-- Confirm delete product modal -->
                                    <div class="modal fade in" id="confirm-delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                        <div class="modal-dialog shadow-lg border-danger">
                                            <div class="modal-content">
                                                <div class="modal-header border-bottom-0 m-0 p-0">
                                                    <div class="d-flex flex-column w-100 text-center">
                                                        <span class="modal-title text-danger" style="font-size: 80px;"><i class="fa fa-exclamation-circle" aria-hidden="true"></i></span>
                                                        <h3 class="modal-title text-center text-danger">Confirm delete</h3>
                                                    </div>
                                                </div>
                                                <div class="modal-body text-center">
                                                    Are you sure want to remove this car from your cart?
                                                </div>
                                                <div class="modal-footer d-flex justify-content-center border-top-0">
                                                    <button type="button" class="btn btn-secondary mr-3" data-dismiss="modal">Cancel</button>
                                                    <a class="btn btn-danger btn-ok-2">Remove</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <c:if test="${empty items}">
                                        <tr>
                                            <td colspan="4" class="py-5"style="text-align: center;">
                                                <h6>
                                                    <font color="red">
                                                    Empty cart
                                                    </font>
                                                </h6>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:if>
                                <c:if test="${cart == null}">
                                    <tr>
                                        <td colspan="4" class="py-5" style="text-align: center;">
                                            <h6>
                                                <font color="red">
                                                You didn't add any car to cart
                                                </font>
                                            </h6>
                                        </td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td colspan="3">
                                        <p class="text-info small"><i>* Click to image or car name to change date *</i></p>
                                    </td>
                                    <td class="pb-3" colspan="1" style="text-align: right;"><input type="submit" class="btn btn-primary" <c:if test="${empty items}">style="pointer-events: none; background-color: gray;"</c:if>value="Update Cart" /></div></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                        <div class="shopping-cart-footer">
                            <div class="column">
                                <input type="hidden" id="apply-discount-message" value="${requestScope.APPY_CODE_MESSAGE}"/>
                            <form class="coupon-form" action="apply-discount" method="post">
                                <input class="form-control form-control-sm" type="text" placeholder="Discount code" name="txtDiscountCode" <c:if test="${cart.discount != null}">value="${cart.discount.code}"</c:if> style="display: inline-block;">
                                <input type="hidden" id="discount-percent" value="${cart.discount.percent}"/>
                                <button class="btn btn-outline-secondary btn-sm" type="submit">Apply Discount</button>
                            </form>
                        </div>
                        <c:set var="cart" value="${sessionScope.CART}"/>

                        <div class="column">
                            <div class="column text-lg"><span class="mr-1">Discount:</span> <span id="discount-price" style="font-size: 20px;"></span> <c:if test="${cart.discount != null}"><span style="font-size: 20px;">(${cart.discount.percent}%)</span></c:if></div>
                            <div class="column text-lg"><span class="mr-1">Total:</span> <span class="text-primary" id="total-price" style="font-size: 25px;"></span> <c:if test="${cart.discount != null}"><strike><span id="old-price" style="font-size: 17px;" class="text-danger"></span></strike></c:if></div>
                            </div>
                        </div>
                        <div class="shopping-cart-footer">
                            <div class="column"><a class="btn btn-outline-secondary" href="home"><i class="icon-arrow-left"></i>&nbsp;Back to Home</a></div>
                            <div class="column"><a class="btn btn-success" href="checkout" style="width: 200px; <c:if test="${empty items}">pointer-events: none; background-color: gray;</c:if>">Checkout</a></div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- confirm checkout cart modal -->           
            <input type="hidden" id="failure-msg" value="${requestScope.CHECKOUT_FAIL}"/>
        <c:set var="order" value="${sessionScope.ORDER_INFO}"/>
        <input type="hidden" id="preview-order" value="${order}"/>
        <c:if test="${order != null}">
            <div class="modal fade in" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true" id="confirm-modal">
                <div class="modal-dialog modal-dialog-centered border-0 border-primary">
                    <div class="modal-content">
                        <!-- header -->
                        <div class="modal-header border-bottom-0 m-0 p-3">
                            <h3 class="modal-title w-100 text-center text-info" id="model-label">Confirm order information</h3>
                        </div>
                        <!-- body -->
                        <div class="modal-body">
                            <div>
                                <p style="font-size: 17px;" class="ml-2"><i class="fa fa-address-card mr-2"></i> <b>${sessionScope.ADDRESS}</b></p>
                            </div>
                            <hr class="my-2">
                            <div>
                                <p style="font-size: 17px;" class="ml-2"><i class="fa fa-user mr-2"></i> <b>${sessionScope.FULLNAME}</b></p>
                                <p style="font-size: 17px;" class="ml-2"><i class="fa fa-phone mr-2"></i> <b>${sessionScope.PHONE}</b></p>
                            </div>
                            <hr class="my-2">
                            <div>
                                <p style="font-size: 20px;" class="p-0 ml-2"><b>* Item list:</b></p>
                                <c:forEach items="${order.rentingItems}" var="item">
                                    <input type="hidden" class="tmp-price-2" value="${item.subTotal}"/>
                                    <p class="ml-4">- <span class="badge badge-info">${item.amount}</span> ${item.carName}: <span class="price-2">${item.subTotal}</span></p>
                                    <p class="small ml-4"><i>From <span class="date">${item.pickUpDate}</span> To <span class="date">${item.dropOffDate}</span> (${item.days} days)</i></p>
                                </c:forEach>
                            </div>
                            <hr class="my-2">
                            <div class="d-flex flex-column">
                                <c:if test="${not empty order.discountCode}">
                                    <p style="font-size: 20px;" class="ml-2 pt-2 text-right"><b>Discount:</b> ${order.discountCode} <span>(${order.discountPercent}%)</span></p>
                                </c:if>
                                <input type="hidden" class="tmp-price-2" value="${order.totalPrice}"/>
                                <p style="font-size: 20px;" class="ml-2 text-right"><b>Total price:</b> <span class="price-2">${order.totalPrice}</span></p>
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
                        <!-- footer -->
                        <div class="d-flex">
                            <div class="modal-footer w-100 align-items-center border-top-0">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <c:url var="checkOutLink" value="checkout">
                                    <c:param name="confirm" value="yes"/>
                                </c:url>
                                <a href="${checkOutLink}" class="btn btn-info">Checkout</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        <input type="hidden" id="change-date-success" value="${requestScope.CHANGE_DATE_SUCCESS}"/>
        <input type="hidden" id="change-date-fail" value="${requestScope.CHANGE_DATE_FAIL}"/>
        <c:set var="itemDetail" value="${requestScope.ITEM_DETAIL}"/>
        <input type="hidden" id="item-detail" value="${itemDetail}"/>
        <c:if test="${itemDetail != null}">
            <div class="fade modal in" role="dialog" tabindex="-1" aria-hidden="true" style="display: block;" id="car-detail-modal" data-keyboard="false" data-backdrop="static">
                <div class="modal-dialog modal-details">
                    <div class="modal-content border-0 bg-transparent" role="document">
                        <div class="modal-detail dt__wrapper">
                            <div class="modal-header text-white border-bottom-0">
                                <div class="info-car">
                                    <h1 class="title-car">${itemDetail.carName}</h1>
                                </div>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="font-size: 40px;">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body detail-container">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="content-detail bg-white shadow-lg ">
                                            <div class="info-car--desc no-pd cover-car">
                                                <div class="car-img">
                                                    <div class="fix-img">
                                                        <img src="${itemDetail.smallImageLink}" alt="Rental Car"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <form action="change-date" method="POST">
                                            <div class="sidebar-detail shadow-lg bg-white border-2 border-secondary p-5">
                                                <div class="rent-box rent-car" id="booking-sidebar">
                                                    <div class="d-flex justify-content-center mb-5">
                                                        <input type="hidden" class="tmpPrice" value="${itemDetail.price}"/>
                                                        <h3 class="text-success"><span class="price-per-day">${itemDetail.price}</span>
                                                            <span> / days</span></h3>
                                                    </div>
                                                    <div class="form-search has-timer">
                                                        <label class="home-label">Rental Date:</label>
                                                        <div class="d-flex">
                                                            <div class="wrap-input home-datetimerange">
                                                                <div class="" style="display: inline-block">
                                                                    <input type="hidden" id="rental-date" value="${requestScope.USER_RENTAL_DATE}"/>
                                                                    <div class="input-append date d-flex align-items-center" id="datepicker1" data-date-format="dd/mm/yyyy" data-date-start-date="d">
                                                                        <input name="txtUserRentalDate" class="form-control" id="fdate" 
                                                                               value="" 
                                                                               type="text" readonly="">
                                                                        <span class="add-on ml-2"><i class="fa fa-calendar"></i></span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="home-select">
                                                                <input type="hidden" id="rental-time" value="${requestScope.USER_RENTAL_TIME}"/>
                                                                <select name="txtUserRentalTime" class="form-control ml-3" id="user-select-rental">
                                                                    <option value="0" <c:if test="${requestScope.USER_RENTAL_TIME eq '0'}">selected</c:if>>00:00 </option>
                                                                    <option value="30" <c:if test="${requestScope.USER_RENTAL_TIME eq '30'}">selected</c:if>>00:30 </option>
                                                                    <option value="60" <c:if test="${requestScope.USER_RENTAL_TIME eq '60'}">selected</c:if>>01:00 </option>
                                                                    <option value="90" <c:if test="${requestScope.USER_RENTAL_TIME eq '90'}">selected</c:if>>01:30 </option>
                                                                    <option value="120" <c:if test="${requestScope.USER_RENTAL_TIME eq '120'}">selected</c:if>>02:00 </option>
                                                                    <option value="150" <c:if test="${requestScope.USER_RENTAL_TIME eq '150'}">selected</c:if>>02:30 </option>
                                                                    <option value="180" <c:if test="${requestScope.USER_RENTAL_TIME eq '180'}">selected</c:if>>03:00 </option>
                                                                    <option value="210" <c:if test="${requestScope.USER_RENTAL_TIME eq '210'}">selected</c:if>>03:30 </option>
                                                                    <option value="240" <c:if test="${requestScope.USER_RENTAL_TIME eq '240'}">selected</c:if>>04:00 </option>
                                                                    <option value="270" <c:if test="${requestScope.USER_RENTAL_TIME eq '270'}">selected</c:if>>04:30 </option>
                                                                    <option value="300" <c:if test="${requestScope.USER_RENTAL_TIME eq '300'}">selected</c:if>>05:00 </option>
                                                                    <option value="330" <c:if test="${requestScope.USER_RENTAL_TIME eq '330'}">selected</c:if>>05:30 </option>
                                                                    <option value="360" <c:if test="${requestScope.USER_RENTAL_TIME eq '360'}">selected</c:if>>06:00 </option>
                                                                    <option value="390" <c:if test="${requestScope.USER_RENTAL_TIME eq '390'}">selected</c:if>>06:30 </option>
                                                                    <option value="420" <c:if test="${requestScope.USER_RENTAL_TIME eq '420'}">selected</c:if>>07:00 </option>
                                                                    <option value="450" <c:if test="${requestScope.USER_RENTAL_TIME eq '450'}">selected</c:if>>07:30 </option>
                                                                    <option value="480" <c:if test="${requestScope.USER_RENTAL_TIME eq '480'}">selected</c:if>>08:00 </option>
                                                                    <option value="510" <c:if test="${requestScope.USER_RENTAL_TIME eq '510'}">selected</c:if>>08:30 </option>
                                                                    <option value="540" <c:if test="${requestScope.USER_RENTAL_TIME eq '540'}">selected</c:if>>09:00 </option>
                                                                    <option value="570" <c:if test="${requestScope.USER_RENTAL_TIME eq '570'}">selected</c:if>>09:30 </option>
                                                                    <option value="600" <c:if test="${requestScope.USER_RENTAL_TIME eq '600'}">selected</c:if>>10:00 </option>
                                                                    <option value="630" <c:if test="${requestScope.USER_RENTAL_TIME eq '630'}">selected</c:if>>10:30 </option>
                                                                    <option value="660" <c:if test="${requestScope.USER_RENTAL_TIME eq '660'}">selected</c:if>>11:00 </option>
                                                                    <option value="690" <c:if test="${requestScope.USER_RENTAL_TIME eq '690'}">selected</c:if>>11:30 </option>
                                                                    <option value="720" <c:if test="${requestScope.USER_RENTAL_TIME eq '720'}">selected</c:if>>12:00 </option>
                                                                    <option value="750" <c:if test="${requestScope.USER_RENTAL_TIME eq '750'}">selected</c:if>>12:30 </option>
                                                                    <option value="780" <c:if test="${requestScope.USER_RENTAL_TIME eq '780'}">selected</c:if>>13:00 </option>
                                                                    <option value="810" <c:if test="${requestScope.USER_RENTAL_TIME eq '810'}">selected</c:if>>13:30 </option>
                                                                    <option value="840" <c:if test="${requestScope.USER_RENTAL_TIME eq '840'}">selected</c:if>>14:00 </option>
                                                                    <option value="870" <c:if test="${requestScope.USER_RENTAL_TIME eq '870'}">selected</c:if>>14:30 </option>
                                                                    <option value="900" <c:if test="${requestScope.USER_RENTAL_TIME eq '900'}">selected</c:if>>15:00 </option>
                                                                    <option value="930" <c:if test="${requestScope.USER_RENTAL_TIME eq '930'}">selected</c:if>>15:30 </option>
                                                                    <option value="960" <c:if test="${requestScope.USER_RENTAL_TIME eq '960'}">selected</c:if>>16:00 </option>
                                                                    <option value="990" <c:if test="${requestScope.USER_RENTAL_TIME eq '990'}">selected</c:if>>16:30 </option>
                                                                    <option value="1020" <c:if test="${requestScope.USER_RENTAL_TIME eq '1020'}">selected</c:if>>17:00 </option>
                                                                    <option value="1050" <c:if test="${requestScope.USER_RENTAL_TIME eq '1050'}">selected</c:if>>17:30 </option>
                                                                    <option value="1080" <c:if test="${requestScope.USER_RENTAL_TIME eq '1080'}">selected</c:if>>18:00 </option>
                                                                    <option value="1110" <c:if test="${requestScope.USER_RENTAL_TIME eq '1110'}">selected</c:if>>18:30 </option>
                                                                    <option value="1140" <c:if test="${requestScope.USER_RENTAL_TIME eq '1140'}">selected</c:if>>19:00 </option>
                                                                    <option value="1170" <c:if test="${requestScope.USER_RENTAL_TIME eq '1170'}">selected</c:if>>19:30 </option>
                                                                    <option value="1200" <c:if test="${requestScope.USER_RENTAL_TIME eq '1200'}">selected</c:if>>20:00 </option>
                                                                    <option value="1230" <c:if test="${requestScope.USER_RENTAL_TIME eq '1230'}">selected</c:if>>20:30 </option>
                                                                    <option value="1260" <c:if test="${requestScope.USER_RENTAL_TIME eq '1260'}">selected</c:if>>21:00 </option>
                                                                    <option value="1290" <c:if test="${requestScope.USER_RENTAL_TIME eq '1290'}">selected</c:if>>21:30 </option>
                                                                    <option value="1320" <c:if test="${requestScope.USER_RENTAL_TIME eq '1320'}">selected</c:if>>22:00 </option>
                                                                    <option value="1350" <c:if test="${requestScope.USER_RENTAL_TIME eq '1350'}">selected</c:if>>22:30 </option>
                                                                    <option value="1380" <c:if test="${requestScope.USER_RENTAL_TIME eq '1380'}">selected</c:if>>23:00 </option>
                                                                    <option value="1410" <c:if test="${requestScope.USER_RENTAL_TIME eq '1410'}">selected</c:if>>23:30 </option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="form-search has-timer">
                                                            <label class="home-label">Return Date:</label>
                                                            <div class="d-flex">
                                                                <input type="hidden" id="return-date" value="${requestScope.USER_RETURN_DATE}"/>
                                                            <div class="input-append date d-flex align-items-center" id="datepicker2" data-date-format="dd/mm/yyyy" data-date-start-date="d">
                                                                <input name="txtUserReturnDate" class="form-control" id="tdate" 
                                                                       value="" 
                                                                       type="text" readonly="">
                                                                <span class="add-on ml-2"><i class="fa fa-calendar"></i></span>
                                                            </div>
                                                            <div class="home-select">
                                                                <input type="hidden" id="return-time" value="${requestScope.USER_RETURN_TIME}"/>
                                                                <select name="txtUserReturnTime" class="form-control ml-3" id="user-select-return">
                                                                    <option value="0" <c:if test="${requestScope.USER_RETURN_TIME eq '0'}">selected</c:if>>00:00 </option>
                                                                    <option value="30" <c:if test="${requestScope.USER_RETURN_TIME eq '30'}">selected</c:if>>00:30 </option>
                                                                    <option value="60" <c:if test="${requestScope.USER_RETURN_TIME eq '60'}">selected</c:if>>01:00 </option>
                                                                    <option value="90" <c:if test="${requestScope.USER_RETURN_TIME eq '90'}">selected</c:if>>01:30 </option>
                                                                    <option value="120" <c:if test="${requestScope.USER_RETURN_TIME eq '120'}">selected</c:if>>02:00 </option>
                                                                    <option value="150" <c:if test="${requestScope.USER_RETURN_TIME eq '150'}">selected</c:if>>02:30 </option>
                                                                    <option value="180" <c:if test="${requestScope.USER_RETURN_TIME eq '180'}">selected</c:if>>03:00 </option>
                                                                    <option value="210" <c:if test="${requestScope.USER_RETURN_TIME eq '210'}">selected</c:if>>03:30 </option>
                                                                    <option value="240" <c:if test="${requestScope.USER_RETURN_TIME eq '240'}">selected</c:if>>04:00 </option>
                                                                    <option value="270" <c:if test="${requestScope.USER_RETURN_TIME eq '270'}">selected</c:if>>04:30 </option>
                                                                    <option value="300" <c:if test="${requestScope.USER_RETURN_TIME eq '300'}">selected</c:if>>05:00 </option>
                                                                    <option value="330" <c:if test="${requestScope.USER_RETURN_TIME eq '330'}">selected</c:if>>05:30 </option>
                                                                    <option value="360" <c:if test="${requestScope.USER_RETURN_TIME eq '360'}">selected</c:if>>06:00 </option>
                                                                    <option value="390" <c:if test="${requestScope.USER_RETURN_TIME eq '390'}">selected</c:if>>06:30 </option>
                                                                    <option value="420" <c:if test="${requestScope.USER_RETURN_TIME eq '420'}">selected</c:if>>07:00 </option>
                                                                    <option value="450" <c:if test="${requestScope.USER_RETURN_TIME eq '450'}">selected</c:if>>07:30 </option>
                                                                    <option value="480" <c:if test="${requestScope.USER_RETURN_TIME eq '480'}">selected</c:if>>08:00 </option>
                                                                    <option value="510" <c:if test="${requestScope.USER_RETURN_TIME eq '510'}">selected</c:if>>08:30 </option>
                                                                    <option value="540" <c:if test="${requestScope.USER_RETURN_TIME eq '540'}">selected</c:if>>09:00 </option>
                                                                    <option value="570" <c:if test="${requestScope.USER_RETURN_TIME eq '570'}">selected</c:if>>09:30 </option>
                                                                    <option value="600" <c:if test="${requestScope.USER_RETURN_TIME eq '600'}">selected</c:if>>10:00 </option>
                                                                    <option value="630" <c:if test="${requestScope.USER_RETURN_TIME eq '630'}">selected</c:if>>10:30 </option>
                                                                    <option value="660" <c:if test="${requestScope.USER_RETURN_TIME eq '660'}">selected</c:if>>11:00 </option>
                                                                    <option value="690" <c:if test="${requestScope.USER_RETURN_TIME eq '690'}">selected</c:if>>11:30 </option>
                                                                    <option value="720" <c:if test="${requestScope.USER_RETURN_TIME eq '720'}">selected</c:if>>12:00 </option>
                                                                    <option value="750" <c:if test="${requestScope.USER_RETURN_TIME eq '750'}">selected</c:if>>12:30 </option>
                                                                    <option value="780" <c:if test="${requestScope.USER_RETURN_TIME eq '780'}">selected</c:if>>13:00 </option>
                                                                    <option value="810" <c:if test="${requestScope.USER_RETURN_TIME eq '810'}">selected</c:if>>13:30 </option>
                                                                    <option value="840" <c:if test="${requestScope.USER_RETURN_TIME eq '840'}">selected</c:if>>14:00 </option>
                                                                    <option value="870" <c:if test="${requestScope.USER_RETURN_TIME eq '870'}">selected</c:if>>14:30 </option>
                                                                    <option value="900" <c:if test="${requestScope.USER_RETURN_TIME eq '900'}">selected</c:if>>15:00 </option>
                                                                    <option value="930" <c:if test="${requestScope.USER_RETURN_TIME eq '930'}">selected</c:if>>15:30 </option>
                                                                    <option value="960" <c:if test="${requestScope.USER_RETURN_TIME eq '960'}">selected</c:if>>16:00 </option>
                                                                    <option value="990" <c:if test="${requestScope.USER_RETURN_TIME eq '990'}">selected</c:if>>16:30 </option>
                                                                    <option value="1020" <c:if test="${requestScope.USER_RETURN_TIME eq '1020'}">selected</c:if>>17:00 </option>
                                                                    <option value="1050" <c:if test="${requestScope.USER_RETURN_TIME eq '1050'}">selected</c:if>>17:30 </option>
                                                                    <option value="1080" <c:if test="${requestScope.USER_RETURN_TIME eq '1080'}">selected</c:if>>18:00 </option>
                                                                    <option value="1110" <c:if test="${requestScope.USER_RETURN_TIME eq '1110'}">selected</c:if>>18:30 </option>
                                                                    <option value="1140" <c:if test="${requestScope.USER_RETURN_TIME eq '1140'}">selected</c:if>>19:00 </option>
                                                                    <option value="1170" <c:if test="${requestScope.USER_RETURN_TIME eq '1170'}">selected</c:if>>19:30 </option>
                                                                    <option value="1200" <c:if test="${requestScope.USER_RETURN_TIME eq '1200'}">selected</c:if>>20:00 </option>
                                                                    <option value="1230" <c:if test="${requestScope.USER_RETURN_TIME eq '1230'}">selected</c:if>>20:30 </option>
                                                                    <option value="1260" <c:if test="${requestScope.USER_RETURN_TIME eq '1260'}">selected</c:if>>21:00 </option>
                                                                    <option value="1290" <c:if test="${requestScope.USER_RETURN_TIME eq '1290'}">selected</c:if>>21:30 </option>
                                                                    <option value="1320" <c:if test="${requestScope.USER_RETURN_TIME eq '1320'}">selected</c:if>>22:00 </option>
                                                                    <option value="1350" <c:if test="${requestScope.USER_RETURN_TIME eq '1350'}">selected</c:if>>22:30 </option>
                                                                    <option value="1380" <c:if test="${requestScope.USER_RETURN_TIME eq '1380'}">selected</c:if>>23:00 </option>
                                                                    <option value="1410" <c:if test="${requestScope.USER_RETURN_TIME eq '1410'}">selected</c:if>>23:30 </option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <hr class="my-3">
                                                        <div class="line-form notice-form note mt-3">
                                                            <h5 class="text-center font-weight-bold">Information</h5>
                                                            <p class="d-flex justify-content-between mt-3">
                                                                <span class="text-primary mr-3">Quantity:</span>
                                                                <span>${itemDetail.quantity}</span>
                                                        </p>
                                                    </div>
                                                    <div class="line-form local mt-3 mb-0">
                                                        <label class="text-primary">Pick-up Location:</label>
                                                        <div class="note d-flex justify-content-end">
                                                            <p class="pickup">
                                                                ${itemDetail.address}
                                                            </p>
                                                        </div>
                                                    </div>
                                                    <hr class="my-3">
                                                    <div class="car-bill mt-3">
                                                        <h5 class="text-center font-weight-bold">Price detail</h5>
                                                        <div class="bill-wrap">
                                                            <div class="group d-flex justify-content-between">
                                                                <input type="hidden" class="tmpPrice" value="${itemDetail.price}"/>
                                                                <input type="hidden" id="item-detail-price" value="${itemDetail.price}"/>
                                                                <label class="text-primary mr-3 font-weight-bold">Rental Price</label>
                                                                <span>
                                                                    <span class="price-per-day">${itemDetail.price}</span> / days
                                                                </span>
                                                            </div>
                                                            <div class="group d-flex justify-content-between">
                                                                <label class="text-primary mr-3 font-weight-bold">Number Of Days</label>
                                                                <span id="count-day"></span>
                                                            </div>
                                                            <div class="group has-line d-flex justify-content-between">
                                                                <label class="text-primary mr-3 font-weight-bold">Total Price</label>
                                                                <span>
                                                                    <strong>
                                                                        <span id="total-rental-price">

                                                                        </span>
                                                                    </strong>
                                                                </span>
                                                            </div>
                                                            <div class="d-flex justify-content-center mt-3">
                                                                <input type="hidden" name="id" value="${itemDetail.carModelId}"/>
                                                                <input type="submit" class="btn btn-outline-primary w-100" value="Change Date" id="btn-change-date"/>
                                                            </div>
                                                            <div class="space m"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        <script type="text/javascript">
            window.onload = function () {
                calculateEachTotalPrice();
                calculateTotalPrice();
                var itemDetail = document.getElementById("item-detail").value;
                if (itemDetail !== "") {
                    $('#car-detail-modal').modal('show');
                }

                var message = document.getElementById("apply-discount-message").value;
                if (message !== '') {
                    if (message.indexOf('successfully') !== -1) {
                        swal({
                            title: "Success",
                            text: message,
                            icon: "success"
                        }).then(function () {
                            window.location.replace("http://localhost:8084/SE140037_J3LP0015/cart");
                        });
                    } else {
                        swal({
                            title: "Apply discount fail",
                            text: message,
                            icon: "error"
                        }).then(function () {
                            window.location.replace("http://localhost:8084/SE140037_J3LP0015/cart");
                        });
                    }
                }

                var msgDateSuccess = document.getElementById("change-date-success").value;
                if (msgDateSuccess !== '') {
                    swal("Success", msgDateSuccess, "success");
                }

                var msgDateFail = document.getElementById("change-date-fail").value;
                if (msgDateFail !== '') {
                    swal("Fail", msgDateFail, "error");
                }

                var failureMsg = document.getElementById("failure-msg").value;
                if (failureMsg !== '') {
                    if (failureMsg.indexOf('mistake') !== -1) {
                        swal({
                            title: "Failure",
                            text: failureMsg,
                            icon: "error"
                        }).then(function () {
                            window.location.replace("http://localhost:8084/SE140037_J3LP0015/cart");
                        });
                    } else {
                        swal({
                            title: "Out of stock",
                            text: failureMsg,
                            icon: "error"
                        }).then(function () {
                            window.location.replace("http://localhost:8084/SE140037_J3LP0015/cart");
                        });
                    }
                }

                var confirmBox = document.getElementById("preview-order").value;
                if (confirmBox !== "") {
                    $('#confirm-modal').modal('show');
                }
            };

            $('#confirm-delete').on('show.bs.modal', function (e) {
                $(this).find('.btn-ok-2').attr('href', $(e.relatedTarget).data('href'));
            });

            $('#confirm-delete-all').on('show.bs.modal', function (e) {
                $(this).find('#btn-ok-1').attr('href', $(e.relatedTarget).data('href'));
            });

            var rentalDate = document.getElementById("rental-date").value;
            var returnDate = document.getElementById("return-date").value;
            var rentalTime = document.getElementById("rental-time").value;
            var returnTime = document.getElementById("return-time").value;

            var dateParts = rentalDate.split("/");
            today = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]);

            var dateParts2 = returnDate.split("/");
            tomorrow = new Date(+dateParts2[2], dateParts2[1] - 1, +dateParts2[0]);

            var arr = new Array();
            $('#user-select-rental option').each(function () {
                arr.push($(this).val());
            });

            var arr2 = new Array();
            $('#user-select-return option').each(function () {
                arr2.push($(this).val());
            });

            var d = new Date();

            if ((today.getFullYear() + today.getMonth() + today.getDay()) === (d.getFullYear() + d.getMonth() + d.getDay())) {
                time = d.getHours() * 60 + d.getMinutes();

                for (i = arr.length - 1; i > 0; i--) {
                    if (parseInt(arr[i - 1]) >= time && time <= parseInt(arr[i])) {
                        if (rentalTime !== '' && parseInt(rentalTime) >= time) {
                            $('#user-select-rental').val(parseInt(rentalTime));
                        } else {
                            $('#user-select-rental').val(arr[i - 1]);
                        }
                    } else {
                        $("#user-select-rental option[value='" + arr[i - 1] + "']")
                                .attr("disabled", "disabled");
                    }
                }
            }

            if ((tomorrow.getFullYear() + tomorrow.getMonth() + tomorrow.getDay()) === (d.getFullYear() + d.getMonth() + d.getDay())) {
                time = d.getHours() * 60 + d.getMinutes();

                var index;
                for (i = arr2.length - 1; i > 0; i--) {
                    if (parseInt(arr2[i - 1]) >= time && time <= parseInt(arr2[i])) {
                        if (returnTime !== '' && parseInt(returnTime) >= time) {
                            $('#user-select-return').val(parseInt(returnTime));
                        } else {
                            $('#user-select-return').val(arr2[i]);
                        }
                        index = i - 1;
                    } else {
                        $("#user-select-return option[value='" + arr2[i - 1] + "']")
                                .attr("disabled", "disabled");
                    }
                }

                $("#user-select-return option[value='" + arr2[index] + "']")
                        .attr("disabled", "disabled");
            }


            $("#datepicker1").datepicker({
                autoclose: true,
                todayHighlight: true
            }).datepicker('update', today);

            $("#datepicker2").datepicker({
                autoclose: true,
                todayHighlight: true
            }).datepicker('update', tomorrow);

            $(function () {
                var start = $("#fdate").val();
                var dp1 = start.split("/");
                var startD = new Date(+dp1[2], dp1[1] - 1, +dp1[0]);

                var end = $("#tdate").val();
                var dp2 = end.split("/");
                var endD = new Date(+dp2[2], dp2[1] - 1, +dp2[0]);

                const diffTime = endD.getTime() - startD.getTime();
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                var price = document.getElementById("item-detail-price").value;
                var totalPrice = (diffDays + 1) * parseInt(price);
                totalPrice = totalPrice.toLocaleString('it-IT', {style: 'currency', currency: 'VND'});
                document.getElementById("count-day").innerHTML = diffDays + 1;
                document.getElementById("total-rental-price").innerHTML = totalPrice;
                $('#datepicker1').datepicker({
                    format: "DD/MM/YYYY",
                    todayHighlight: 'TRUE',
                    autoclose: true,
                    minDate: 0,
                    maxDate: '+1Y+6M'
                }).on('changeDate', function (ev) {
                    var startDate = new Date(ev.date.valueOf());
                    $('#datepicker2').datepicker('setStartDate', startDate);

                    var start = $("#fdate").val();
                    var dp1 = start.split("/");
                    var startD = new Date(+dp1[2], dp1[1] - 1, +dp1[0]);

                    var end = $("#tdate").val();
                    var dp2 = end.split("/");
                    var endD = new Date(+dp2[2], dp2[1] - 1, +dp2[0]);

                    var d1 = new Date();

                    if ((startD.getFullYear() + startD.getMonth() + startD.getDay()) === (d1.getFullYear() + d1.getMonth() + d1.getDay())) {
                        var time1 = d1.getHours() * 60 + d1.getMinutes();

                        for (i = arr.length - 1; i > 0; i--) {
                            if (parseInt(arr[i - 1]) >= time1 && time1 <= parseInt(arr[i])) {
                                $('#user-select-rental').val(arr[i - 1]);
                            } else {
                                $("#user-select-rental option[value='" + arr[i - 1] + "']")
                                        .attr("disabled", "disabled");
                            }
                        }
                    } else {
                        for (i = arr.length - 1; i > 0; i--) {
                            $('#user-select-rental').val(arr[0]);
                            $("#user-select-rental option[value='" + arr[i - 1] + "']")
                                    .removeAttr("disabled");
                        }
                    }

                    const diffTime = endD.getTime() - startD.getTime();
                    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                    var price = document.getElementById("item-detail-price").value;
                    var totalPrice = (diffDays + 1) * parseInt(price, 10);
                    totalPrice = totalPrice.toLocaleString('it-IT', {style: 'currency', currency: 'VND'});
                    if (diffDays >= 0) {
                        document.getElementById("count-day").innerHTML = diffDays + 1;
                        document.getElementById("total-rental-price").innerHTML = totalPrice;
                        document.getElementById("btn-change-date").disabled = false;
                    } else {
                        document.getElementById("count-day").innerHTML = 0;
                        document.getElementById("total-rental-price").innerHTML = 0;
                        document.getElementById("btn-change-date").disabled = true;
                    }
                }).on('clearDate', function (ev) {
                    $('#datepicker2').datepicker('setStartDate', null);
                });

                $('#datepicker2').datepicker({
                    format: "DD/MM/YYYY",
                    todayHighlight: 'TRUE',
                    autoclose: true,
                    minDate: '0',
                    maxDate: '+1Y+6M'
                }).on('changeDate', function (ev) {
                    var endDate = new Date(ev.date.valueOf());
                    $('#datepicker1').datepicker('setEndDate', endDate);

                    var start = $("#fdate").val();
                    var dp1 = start.split("/");
                    var startD = new Date(+dp1[2], dp1[1] - 1, +dp1[0]);

                    var end = $("#tdate").val();
                    var dp2 = end.split("/");
                    var endD = new Date(+dp2[2], dp2[1] - 1, +dp2[0]);

                    var d1 = new Date();

                    if ((endD.getFullYear() + endD.getMonth() + endD.getDay()) === (d1.getFullYear() + d1.getMonth() + d1.getDay())) {
                        var time1 = d1.getHours() * 60 + d1.getMinutes();

                        for (i = arr2.length - 1; i > 0; i--) {
                            if (parseInt(arr2[i - 1]) >= time1 && time1 <= parseInt(arr2[i])) {
                                $('#user-select-return').val(arr2[i]);
                            } else {
                                $("#user-select-return option[value='" + arr2[i] + "']")
                                        .attr("disabled", "disabled");
                            }
                        }
                    } else {
                        for (i = arr2.length - 1; i > 0; i--) {
                            $('#user-select-return').val(arr2[0]);
                            $("#user-select-return option[value='" + arr2[i - 1] + "']")
                                    .removeAttr("disabled");
                        }
                    }

                    const diffTime = endD.getTime() - startD.getTime();
                    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                    var price = document.getElementById("item-detail-price").value;
                    var totalPrice = (diffDays + 1) * parseInt(price, 10);
                    totalPrice = totalPrice.toLocaleString('it-IT', {style: 'currency', currency: 'VND'});
                    if (diffDays >= 0) {
                        document.getElementById("count-day").innerHTML = diffDays + 1;
                        document.getElementById("total-rental-price").innerHTML = totalPrice;
                        document.getElementById("btn-change-date").disabled = false;
                    } else {
                        document.getElementById("count-day").innerHTML = 0;
                        document.getElementById("total-rental-price").innerHTML = 0;
                        document.getElementById("btn-change-date").disabled = true;
                    }
                }).on('clearDate', function (ev) {
                    $('#datepicker1').datepicker('setEndDate', null);
                });
            });
            function increaseValue(val) {
                var value = parseInt(document.getElementById(val).value, 10);
                value = isNaN(value) ? 0 : value;
                value++;
                document.getElementById(val).value = value;
                calculateEachTotalPrice();
                calculateTotalPrice();
            }

            function decreaseValue(val) {
                var value = parseInt(document.getElementById(val).value, 10);
                value = isNaN(value) ? 0 : value;
                value < 1 ? value = 1 : '';
                if (value > 1) {
                    value--;
                }
                document.getElementById(val).value = value;
                calculateEachTotalPrice();
                calculateTotalPrice();
            }

            formatPrice();

            function formatCurrency(val) {
                val = val / 1000;
                return val + "K";
            }

            function formatPrice() {
                var val;
                var listTmpPrice = document.getElementsByClassName("tmpPrice");
                var listPrice = document.getElementsByClassName("price-per-day");
                for (var i = 0; i < listPrice.length; i++) {
                    var price = parseInt(listTmpPrice[i].value, 10);
                    val = formatCurrency(price);
                    listPrice[i].innerHTML = val;
                }
            }
        </script>
        <script src="//unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
        <script type="text/javascript" src="js/my-js.js"></script>
        <script type="text/javascript" src="https://momentjs.com/downloads/moment.js"></script>
    </body>
</html>
