<%-- 
    Document   : history
    Created on : Feb 28, 2021, 7:28:29 PM
    Author     : TrongNS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

    <script src="bootstrap/js/jquery-3.4.1.slim.min.js" type="text/javascript"></script>
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" rel="stylesheet" type="text/css">
    <link href="assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="assets/css/myCSS.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>History</title>

    <body>
        <style>
            .img-sm {
                width: 100px;
                height: 100px;
            }

            .card {
                min-width: 0;
                word-wrap: break-word;
                background-clip: border-box;
            }

            .rating {
                float:left;
            }

            /* :not(:checked) is a filter, so that browsers that don’t support :checked don’t 
              follow these rules. Every browser that supports :checked also supports :not(), so
              it doesn’t make the test unnecessarily selective */
            .rating:not(:checked) > input {
                position:absolute;
                top:-9999px;
                clip:rect(0,0,0,0);
            }

            .rating:not(:checked) > label {
                float:right;
                width:1em;
                /* padding:0 .1em; */
                overflow:hidden;
                white-space:nowrap;
                cursor:pointer;
                font-size:200%;
                /* line-height:1.2; */
                color:#ddd;
            }

            .rating:not(:checked) > label:before {
                content: '★ ';
            }

            .rating > input:checked ~ label {
                color: dodgerblue;

            }

            .rating:not(:checked) > label:hover,
            .rating:not(:checked) > label:hover ~ label {
                color: dodgerblue;

            }

            .rating > input:checked + label:hover,
            .rating > input:checked + label:hover ~ label,
            .rating > input:checked ~ label:hover,
            .rating > input:checked ~ label:hover ~ label,
            .rating > label:hover ~ input:checked ~ label {
                color: dodgerblue;

            }

            .rating > label:active {
                position:relative;
                top:2px;
                left:2px;
            }
        </style>
        <c:if test="${empty sessionScope.FULLNAME}">
            <c:redirect url="home"/>
        </c:if>
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
                            <!-- history -->
                            <li>
                                <a class="dropdown-item" href="history">History</a>
                            </li>
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
                <div class="container">
                    <div class="row align-items-center">
                        <div class="col-xl-12" style="position: relative; height: 100%; padding-bottom: 6rem;">
                            <div class="mb-2">
                                <a href="home" class="btn btn-secondary">Back to Home</a>
                            </div>
                            <div class="card position-relative d-flex flex-column">
                                <div class="table-responsive">
                                    <table class="table table-borderless table-submit" style="border: 2px solid #005086;">
                                        <thead class="text-muted">
                                            <tr>
                                                <th colspan="2" scope="col">
                                                    <div class="text-center pb-5">
                                                        <form action="search-history">
                                                            <div class="w-100 mx-auto d-flex">
                                                                <div class="form-group mb-1 w-100">
                                                                    <i class="fa fa-search position-absolute" style="padding: 10px; min-width: 40px; "></i>
                                                                    <input type="text" name="txtSearchHistory" value="${param.txtSearchHistory}" class="form-control w-100 align-content-center" style="padding-left: 35px;" placeholder="Search">
                                                                </div>
                                                                <div class="ml-3">
                                                                    <input type="hidden" name="pageNo" value="${requestScope.PAGE_NO}" />
                                                                    <input type="hidden" name="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                                                    <button type="submit" class="btn btn-success mr-1" style="width: 100px;">Search</button>
                                                                </div>
                                                            </div>
                                                            <div class="form-group mt-2 d-flex align-items-center justify-content-center">
                                                                <label class="home-label mr-3">Rental Date:</label>
                                                                <div class="d-flex">
                                                                    <div class="wrap-input home-datetimerange">
                                                                        <div class="" style="display: inline-block">
                                                                            <input type="hidden" id="rental-date" value="${param.txtRentalDate}"/>
                                                                            <div class="input-append date d-flex align-items-center" id="datepicker" data-date-format="dd/mm/yyyy">
                                                                                <input name="txtRentalDate" class="form-control" 
                                                                                       value="" 
                                                                                       type="text" readonly="">
                                                                                <span class="add-on ml-2"><i class="fa fa-calendar"></i></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </th>
                                            </tr>
                                            <tr>
                                                <th colspan="2" scope="col">
                                                    <div class="d-flex justify-content-end">
                                                        <form action="search-history">
                                                            <c:set var="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                                            <select name="pageSize">
                                                                <option value="all">Always show all</option>
                                                                <c:forEach var="size" begin="1" end="20" step="1">
                                                                    <option value="${size}" class="form-control" <c:if test="${pageSize == size}">selected="selected"</c:if>>${size}</option>
                                                                </c:forEach>
                                                            </select>
                                                            <input type="hidden" name="txtSearchHistory" value="${param.txtSearchHistory}"/>
                                                            <input type="hidden" name="txtRentalDate" value="${param.txtRentalDate}"/>
                                                            <input type="submit" class="btn btn-primary ml-2" name="action" value="Change size">
                                                        </form>
                                                    </div>
                                                </th>
                                            </tr>
                                            <tr class="small text-uppercase text-white" style="background: #005086;">
                                                <th scope="col">Car</th>
                                                <th scope="col" class="text-right">Subtotal</th>
                                            </tr>
                                        </thead>
                                        <input type="hidden" id="cancel-order-msg" value="${requestScope.CANCEL_ORDER_MESSAGE}"/>
                                        <input type="hidden" id="feedback-msg" value="${requestScope.FEEDBACK_MESSAGE}"/>
                                        <c:set var="orders" value="${requestScope.ORDER_LIST}"/>
                                        <c:if test="${not empty orders}">
                                            <c:forEach var="order" items="${orders}">
                                                <tbody class="bg-white w-100" style="border-bottom: 2px solid #005086;">
                                                    <tr class="small">
                                                        <td> 
                                                            <p>Rental Date: ${order.rentalDate}</p>
                                                        </td> 
                                                        <td class="text-right">
                                                            <p>ORDER ID: <span class="font-italic">${order.orderId}</span></p>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="2">
                                                            <div class="text-right">
                                                                <c:if test="${order.activate eq true}">
                                                                    <c:url var="cancelOrderLink" value="cancel-order">
                                                                        <c:param name="id" value="${order.orderId}"/>
                                                                        <c:param name="txtSearchHistory" value="${param.txtSearchHistory}"/>
                                                                        <c:param name="txtRentalDate" value="${param.txtRentalDate}"/>
                                                                        <c:if test="${not empty requestScope.PAGE_NO}">
                                                                            <c:param name="pageNo" value="${requestScope.PAGE_NO}"/>
                                                                        </c:if>
                                                                        <c:if test="${not empty requestScope.PAGE_SIZE}">
                                                                            <c:param name="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                                                        </c:if>
                                                                    </c:url>
                                                                    <a href="#" class="btn btn-danger" data-abc="true" data-href="${cancelOrderLink}" data-toggle="modal" data-target="#confirm-cancel">Cancel Order</a>
                                                                </c:if>
                                                            </div>
                                                            <hr>
                                                        </td>
                                                    </tr>
                                                    <c:forEach var="item" items="${order.rentingItems}">
                                                        <tr>
                                                            <td>
                                                                <div class="d-flex">
                                                                    <figure class="d-flex pl-1 pr-1 position-relative align-items-center">
                                                                        <c:url var="img" value="${item.imageLink}"/> <!-- image -->
                                                                        <div><img src="${img}" class="img-sm mr-2 img-fluid img-thumbnail" alt="${item.carName}"></div>
                                                                        <figcaption class="pl-1 pr-1"><p class="text-dark b text-body" data-abc="true">${item.carName}</p> <!-- product name -->
                                                                            <p class="text-primary small">x ${item.amount}</p>
                                                                            <p class="text-muted small"><i>From <span class="date">${item.pickUpDate}</span> <br/> To <span class="date">${item.dropOffDate}</span> (${item.days} days)</i></p>
                                                                        </figcaption>
                                                                    </figure>
                                                                    <c:if test="${item.renting eq true and order.activate}">
                                                                        <p class="text-info"><i>* Not Yet *</i></p>
                                                                    </c:if>
                                                                    <c:if test="${item.renting eq true and not order.activate}">
                                                                        <p class="text-info"><i>* Renting *</i></p>
                                                                    </c:if>
                                                                </div>

                                                            </td>
                                                            <td class="text-right"> <!-- price -->
                                                                <input type="hidden" class="tmpPrice" value="${item.subTotal}" />
                                                                <div class="price-wrap"><var class="b"><span class="price" style="font-size: 20px;">${item.subTotal}</span></var></div>
                                                            </td>
                                                        </tr>
                                                        <c:if test="${not item.renting and not order.activate}">
                                                            <tr>
                                                                <td colspan="2">
                                                                    <div class="d-flex flex-column form-group">
                                                                        <form accept-charset="UTF-8" action="send-feedback" method="post" onsubmit="return checkRating('${item.orderId}', '${item.carModelId}');">
                                                                            <textarea class="w-100 p-2" id="new-review" name="feedback_${item.orderId}_${item.carModelId}" placeholder="Enter your feedback here..." rows="3" required="required">${item.feedback}</textarea>
                                                                            <div class="text-right">
                                                                                <div class="rating">
                                                                                    <input type="radio" id="star10_${item.orderId}_${item.carModelId}" name="rating_${item.orderId}_${item.carModelId}" value="10" <c:if test="${item.rating == 10}">checked="checked"</c:if>/><label for="star10_${item.orderId}_${item.carModelId}" title="10">10 stars</label>
                                                                                    <input type="radio" id="star9_${item.orderId}_${item.carModelId}" name="rating_${item.orderId}_${item.carModelId}" value="9" <c:if test="${item.rating == 9}">checked="checked"</c:if>/><label for="star9_${item.orderId}_${item.carModelId}" title="9">9 stars</label>
                                                                                    <input type="radio" id="star8_${item.orderId}_${item.carModelId}" name="rating_${item.orderId}_${item.carModelId}" value="8" <c:if test="${item.rating == 8}">checked="checked"</c:if>/><label for="star8_${item.orderId}_${item.carModelId}" title="8">8 stars</label>
                                                                                    <input type="radio" id="star7_${item.orderId}_${item.carModelId}" name="rating_${item.orderId}_${item.carModelId}" value="7" <c:if test="${item.rating == 7}">checked="checked"</c:if>/><label for="star7_${item.orderId}_${item.carModelId}" title="7">7 stars</label>
                                                                                    <input type="radio" id="star6_${item.orderId}_${item.carModelId}" name="rating_${item.orderId}_${item.carModelId}" value="6" <c:if test="${item.rating == 6}">checked="checked"</c:if>/><label for="star6_${item.orderId}_${item.carModelId}" title="6">6 star</label>
                                                                                    <input type="radio" id="star5_${item.orderId}_${item.carModelId}" name="rating_${item.orderId}_${item.carModelId}" value="5" <c:if test="${item.rating == 5}">checked="checked"</c:if>/><label for="star5_${item.orderId}_${item.carModelId}" title="5">5 stars</label>
                                                                                    <input type="radio" id="star4_${item.orderId}_${item.carModelId}" name="rating_${item.orderId}_${item.carModelId}" value="4" <c:if test="${item.rating == 4}">checked="checked"</c:if>><label for="star4_${item.orderId}_${item.carModelId}" title="4">4 stars</label>
                                                                                    <input type="radio" id="star3_${item.orderId}_${item.carModelId}" name="rating_${item.orderId}_${item.carModelId}" value="3" <c:if test="${item.rating == 3}">checked="checked"</c:if>/><label for="star3_${item.orderId}_${item.carModelId}" title="3">3 stars</label>
                                                                                    <input type="radio" id="star2_${item.orderId}_${item.carModelId}" name="rating_${item.orderId}_${item.carModelId}" value="2" <c:if test="${item.rating == 2}">checked="checked"</c:if>/><label for="star2_${item.orderId}_${item.carModelId}" title="2">2 stars</label>
                                                                                    <input type="radio" id="star1_${item.orderId}_${item.carModelId}" name="rating_${item.orderId}_${item.carModelId}" value="1" <c:if test="${item.rating == 1}">checked="checked"</c:if>/><label for="star1_${item.orderId}_${item.carModelId}" title="1">1 star</label>
                                                                                    </div>
                                                                                    <div class="mt-3">
                                                                                        <input type="hidden" name="orderId" value="${item.orderId}"/>
                                                                                    <input type="hidden" name="carModelId" value="${item.carModelId}"/>
                                                                                    <input type="hidden" name="txtSearchHistory" value="${param.txtSearchHistory}"/>
                                                                                    <input type="hidden" name="txtRentalDate" value="${param.txtRentalDate}"/>
                                                                                    <c:if test="${not empty requestScope.PAGE_NO}">
                                                                                        <input type="hidden" name="pageNo" value="${requestScope.PAGE_NO}"/>
                                                                                    </c:if>
                                                                                    <c:if test="${not empty requestScope.PAGE_SIZE}">
                                                                                        <input type="hidden" name="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                                                                    </c:if>
                                                                                    <input type="reset" class="btn btn-secondary" value="Reset"/>
                                                                                    <button class="btn btn-success" type="submit">Send</button>
                                                                                </div>
                                                                            </div>
                                                                        </form>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>
                                                    <tr>
                                                        <td class="text-right mr-3" colspan="2"> <!-- right side -->
                                                            <hr>
                                                            <div align="right" style="float: right;" class="mb-5">
                                                                <p><span class="text-muted">Discount Code:</span> <span>${order.discountCode}<c:if test="${empty order.discountCode}">NONE</c:if></span></p>
                                                                <input type="hidden" class="tmpPrice" value="${order.totalPrice}" />
                                                                <p><span class="text-muted">Total price:</span> <span class="font-weight-bold text-info price" style="font-size: 25px;">${order.totalPrice}</span></p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </c:forEach>
                                        </table>
                                        <!-- Confirm delete product modal -->
                                        <div class="modal fade in" id="confirm-cancel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-dialog-centered border-danger">
                                                <div class="modal-content p-3">
                                                    <div class="modal-header border-bottom-0 p-3">
                                                        <h3 class="modal-title text-center text-danger">Cancel Order</h3>
                                                    </div>
                                                    <div class="modal-body">
                                                        You are about to cancel this order, you cannot undo this action.<br/>
                                                        <span class="font-weight-bold">Are you sure want to continue?</span>
                                                    </div>
                                                    <div class="modal-footer d-flex justify-content-start border-top-0">
                                                        <a class="btn btn-danger" id="btn-ok">Yes, Cancel It</a>
                                                        <button type="button" class="btn btn-secondary mr-3" data-dismiss="modal">No, Close</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="d-flex w-100 justify-content-center mt-2 mb-4">
                                            <c:if test="${not empty requestScope.PAGE_NO}">
                                                <c:set var="pageNo" value="${requestScope.PAGE_NO}" />
                                                <c:if test="${pageNo != 1}">
                                                    <c:url var="prevLink" value="search-history">
                                                        <c:param name="txtSearchHistory" value="${param.txtSearchHistory}"/>
                                                        <c:param name="txtRentalDate" value="${param.txtRentalDate}"/>
                                                        <c:param name="pageNo" value="${pageNo}"/>
                                                        <c:param name="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                                        <c:param name="action" value="Prev" />
                                                    </c:url>
                                                    <a href="${prevLink}" class="btn btn-primary">Prev</a>
                                                </c:if>
                                                <input type="number" class="form-control m-0 mx-2 text-center" min="1" value="${pageNo}" style="width: 50px;" readonly="readonly"/>
                                                <c:if test="${empty requestScope.LAST_LIST}">
                                                    <c:url var="nextLink" value="search-history">
                                                        <c:param name="txtSearchHistory" value="${param.txtSearchHistory}"/>
                                                        <c:param name="txtRentalDate" value="${param.txtRentalDate}"/>
                                                        <c:param name="pageNo" value="${pageNo}"/>
                                                        <c:param name="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                                        <c:param name="action" value="Next" />
                                                    </c:url>
                                                    <a href="${nextLink}" class="btn btn-primary">Next</a>
                                                </c:if>
                                            </c:if>
                                        </div>
                                    </c:if>
                                    <c:if test="${empty orders}">
                                        <tbody class="bg-white" style="border-bottom: 2px solid #005086;">
                                            <tr>
                                                <td class="text-center py-5" colspan="2">
                                                    <font color="red">
                                                    No result was found
                                                    </font>
                                                </td>
                                            </tr>
                                        </tbody>
                                        </table>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            window.onload = function () {
                var cancelMessage = document.getElementById("cancel-order-msg").value;
                if (cancelMessage !== '') {
                    if (cancelMessage.indexOf('successfully') !== -1) {
                        swal("Success", cancelMessage, "success");
                    } else {
                        swal("Fail", cancelMessage, "error");
                    }
                }

                var feedBackMessage = document.getElementById("feedback-msg").value;
                if (feedBackMessage !== '') {
                    if (feedBackMessage.indexOf('Thank') !== -1) {
                        swal("Success", feedBackMessage, "success");
                    } else {
                        swal("Fail", feedBackMessage, "error");
                    }
                }
            };
            
            $("#datepicker").datepicker({
                autoclose: true,
                todayHighlight: true, 
                clearBtn: true
            });
            
            var rentalDate = document.getElementById("rental-date").value;
            var today;
            if (rentalDate !== '') {
                var dateParts = rentalDate.split("/");
                today = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]);

                $("#datepicker").datepicker({
                    autoclose: true,
                    todayHighlight: true, 
                    clearBtn: true
                }).datepicker('update', today);
            }

            var listTmpPrice2 = document.getElementsByClassName("tmpPrice");
            var listPrice2 = document.getElementsByClassName("price");
            for (var i = 0; i < listPrice2.length; i++) {
                var price2 = parseInt(listTmpPrice2[i].value, 10);
                price2 = price2.toLocaleString('it-IT', {style: 'currency', currency: 'VND'});
                listPrice2[i].innerHTML = price2;
            }

            $('#confirm-cancel').on('show.bs.modal', function (e) {
                $(this).find('#btn-ok').attr('href', $(e.relatedTarget).data('href'));
            });

            function checkRating(orderId, carModelId) {
                if (!$("input[name='rating_" + orderId + "_" + carModelId +"']").is(':checked')) {
                    swal("Please rating 1 - 10 before send feedback!");
                    return false;
                }
                return true;
            }

        </script>
        <script type="text/javascript" src="https://momentjs.com/downloads/moment.js"></script>
        <script src="//unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    </body>
</html>
