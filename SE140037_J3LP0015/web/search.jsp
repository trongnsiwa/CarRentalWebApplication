<%-- 
    Document   : search
    Created on : Feb 20, 2021, 10:53:12 PM
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
        <title>Search</title>
    </head>
    <body>
        <style>
            div#search-form{
                overflow-x: hidden; 
                overflow-y: scroll; 
                height: 929px;
                border: 0;
            }


            div#car-items {
                overflow-x: hidden; 
                overflow-y: scroll; 
                height: 929px;
                border: 0;
            }

            form #decrease {
                margin-right: -4px;
                border-radius: 8px 0 0 8px;
            }

            form #increase {
                margin-left: -4px;
                border-radius: 0 8px 8px 0;
            }

            input#number {
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

            .card {
                border: none;
                border-radius: 10px
            }

            .rating, .rating2 {
                float:left;
            }

            /* :not(:checked) is a filter, so that browsers that don’t support :checked don’t 
              follow these rules. Every browser that supports :checked also supports :not(), so
              it doesn’t make the test unnecessarily selective */
            .rating:not(:checked) > input, 
            .rating2:not(:checked) > input{
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

            .rating2:not(:checked) > label {
                float:right;
                width:1em;
                /* padding:0 .1em; */
                overflow:hidden;
                white-space:nowrap;
                cursor:pointer;
                font-size:150%;
                /* line-height:1.2; */
                color:#ddd;
            }

            .rating:not(:checked) > label:before, 
            .rating2:not(:checked) > label:before{
                content: '★ ';
            }

            .rating > input:checked ~ label, 
            .rating2 > input:checked ~ label{
                color: dodgerblue;

            }

            .rating > label:active, 
            .rating2 > label:active{
                position:relative;
                top:2px;
                left:2px;
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
        <div class="content ml-0 p-0 mt-0">
            <div class="content-wrapper m-0 p-0">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-4 shadow-lg border-2 border-primary" id="search-form">
                            <div class="container mt-3">
                                <form action="search" class="d-flex flex-column justify-content-center align-items-center">
                                    <div class="d-flex flex-column">
                                        <div class="form-group">
                                            <label for="txtCarName" class="mr-2">Name:</label>
                                            <input type="text" class="form-control" name="txtCarName" value="${param.txtCarName}" />
                                        </div>
                                        <div class="form-search has-timer">
                                            <label class="home-label">Rental Date:</label>
                                            <div class="d-flex">
                                                <div class="wrap-input home-datetimerange">
                                                    <div class="" style="display: inline-block">
                                                        <input type="hidden" id="rental-date" value="${param.txtRentalDate}"/>
                                                        <div class="input-append date d-flex align-items-center" id="datepicker1" data-date-format="dd/mm/yyyy" data-date-start-date="d">
                                                            <input name="txtRentalDate" class="form-control" 
                                                                   value="" 
                                                                   type="text" readonly="" id="rfdate">
                                                            <span class="add-on ml-2"><i class="fa fa-calendar"></i></span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="home-select1">
                                                    <input type="hidden" id="rental-time" value="${param.txtRentalTime}"/>
                                                    <select name="txtRentalTime" class="form-control ml-3" id="select-rental">
                                                        <option value="0" <c:if test="${param.txtRentalTime eq '0'}">selected</c:if>>00:00 </option>
                                                        <option value="30" <c:if test="${param.txtRentalTime eq '30'}">selected</c:if>>00:30 </option>
                                                        <option value="60" <c:if test="${param.txtRentalTime eq '60'}">selected</c:if>>01:00 </option>
                                                        <option value="90" <c:if test="${param.txtRentalTime eq '90'}">selected</c:if>>01:30 </option>
                                                        <option value="120" <c:if test="${param.txtRentalTime eq '120'}">selected</c:if>>02:00 </option>
                                                        <option value="150" <c:if test="${param.txtRentalTime eq '150'}">selected</c:if>>02:30 </option>
                                                        <option value="180" <c:if test="${param.txtRentalTime eq '180'}">selected</c:if>>03:00 </option>
                                                        <option value="210" <c:if test="${param.txtRentalTime eq '210'}">selected</c:if>>03:30 </option>
                                                        <option value="240" <c:if test="${param.txtRentalTime eq '240'}">selected</c:if>>04:00 </option>
                                                        <option value="270" <c:if test="${param.txtRentalTime eq '270'}">selected</c:if>>04:30 </option>
                                                        <option value="300" <c:if test="${param.txtRentalTime eq '300'}">selected</c:if>>05:00 </option>
                                                        <option value="330" <c:if test="${param.txtRentalTime eq '330'}">selected</c:if>>05:30 </option>
                                                        <option value="360" <c:if test="${param.txtRentalTime eq '360'}">selected</c:if>>06:00 </option>
                                                        <option value="390" <c:if test="${param.txtRentalTime eq '390'}">selected</c:if>>06:30 </option>
                                                        <option value="420" <c:if test="${param.txtRentalTime eq '420'}">selected</c:if>>07:00 </option>
                                                        <option value="450" <c:if test="${param.txtRentalTime eq '450'}">selected</c:if>>07:30 </option>
                                                        <option value="480" <c:if test="${param.txtRentalTime eq '480'}">selected</c:if>>08:00 </option>
                                                        <option value="510" <c:if test="${param.txtRentalTime eq '510'}">selected</c:if>>08:30 </option>
                                                        <option value="540" <c:if test="${param.txtRentalTime eq '540'}">selected</c:if>>09:00 </option>
                                                        <option value="570" <c:if test="${param.txtRentalTime eq '570'}">selected</c:if>>09:30 </option>
                                                        <option value="600" <c:if test="${param.txtRentalTime eq '600'}">selected</c:if>>10:00 </option>
                                                        <option value="630" <c:if test="${param.txtRentalTime eq '630'}">selected</c:if>>10:30 </option>
                                                        <option value="660" <c:if test="${param.txtRentalTime eq '660'}">selected</c:if>>11:00 </option>
                                                        <option value="690" <c:if test="${param.txtRentalTime eq '690'}">selected</c:if>>11:30 </option>
                                                        <option value="720" <c:if test="${param.txtRentalTime eq '720'}">selected</c:if>>12:00 </option>
                                                        <option value="750" <c:if test="${param.txtRentalTime eq '750'}">selected</c:if>>12:30 </option>
                                                        <option value="780" <c:if test="${param.txtRentalTime eq '780'}">selected</c:if>>13:00 </option>
                                                        <option value="810" <c:if test="${param.txtRentalTime eq '810'}">selected</c:if>>13:30 </option>
                                                        <option value="840" <c:if test="${param.txtRentalTime eq '840'}">selected</c:if>>14:00 </option>
                                                        <option value="870" <c:if test="${param.txtRentalTime eq '870'}">selected</c:if>>14:30 </option>
                                                        <option value="900" <c:if test="${param.txtRentalTime eq '900'}">selected</c:if>>15:00 </option>
                                                        <option value="930" <c:if test="${param.txtRentalTime eq '930'}">selected</c:if>>15:30 </option>
                                                        <option value="960" <c:if test="${param.txtRentalTime eq '960'}">selected</c:if>>16:00 </option>
                                                        <option value="990" <c:if test="${param.txtRentalTime eq '990'}">selected</c:if>>16:30 </option>
                                                        <option value="1020" <c:if test="${param.txtRentalTime eq '1020'}">selected</c:if>>17:00 </option>
                                                        <option value="1050" <c:if test="${param.txtRentalTime eq '1050'}">selected</c:if>>17:30 </option>
                                                        <option value="1080" <c:if test="${param.txtRentalTime eq '1080'}">selected</c:if>>18:00 </option>
                                                        <option value="1110" <c:if test="${param.txtRentalTime eq '1110'}">selected</c:if>>18:30 </option>
                                                        <option value="1140" <c:if test="${param.txtRentalTime eq '1140'}">selected</c:if>>19:00 </option>
                                                        <option value="1170" <c:if test="${param.txtRentalTime eq '1170'}">selected</c:if>>19:30 </option>
                                                        <option value="1200" <c:if test="${param.txtRentalTime eq '1200'}">selected</c:if>>20:00 </option>
                                                        <option value="1230" <c:if test="${param.txtRentalTime eq '1230'}">selected</c:if>>20:30 </option>
                                                        <option value="1260" <c:if test="${param.txtRentalTime eq '1260'}">selected</c:if>>21:00 </option>
                                                        <option value="1290" <c:if test="${param.txtRentalTime eq '1290'}">selected</c:if>>21:30 </option>
                                                        <option value="1320" <c:if test="${param.txtRentalTime eq '1320'}">selected</c:if>>22:00 </option>
                                                        <option value="1350" <c:if test="${param.txtRentalTime eq '1350'}">selected</c:if>>22:30 </option>
                                                        <option value="1380" <c:if test="${param.txtRentalTime eq '1380'}">selected</c:if>>23:00 </option>
                                                        <option value="1410" <c:if test="${param.txtRentalTime eq '1410'}">selected</c:if>>23:30 </option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-search has-timer">
                                                <label class="home-label">Return Date:</label>
                                                <div class="d-flex">
                                                    <input type="hidden" id="return-date" value="${param.txtReturnDate}"/>
                                                <div class="input-append date d-flex align-items-center" id="datepicker2" data-date-format="dd/mm/yyyy" data-date-start-date="d">
                                                    <input name="txtReturnDate" class="form-control" 
                                                           value="" 
                                                           type="text" readonly="" id="rtdate">
                                                    <span class="add-on ml-2"><i class="fa fa-calendar"></i></span>
                                                </div>
                                                <div class="home-select">
                                                    <input type="hidden" id="return-time" value="${param.txtReturnTime}"/>
                                                    <select name="txtReturnTime" class="form-control ml-3" id="select-return">
                                                        <option value="0" <c:if test="${param.txtReturnTime eq '0'}">selected</c:if>>00:00 </option>
                                                        <option value="30" <c:if test="${param.txtReturnTime eq '30'}">selected</c:if>>00:30 </option>
                                                        <option value="60" <c:if test="${param.txtReturnTime eq '60'}">selected</c:if>>01:00 </option>
                                                        <option value="90" <c:if test="${param.txtReturnTime eq '90'}">selected</c:if>>01:30 </option>
                                                        <option value="120" <c:if test="${param.txtReturnTime eq '120'}">selected</c:if>>02:00 </option>
                                                        <option value="150" <c:if test="${param.txtReturnTime eq '150'}">selected</c:if>>02:30 </option>
                                                        <option value="180" <c:if test="${param.txtReturnTime eq '180'}">selected</c:if>>03:00 </option>
                                                        <option value="210" <c:if test="${param.txtReturnTime eq '210'}">selected</c:if>>03:30 </option>
                                                        <option value="240" <c:if test="${param.txtReturnTime eq '240'}">selected</c:if>>04:00 </option>
                                                        <option value="270" <c:if test="${param.txtReturnTime eq '270'}">selected</c:if>>04:30 </option>
                                                        <option value="300" <c:if test="${param.txtReturnTime eq '300'}">selected</c:if>>05:00 </option>
                                                        <option value="330" <c:if test="${param.txtReturnTime eq '330'}">selected</c:if>>05:30 </option>
                                                        <option value="360" <c:if test="${param.txtReturnTime eq '360'}">selected</c:if>>06:00 </option>
                                                        <option value="390" <c:if test="${param.txtReturnTime eq '390'}">selected</c:if>>06:30 </option>
                                                        <option value="420" <c:if test="${param.txtReturnTime eq '420'}">selected</c:if>>07:00 </option>
                                                        <option value="450" <c:if test="${param.txtReturnTime eq '450'}">selected</c:if>>07:30 </option>
                                                        <option value="480" <c:if test="${param.txtReturnTime eq '480'}">selected</c:if>>08:00 </option>
                                                        <option value="510" <c:if test="${param.txtReturnTime eq '510'}">selected</c:if>>08:30 </option>
                                                        <option value="540" <c:if test="${param.txtReturnTime eq '540'}">selected</c:if>>09:00 </option>
                                                        <option value="570" <c:if test="${param.txtReturnTime eq '570'}">selected</c:if>>09:30 </option>
                                                        <option value="600" <c:if test="${param.txtReturnTime eq '600'}">selected</c:if>>10:00 </option>
                                                        <option value="630" <c:if test="${param.txtReturnTime eq '630'}">selected</c:if>>10:30 </option>
                                                        <option value="660" <c:if test="${param.txtReturnTime eq '660'}">selected</c:if>>11:00 </option>
                                                        <option value="690" <c:if test="${param.txtReturnTime eq '690'}">selected</c:if>>11:30 </option>
                                                        <option value="720" <c:if test="${param.txtReturnTime eq '720'}">selected</c:if>>12:00 </option>
                                                        <option value="750" <c:if test="${param.txtReturnTime eq '750'}">selected</c:if>>12:30 </option>
                                                        <option value="780" <c:if test="${param.txtReturnTime eq '780'}">selected</c:if>>13:00 </option>
                                                        <option value="810" <c:if test="${param.txtReturnTime eq '810'}">selected</c:if>>13:30 </option>
                                                        <option value="840" <c:if test="${param.txtReturnTime eq '840'}">selected</c:if>>14:00 </option>
                                                        <option value="870" <c:if test="${param.txtReturnTime eq '870'}">selected</c:if>>14:30 </option>
                                                        <option value="900" <c:if test="${param.txtReturnTime eq '900'}">selected</c:if>>15:00 </option>
                                                        <option value="930" <c:if test="${param.txtReturnTime eq '930'}">selected</c:if>>15:30 </option>
                                                        <option value="960" <c:if test="${param.txtReturnTime eq '960'}">selected</c:if>>16:00 </option>
                                                        <option value="990" <c:if test="${param.txtReturnTime eq '990'}">selected</c:if>>16:30 </option>
                                                        <option value="1020" <c:if test="${param.txtReturnTime eq '1020'}">selected</c:if>>17:00 </option>
                                                        <option value="1050" <c:if test="${param.txtReturnTime eq '1050'}">selected</c:if>>17:30 </option>
                                                        <option value="1080" <c:if test="${param.txtReturnTime eq '1080'}">selected</c:if>>18:00 </option>
                                                        <option value="1110" <c:if test="${param.txtReturnTime eq '1110'}">selected</c:if>>18:30 </option>
                                                        <option value="1140" <c:if test="${param.txtReturnTime eq '1140'}">selected</c:if>>19:00 </option>
                                                        <option value="1170" <c:if test="${param.txtReturnTime eq '1170'}">selected</c:if>>19:30 </option>
                                                        <option value="1200" <c:if test="${param.txtReturnTime eq '1200'}">selected</c:if>>20:00 </option>
                                                        <option value="1230" <c:if test="${param.txtReturnTime eq '1230'}">selected</c:if>>20:30 </option>
                                                        <option value="1260" <c:if test="${param.txtReturnTime eq '1260'}">selected</c:if>>21:00 </option>
                                                        <option value="1290" <c:if test="${param.txtReturnTime eq '1290'}">selected</c:if>>21:30 </option>
                                                        <option value="1320" <c:if test="${param.txtReturnTime eq '1320'}">selected</c:if>>22:00 </option>
                                                        <option value="1350" <c:if test="${param.txtReturnTime eq '1350'}">selected</c:if>>22:30 </option>
                                                        <option value="1380" <c:if test="${param.txtReturnTime eq '1380'}">selected</c:if>>23:00 </option>
                                                        <option value="1410" <c:if test="${param.txtReturnTime eq '1410'}">selected</c:if>>23:30 </option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group d-flex mt-4">
                                                <label for="txtAmountOfCar" class="mr-2">
                                                    Amount Of Car:
                                                </label>
                                                <div class="btn btn-outline-secondary" id="decrease" onclick="decreaseValue()" value="Decrease Value" style="cursor: pointer;">-</div>
                                                <input type="number" class="form-control mx-2" id="number" name="txtAmountOfCar" value="<c:if test="${not empty param.txtAmountOfCar}">${param.txtAmountOfCar}</c:if><c:if test="empty param">0</c:if>" readonly=""/>
                                                <div class="btn btn-outline-secondary" id="increase" onclick="increaseValue()" value="Increase Value" style="cursor: pointer;">+</div>
                                            </div>
                                            <div>
                                                <span>Type:</span>
                                                <div class="vehicle-type">
                                                <c:set var="typeList" value="${requestScope.CAR_TYPE_LIST}"/>
                                                <div class="row">
                                                    <c:forEach var="type" items="${typeList}">
                                                        <div class="col-md-4">
                                                            <div class="squaredFour have-label">
                                                                <input type="checkbox" class="radio" name="txtCarType" id="${type.typeId}" value="${type.typeId}" <c:if test="${param.txtCarType eq type.typeId}">checked="checked"</c:if>>
                                                                <label class="description" for="${type.typeId}">
                                                                    <div class="thumbnail" style="padding: 0.5rem;">
                                                                        <img src="${type.imageLink}" class="img-fluid">
                                                                    </div>
                                                                    <span>${type.seat} (${type.typeName})</span>
                                                                </label>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                            <div class="fn-search d-flex justify-content-center mt-3 mb-5 w-100" style="width: 100px;">
                                                <input type="hidden" name="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                                <button type="submit" class="btn btn-primary"><i class="fa fa-search mr-2"></i>Search</button>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="col-md-8 shadow-lg border-2 border-primary" id="car-items">
                            <div class="container-fluid mt-5 mb-3">
                                <div class="row w-100 d-flex justify-content-end mb-3">
                                    <div class="d-flex flex-row">
                                        <form action="search">
                                            <c:set var="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                            <select name="pageSize">
                                                <option value="all">Always show all</option>
                                                <c:forEach var="size" begin="1" end="20" step="1">
                                                    <option value="${size}" class="form-control" <c:if test="${pageSize == size}">selected="selected"</c:if>>${size}</option>
                                                </c:forEach>
                                            </select>
                                            <input type="hidden" name="txtCarName" value="${param.txtCarName}"/>
                                            <input type="hidden" name="txtRentalDate" value="${param.txtRentalDate}"/>
                                            <input type="hidden" name="txtRentalTime" value="${param.txtRentalTime}"/>
                                            <input type="hidden" name="txtReturnDate" value="${param.txtReturnDate}"/>
                                            <input type="hidden" name="txtReturnTime" value="${param.txtReturnTime}"/>
                                            <input type="hidden" name="txtAmountOfCar" value="${param.txtAmountOfCar}"/>
                                            <input type="hidden" name="txtCarType" value="${param.txtCarType}"/>
                                            <input type="submit" class="btn btn-outline-primary ml-2" value="Change size"/>
                                        </form>
                                    </div>
                                </div>
                                <div class="row g-2">
                                    <c:set var="items" value="${requestScope.CAR_ITEMS}"/>
                                    <c:if test="${not empty items}">
                                        <c:forEach var="item" items="${items}">
                                            <c:url var="viewDetailLink" value="view-detail">
                                                <c:param name="id" value="${item.carModelId}"/>
                                                <c:param name="goBackPage" value="search"/>
                                                <c:param name="txtCarName" value="${param.txtCarName}"/>
                                                <c:param name="txtRentalDate" value="${param.txtRentalDate}"/>
                                                <c:param name="txtRentalTime" value="${param.txtRentalTime}"/>
                                                <c:param name="txtReturnDate" value="${param.txtReturnDate}"/>
                                                <c:param name="txtReturnTime" value="${param.txtReturnTime}"/>
                                                <c:param name="txtAmountOfCar" value="${param.txtAmountOfCar}"/>
                                                <c:param name="txtCarType" value="${param.txtCarType}"/>
                                                <c:param name="pageNo" value="${requestScope.PAGE_NO}"/>
                                                <c:param name="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                            </c:url>
                                            <div class="col-md-6">
                                                <a href="${viewDetailLink}" style="outline: 0; text-decoration: none; display: block; -webkit-tap-highlight-color: rgba(0,0,0,.2)!important; color: #141414;
                                                   cursor: pointer;">
                                                    <div class="card shadow-lg m-3">
                                                        <div class="img-container">
                                                            <img src="${item.smallImageLink}" class="img-fluid">
                                                        </div>
                                                        <div class="car-detail-container p-3">

                                                            <div class="d-flex justify-content-between align-items-center mb-3">
                                                                <input type="hidden" class="tmpPrice" value="${item.price}"/>
                                                                <h4 class="mb-0"><span class="font-weight-bold">${item.carName}</span></h4> <h4><span class="text-primary  price">${item.price}</span></h4>
                                                            </div>
                                                            <div class="d-flex">
                                                                <p>${item.address}</p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </a>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    <div class="row mt-3">
                                        <!-- Paging -->
                                        <div class="d-flex w-100 justify-content-center mt-3 mb-4">
                                            <c:if test="${not empty requestScope.PAGE_NO}">
                                                <c:set var="pageNo" value="${requestScope.PAGE_NO}" />
                                                <c:if test="${pageNo != 1}">
                                                    <c:url var="prevLink" value="search">
                                                        <c:param name="txtCarName" value="${param.txtCarName}"/>
                                                        <c:param name="txtRentalDate" value="${param.txtRentalDate}"/>
                                                        <c:param name="txtRentalTime" value="${param.txtRentalTime}"/>
                                                        <c:param name="txtReturnDate" value="${param.txtReturnDate}"/>
                                                        <c:param name="txtReturnTime" value="${param.txtReturnTime}"/>
                                                        <c:param name="txtAmountOfCar" value="${param.txtAmountOfCar}"/>
                                                        <c:param name="txtCarType" value="${param.txtCarType}"/>
                                                        <c:param name="pageNo" value="${pageNo}"/>
                                                        <c:param name="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                                        <c:param name="action" value="Prev" />
                                                    </c:url>
                                                    <a href="${prevLink}" class="btn btn-primary">Prev</a>
                                                </c:if>
                                                <input type="number" class="form-control m-0 mx-2 text-center" min="1" value="${pageNo}" style="width: 50px;" readonly="readonly"/>
                                                <c:if test="${empty requestScope.LAST_LIST}">
                                                    <c:url var="nextLink" value="search">
                                                        <c:param name="txtCarName" value="${param.txtCarName}"/>
                                                        <c:param name="txtRentalDate" value="${param.txtRentalDate}"/>
                                                        <c:param name="txtRentalTime" value="${param.txtRentalTime}"/>
                                                        <c:param name="txtReturnDate" value="${param.txtReturnDate}"/>
                                                        <c:param name="txtReturnTime" value="${param.txtReturnTime}"/>
                                                        <c:param name="txtAmountOfCar" value="${param.txtAmountOfCar}"/>
                                                        <c:param name="txtCarType" value="${param.txtCarType}"/>
                                                        <c:param name="pageNo" value="${pageNo}"/>
                                                        <c:param name="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                                        <c:param name="action" value="Next" />
                                                    </c:url>
                                                    <a href="${nextLink}" class="btn btn-primary">Next</a>
                                                </c:if>
                                            </c:if>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${empty items || items == null}">
                                    <div class="row w-100 d-flex justify-content-center mt-3">
                                        <h5>
                                            <font color="red">
                                            No Result Was Found
                                            </font>
                                        </h5>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <c:set var="itemDetail" value="${requestScope.ITEM_DETAIL}"/>
        <input type="hidden" id="item-detail" value="${itemDetail}"/>
        <input type="hidden" id="add-to-cart-fail" value="${requestScope.ADD_TO_CART_FAIL}"/>
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
                                    <div class="col-md-7">
                                        <div class="content-detail bg-white shadow-lg ">
                                            <div class="info-car--desc no-pd cover-car">
                                                <div class="car-img">
                                                    <div class="fix-img">
                                                        <img src="${itemDetail.smallImageLink}" alt="Rental Car"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="content-detail mt-3">
                                            <div class="info-car--desc">
                                                <div class="group p-5 my-3 d-flex bg-white shadow-lg border-secondary border-2">
                                                    <span class="lstitle-new text-primary mr-5 font-weight-bold">Characteristics: </span>
                                                    <div class="ctn-desc-new">
                                                        <ul class="features">
                                                            <li class="mb-3">
                                                                <i class="ic ic-chair"></i>
                                                                Seats: ${itemDetail.seat}
                                                            </li>
                                                            <li>
                                                                <i class="ic ic-diesel"></i>
                                                                Fuel: ${itemDetail.fuel}
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </div>
                                                <div class="group p-5 my-3 d-flex bg-white shadow-lg border-secondary border-2">
                                                    <span class="lstitle-new text-primary mr-5 font-weight-bold">Description:</span>
                                                    <div class="ctn-desc-new">
                                                        <pre>${itemDetail.description}</pre>
                                                    </div>
                                                </div>
                                                <div class="group p-5 my-3 d-flex bg-white shadow-lg border-secondary border-2">
                                                    <span class="lstitle-new mr-5 text-primary font-weight-bold">Features:</span>
                                                    <div class="ctn-desc-new">
                                                        <ul class="accessories">
                                                            <c:forEach var="feature" items="${itemDetail.featureList}">
                                                                <li class="mb-3">
                                                                    ${feature.featureName}
                                                                </li>
                                                            </c:forEach>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="info-car--desc">
                                                <div class="review p-5 my-3 bg-white shadow-lg border-secondary border-2">
                                                    <h4 class="title font-weight-bold">FEEDBACK</h4>
                                                    <c:set var="feedbackList" value="${itemDetail.feedbackList}" />
                                                    <c:set var="avrRating" value="${requestScope.AVERAGE_RATING}"/>
                                                    <div class="group-review d-flex align-items-center">
                                                        <div class="rating">
                                                            <input type="radio" id="star10" name="rating_${itemDetail.carModelId}" value="10" <c:if test="${avrRating == 10}">checked="checked"</c:if> /><label for="star10" title="10">10 stars</label>
                                                            <input type="radio" id="star9" name="rating_${itemDetail.carModelId}" value="9" <c:if test="${avrRating == 9}">checked="checked"</c:if> /><label for="star9" title="9">9 stars</label>
                                                            <input type="radio" id="star8" name="rating_${itemDetail.carModelId}" value="8" <c:if test="${avrRating == 8}">checked="checked"</c:if> /><label for="star8" title="8">8 stars</label>
                                                            <input type="radio" id="star7" name="rating_${itemDetail.carModelId}" value="7" <c:if test="${avrRating == 7}">checked="checked"</c:if> /><label for="star7" title="7">7 stars</label>
                                                            <input type="radio" id="star6" name="rating_${itemDetail.carModelId}" value="6" <c:if test="${avrRating == 6}">checked="checked"</c:if> /><label for="star6" title="6">6 star</label>
                                                            <input type="radio" id="star5" name="rating_${itemDetail.carModelId}" value="5" <c:if test="${avrRating == 5}">checked="checked"</c:if> /><label for="star5" title="5">5 stars</label>
                                                            <input type="radio" id="star4" name="rating_${itemDetail.carModelId}" value="4" <c:if test="${avrRating == 4}">checked="checked"</c:if> /><label for="star4" title="4">4 stars</label>
                                                            <input type="radio" id="star3" name="rating_${itemDetail.carModelId}" value="3" <c:if test="${avrRating == 3}">checked="checked"</c:if> /><label for="star3" title="3">3 stars</label> 
                                                            <input type="radio" id="star2" name="rating_${itemDetail.carModelId}" value="2" <c:if test="${avrRating == 2}">checked="checked"</c:if> /><label for="star2" title="2">2 stars</label>
                                                            <input type="radio" id="star1" name="rating_${itemDetail.carModelId}" value="1" <c:if test="${avrRating == 1}">checked="checked"</c:if> /><label for="star1" title="1">1 star</label>
                                                            </div>
                                                            <div class="bar-line mx-3"></div>
                                                        <c:if test="${not empty feedbackList and feedbackList != null}">
                                                            <p><span class="value small">${requestScope.COUNT_FEEDBACK} feedbacks</span></p>
                                                        </c:if>
                                                        <c:if test="${empty feedbackList || feedbackList == null}">
                                                            <p>No reviews yet</p>
                                                        </c:if>
                                                    </div>
                                                    <hr class="line">
                                                    <c:if test="${not empty feedbackList and feedbackList != null}">
                                                        <div class="ratingline" id="ratingline">
                                                            <c:forEach var="feedback" items="${feedbackList}"> 
                                                                <t style="display:none">
                                                                    <div class="list-comments">
                                                                        <div class="left">
                                                                            <div class="fix-avatar">
                                                                                <img src="image/user.png" alt="User">
                                                                            </div>
                                                                        </div>
                                                                        <div class="right">
                                                                            <h6 class="name">${feedback.nameOfUser}</h6>
                                                                            <div class="cmt-box d-flex flex-column">
                                                                                <div class="group d-flex justify-content-between align-items-center">
                                                                                    <div class="rating2">
                                                                                        <input type="radio" id="star10_${feedback.orderId}" name="rating_${feedback.orderId}" value="10" <c:if test="${feedback.rating == 10}">checked="checked"</c:if>/><label for="star10_${feedback.orderId}" title="10">10 stars</label>
                                                                                        <input type="radio" id="star9_${feedback.orderId}" name="rating_${feedback.orderId}" value="9" <c:if test="${feedback.rating == 9}">checked="checked"</c:if> /><label for="star9_${feedback.orderId}" title="9">9 stars</label>
                                                                                        <input type="radio" id="star8_${feedback.orderId}" name="rating_${feedback.orderId}" value="8" <c:if test="${feedback.rating == 8}">checked="checked"</c:if> /><label for="star8_${feedback.orderId}" title="8">8 stars</label>
                                                                                        <input type="radio" id="star7_${feedback.orderId}" name="rating_${feedback.orderId}" value="7" <c:if test="${feedback.rating == 7}">checked="checked"</c:if> /><label for="star7_${feedback.orderId}" title="7">7 stars</label>
                                                                                        <input type="radio" id="star6_${feedback.orderId}" name="rating_${feedback.orderId}" value="6" <c:if test="${feedback.rating == 6}">checked="checked"</c:if> /><label for="star6_${feedback.orderId}" title="6">6 star</label>
                                                                                        <input type="radio" id="star5_${feedback.orderId}" name="rating_${feedback.orderId}" value="5" <c:if test="${feedback.rating == 5}">checked="checked"</c:if> /><label for="star5_${feedback.orderId}" title="5">5 stars</label>
                                                                                        <input type="radio" id="star4_${feedback.orderId}" name="rating_${feedback.orderId}" value="4" <c:if test="${feedback.rating == 4}">checked="checked"</c:if> /><label for="star4_${feedback.orderId}" title="4">4 stars</label>
                                                                                        <input type="radio" id="star3_${feedback.orderId}" name="rating_${feedback.orderId}" value="3" <c:if test="${feedback.rating == 3}">checked="checked"</c:if> /><label for="star3_${feedback.orderId}" title="3">3 stars</label> 
                                                                                        <input type="radio" id="star2_${feedback.orderId}" name="rating_${feedback.orderId}" value="2" <c:if test="${feedback.rating == 2}">checked="checked"</c:if> /><label for="star2_${feedback.orderId}" title="2">2 stars</label>
                                                                                        <input type="radio" id="star1_${feedback.orderId}" name="rating_${feedback.orderId}" value="1" <c:if test="${feedback.rating == 1}">checked="checked"</c:if> /><label for="star1_${feedback.orderId}" title="1">1 star</label>
                                                                                        </div>
                                                                                        <p class="date small">
                                                                                        <c:if test="${feedback.daysSinceCreatedDate > 0}">
                                                                                            ${feedback.daysSinceCreatedDate} days before
                                                                                        </c:if>
                                                                                        <c:if test="${feedback.daysSinceCreatedDate == 0}">
                                                                                            Today
                                                                                        </c:if>
                                                                                    </p>
                                                                                </div>
                                                                                <p class="desc mt-3">${feedback.feedback}</p>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </t>
                                                            </c:forEach>
                                                        </div>
                                                        <div class="s-more"><button id="see-more">See More <i class="fa fa-chevron-down"></i></button></div>
                                                            </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-5">
                                        <div class="sidebar-detail shadow-lg bg-white border-2 border-secondary p-5">
                                            <form action="add-to-cart" method="POST" onsubmit="return checkLogin();">
                                                <div class="rent-box rent-car" id="booking-sidebar">
                                                    <div class="price-per-day d-flex justify-content-center mb-5">
                                                        <input type="hidden" class="tmpPrice" value="${itemDetail.price}"/>
                                                        <h3 class="text-success"><span class="price">${itemDetail.price}</span>
                                                            <span> / days</span></h3>
                                                    </div>
                                                    <div class="form-search has-timer">
                                                        <label class="home-label">Rental Date:</label>
                                                        <div class="d-flex">
                                                            <div class="wrap-input home-datetimerange">
                                                                <div class="" style="display: inline-block">
                                                                    <div class="input-append date d-flex align-items-center" id="datepicker3" data-date-format="dd/mm/yyyy" data-date-start-date="d">
                                                                        <input name="txtUserRentalDate" class="form-control" id="fdate" 
                                                                               value="" 
                                                                               type="text" readonly="">
                                                                        <span class="add-on ml-2"><i class="fa fa-calendar"></i></span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="home-select">
                                                                <select name="txtUserRentalTime" class="form-control ml-3" id="user-select-rental">
                                                                    <option value="0">00:00 </option>
                                                                    <option value="30">00:30 </option>
                                                                    <option value="60">01:00 </option>
                                                                    <option value="90">01:30 </option>
                                                                    <option value="120">02:00 </option>
                                                                    <option value="150">02:30 </option>
                                                                    <option value="180">03:00 </option>
                                                                    <option value="210">03:30 </option>
                                                                    <option value="240">04:00 </option>
                                                                    <option value="270">04:30 </option>
                                                                    <option value="300">05:00 </option>
                                                                    <option value="330">05:30 </option>
                                                                    <option value="360">06:00 </option>
                                                                    <option value="390">06:30 </option>
                                                                    <option value="420">07:00 </option>
                                                                    <option value="450">07:30 </option>
                                                                    <option value="480">08:00 </option>
                                                                    <option value="510">08:30 </option>
                                                                    <option value="540">09:00 </option>
                                                                    <option value="570">09:30 </option>
                                                                    <option value="600">10:00 </option>
                                                                    <option value="630">10:30 </option>
                                                                    <option value="660">11:00 </option>
                                                                    <option value="690">11:30 </option>
                                                                    <option value="720">12:00 </option>
                                                                    <option value="750">12:30 </option>
                                                                    <option value="780">13:00 </option>
                                                                    <option value="810">13:30 </option>
                                                                    <option value="840">14:00 </option>
                                                                    <option value="870">14:30 </option>
                                                                    <option value="900">15:00 </option>
                                                                    <option value="930">15:30 </option>
                                                                    <option value="960">16:00 </option>
                                                                    <option value="990">16:30 </option>
                                                                    <option value="1020">17:00 </option>
                                                                    <option value="1050">17:30 </option>
                                                                    <option value="1080">18:00 </option>
                                                                    <option value="1110">18:30 </option>
                                                                    <option value="1140">19:00 </option>
                                                                    <option value="1170">19:30 </option>
                                                                    <option value="1200">20:00 </option>
                                                                    <option value="1230">20:30 </option>
                                                                    <option value="1260">21:00 </option>
                                                                    <option value="1290">21:30 </option>
                                                                    <option value="1320">22:00 </option>
                                                                    <option value="1350">22:30 </option>
                                                                    <option value="1380">23:00 </option>
                                                                    <option value="1410">23:30 </option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="form-search has-timer">
                                                        <label class="home-label">Return Date:</label>
                                                        <div class="d-flex">
                                                            <div class="input-append date d-flex align-items-center" id="datepicker4" data-date-format="dd/mm/yyyy" data-date-start-date="d">
                                                                <input name="txtUserReturnDate" class="form-control" id="tdate" 
                                                                       value="" 
                                                                       type="text" readonly="">
                                                                <span class="add-on ml-2"><i class="fa fa-calendar"></i></span>
                                                            </div>
                                                            <div class="home-select">
                                                                <select name="txtUserReturnTime" class="form-control ml-3" id="user-select-return">
                                                                    <option value="0">00:00 </option>
                                                                    <option value="30">00:30 </option>
                                                                    <option value="60">01:00 </option>
                                                                    <option value="90">01:30 </option>
                                                                    <option value="120">02:00 </option>
                                                                    <option value="150">02:30 </option>
                                                                    <option value="180">03:00 </option>
                                                                    <option value="210">03:30 </option>
                                                                    <option value="240">04:00 </option>
                                                                    <option value="270">04:30 </option>
                                                                    <option value="300">05:00 </option>
                                                                    <option value="330">05:30 </option>
                                                                    <option value="360">06:00 </option>
                                                                    <option value="390">06:30 </option>
                                                                    <option value="420">07:00 </option>
                                                                    <option value="450">07:30 </option>
                                                                    <option value="480">08:00 </option>
                                                                    <option value="510">08:30 </option>
                                                                    <option value="540">09:00 </option>
                                                                    <option value="570">09:30 </option>
                                                                    <option value="600">10:00 </option>
                                                                    <option value="630">10:30 </option>
                                                                    <option value="660">11:00 </option>
                                                                    <option value="690">11:30 </option>
                                                                    <option value="720">12:00 </option>
                                                                    <option value="750">12:30 </option>
                                                                    <option value="780">13:00 </option>
                                                                    <option value="810">13:30 </option>
                                                                    <option value="840">14:00 </option>
                                                                    <option value="870">14:30 </option>
                                                                    <option value="900">15:00 </option>
                                                                    <option value="930">15:30 </option>
                                                                    <option value="960">16:00 </option>
                                                                    <option value="990">16:30 </option>
                                                                    <option value="1020">17:00 </option>
                                                                    <option value="1050">17:30 </option>
                                                                    <option value="1080">18:00 </option>
                                                                    <option value="1110">18:30 </option>
                                                                    <option value="1140">19:00 </option>
                                                                    <option value="1170">19:30 </option>
                                                                    <option value="1200">20:00 </option>
                                                                    <option value="1230">20:30 </option>
                                                                    <option value="1260">21:00 </option>
                                                                    <option value="1290">21:30 </option>
                                                                    <option value="1320">22:00 </option>
                                                                    <option value="1350">22:30 </option>
                                                                    <option value="1380">23:00 </option>
                                                                    <option value="1410">23:30 </option>
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
                                                                    <span class="price">${itemDetail.price}</span> / days
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
                                                            <c:if test="${sessionScope.ROLE ne 'Admin'}">
                                                                <div class="d-flex justify-content-center mt-3">
                                                                    <input type="hidden" id="user" value="${sessionScope.USER}"/>
                                                                    <input type="hidden" name="id" value="${itemDetail.carModelId}"/>
                                                                    <input type="hidden" name="goBackPage" value="search"/>
                                                                    <input type="hidden" name="txtCarName" value="${param.txtCarName}"/>
                                                                    <input type="hidden" name="txtRentalDate" value="${param.txtRentalDate}"/>
                                                                    <input type="hidden" name="txtRentalTime" value="${param.txtRentalTime}"/>
                                                                    <input type="hidden" name="txtReturnDate" value="${param.txtReturnDate}"/>
                                                                    <input type="hidden" name="txtReturnTime" value="${param.txtReturnTime}"/>
                                                                    <input type="hidden" name="txtAmountOfCar" value="${param.txtAmountOfCar}"/>
                                                                    <input type="hidden" name="txtCarType" value="${param.txtCarType}"/>
                                                                    <input type="hidden" name="pageNo" value="${requestScope.PAGE_NO}"/>
                                                                    <input type="hidden" name="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                                                    <input type="submit" class="btn btn-outline-primary w-100" value="Add To Cart" id="btn-add-to-cart" <c:if test="${itemDetail.quantity == 0}">disabled=""</c:if>/>
                                                                    </div>
                                                            </c:if>
                                                            <div class="space m"></div>
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
            </div>
        </c:if>
        <script type="text/javascript">
            $(':radio:not(:checked)').attr('disabled', true);

            $("input:checkbox").on('click', function () {
                var $box = $(this);
                if ($box.is(":checked")) {
                    var group = "input:checkbox[name='" + $box.attr("name") + "']";
                    $(group).prop("checked", false);
                    $box.prop("checked", true);
                } else {
                    $box.prop("checked", false);
                }
            });

            window.onload = function () {
                formatPrice();

                var itemDetail = document.getElementById("item-detail").value;
                if (itemDetail !== "") {
                    $('#car-detail-modal').modal('show');
                }

                var failureMsg = document.getElementById("add-to-cart-fail").value;
                if (failureMsg !== '') {
                    swal("Add car failed", failureMsg, "error");
                }
            };


            function checkLogin() {
                var user = document.getElementById("user").value;
                if (user === '') {
                    window.location.replace("http://localhost:8084/SE140037_J3LP0015/login-page");
                    return false;
                }
                return true;
            }

            var rentalDate = document.getElementById("rental-date").value;
            var returnDate = document.getElementById("return-date").value;
            var rentalTime = document.getElementById("rental-time").value;
            var returnTime = document.getElementById("return-time").value;

            var today;
            var tomorrow;
            var time;
            if (rentalDate !== '') {
                var dateParts = rentalDate.split("/");
                today = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]);

                var dateParts2 = returnDate.split("/");
                tomorrow = new Date(+dateParts2[2], dateParts2[1] - 1, +dateParts2[0]);
            } else {
                today = new Date();
                tomorrow = new Date(today);
                tomorrow.setDate(tomorrow.getDate() + 1);
            }

            var arr = new Array();
            $('#select-rental option').each(function () {
                arr.push($(this).val());
            });

            var arr2 = new Array();
            $('#select-return option').each(function () {
                arr2.push($(this).val());
            });

            var d = new Date();

            if ((today.getFullYear() + today.getMonth() + today.getDay()) === (d.getFullYear() + d.getMonth() + d.getDay())) {
                time = d.getHours() * 60 + d.getMinutes();

                for (i = arr.length - 1; i > 0; i--) {
                    if (parseInt(arr[i - 1]) >= time && time <= parseInt(arr[i])) {
                        if (rentalTime !== '') {
                            $('#select-rental').val(parseInt(rentalTime));
                        } else {
                            $('#select-rental').val(arr[i - 1]);
                        }
                    } else {
                        $("#select-rental option[value='" + arr[i - 1] + "']")
                                .attr("disabled", "disabled");
                    }
                }
            }

            if ((tomorrow.getFullYear() + tomorrow.getMonth() + tomorrow.getDay()) === (d.getFullYear() + d.getMonth() + d.getDay())) {
                time = d.getHours() * 60 + d.getMinutes();

                var index;
                for (i = arr2.length - 1; i > 0; i--) {
                    if (parseInt(arr2[i - 1]) >= time && time <= parseInt(arr2[i])) {
                        if (returnTime !== '') {
                            $('#select-return').val(parseInt(returnTime));
                        } else {
                            $('#select-return').val(arr2[i]);
                        }
                        index = i - 1;
                    } else {
                        $("#select-return option[value='" + arr2[i - 1] + "']")
                                .attr("disabled", "disabled");
                    }
                }

                $("#select-return option[value='" + arr2[index] + "']")
                        .attr("disabled", "disabled");
            }

            $("#datepicker1").datepicker({
                autoclose: true,
                todayHighlight: true
            }).datepicker('update', today);

            $('#datepicker1').datepicker({
                format: "DD/MM/YYYY",
                todayHighlight: 'TRUE',
                autoclose: true
            }).on('changeDate', function (ev) {
                var start = $("#rfdate").val();
                var dp1 = start.split("/");
                var startD = new Date(+dp1[2], dp1[1] - 1, +dp1[0]);

                var d1 = new Date();

                if ((startD.getFullYear() + startD.getMonth() + startD.getDay()) === (d1.getFullYear() + d1.getMonth() + d1.getDay())) {
                    var time1 = d1.getHours() * 60 + d1.getMinutes();

                    for (i = arr.length - 1; i > 0; i--) {
                        if (parseInt(arr[i - 1]) >= time1 && time1 <= parseInt(arr[i])) {
                            $('#select-rental').val(arr[i - 1]);
                        } else {
                            $("#select-rental option[value='" + arr[i - 1] + "']")
                                    .attr("disabled", "disabled");
                        }
                    }
                } else {
                    for (i = arr.length - 1; i > 0; i--) {
                        $('#select-rental').val(arr[0]);
                        $("#select-rental option[value='" + arr[i - 1] + "']")
                                .removeAttr("disabled");
                    }
                }
            });

            $("#datepicker2").datepicker({
                autoclose: true,
                todayHighlight: true
            }).datepicker('update', tomorrow);

            $('#datepicker2').datepicker({
                format: "DD/MM/YYYY",
                todayHighlight: 'TRUE',
                autoclose: true
            }).on('changeDate', function (ev) {
                var end = $("#rtdate").val();
                var dp1 = end.split("/");
                var endD = new Date(+dp1[2], dp1[1] - 1, +dp1[0]);

                var d1 = new Date();

                if ((endD.getFullYear() + endD.getMonth() + endD.getDay()) === (d1.getFullYear() + d1.getMonth() + d1.getDay())) {
                    var time1 = d1.getHours() * 60 + d1.getMinutes();
                    var index = 0;
                    for (i = arr2.length - 1; i > 0; i--) {
                        if (parseInt(arr2[i - 1]) >= time1 && time1 <= parseInt(arr2[i])) {
                            $('#select-return').val(arr2[i]);
                            index = i - 1;
                        } else {
                            $("#select-return option[value='" + arr2[i - 1] + "']")
                                    .attr("disabled", "disabled");
                        }
                    }

                    $("#select-return option[value='" + arr2[index] + "']")
                            .attr("disabled", "disabled");
                } else {
                    for (i = arr2.length - 1; i >= 0; i--) {
                        $('#select-return').val(arr[0]);
                        $("#select-return option[value='" + arr2[i - 1] + "']")
                                .removeAttr("disabled");
                    }
                }
            });

            $("#datepicker3").datepicker({
                autoclose: true,
                todayHighlight: true,
                startDate: today,
                endDate: tomorrow
            }).datepicker('update', today);

            $("#datepicker4").datepicker({
                autoclose: true,
                todayHighlight: true,
                startDate: today,
                endDate: tomorrow
            }).datepicker('update', tomorrow);


            $(function () {
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
                            if (rentalTime !== '') {
                                $('#user-select-rental').val(parseInt(rentalTime));
                            } else {
                                $('#user-select-rental').val(arr[i - 1]);
                            }
                        } else {
                            $("#user-select-rental option[value='" + arr[i - 1] + "']")
                                    .attr("disabled", "disabled");
                        }
                    }
                } else {
                    for (i = arr.length - 1; i > 0; i--) {
                        if (rentalTime !== '') {
                            $('#user-select-rental').val(parseInt(rentalTime));
                        } else {
                            $('#user-select-rental').val(arr[0]);
                        }
                        $("#user-select-rental option[value='" + arr[i - 1] + "']")
                                .removeAttr("disabled");
                    }
                }

                if ((endD.getFullYear() + endD.getMonth() + endD.getDay()) === (d1.getFullYear() + d1.getMonth() + d1.getDay())) {
                    var time1 = d1.getHours() * 60 + d1.getMinutes();
                    var index = 0;
                    for (i = arr2.length - 1; i > 0; i--) {
                        if (parseInt(arr2[i - 1]) >= time1 && time1 <= parseInt(arr2[i])) {
                            if (returnTime !== '') {
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
                } else {
                    for (i = arr2.length - 1; i > 0; i--) {
                       if (returnTime !== '') {
                            $('#user-select-return').val(parseInt(returnTime));
                        } else {
                            $('#user-select-return').val(arr2[0]);
                        }
                        $("#user-select-return option[value='" + arr2[i - 1] + "']")
                                .removeAttr("disabled");
                    }
                }

                const diffTime = endD.getTime() - startD.getTime();
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

                var price = document.getElementById("item-detail-price").value;
                var totalPrice = (diffDays + 1) * parseInt(price);

                totalPrice = totalPrice.toLocaleString('it-IT', {style: 'currency', currency: 'VND'});

                document.getElementById("count-day").innerHTML = diffDays + 1;
                document.getElementById("total-rental-price").innerHTML = totalPrice;

                $('#datepicker3').datepicker({
                    format: "DD/MM/YYYY",
                    todayHighlight: 'TRUE',
                    autoclose: true,
                    minDate: 0,
                    maxDate: '+1Y+6M'
                }).on('changeDate', function (ev) {
                    var startDate = new Date(ev.date.valueOf());
                    $('#datepicker4').datepicker('setStartDate', startDate);

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
                        document.getElementById("btn-add-to-cart").disabled = false;
                    } else {
                        document.getElementById("count-day").innerHTML = 0;
                        document.getElementById("total-rental-price").innerHTML = 0;
                        document.getElementById("btn-add-to-cart").disabled = true;
                    }
                }).on('clearDate', function (ev) {
                    $('#datepicker4').datepicker('setStartDate', null);
                });

                $('#datepicker4').datepicker({
                    format: "DD/MM/YYYY",
                    todayHighlight: 'TRUE',
                    autoclose: true,
                    minDate: '0',
                    maxDate: '+1Y+6M'
                }).on('changeDate', function (ev) {
                    var endDate = new Date(ev.date.valueOf());
                    $('#datepicker3').datepicker('setEndDate', endDate);

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
                        document.getElementById("btn-add-to-cart").disabled = false;
                    } else {
                        document.getElementById("count-day").innerHTML = 0;
                        document.getElementById("total-rental-price").innerHTML = 0;
                        document.getElementById("btn-add-to-cart").disabled = true;
                    }
                }).on('clearDate', function (ev) {
                    $('#datepicker3').datepicker('setEndDate', null);
                });
            });

            function increaseValue() {
                var value = parseInt(document.getElementById('number').value, 10);
                value = isNaN(value) ? 0 : value;
                value++;
                document.getElementById('number').value = value;
            }

            function decreaseValue() {
                var value = parseInt(document.getElementById('number').value, 10);
                value = isNaN(value) ? 0 : value;
                value < 1 ? value = 1 : '';
                value--;
                document.getElementById('number').value = value;
            }

            var x = 2;
            var size_listcommentsdiv = document.querySelectorAll("#ratingline t").length;
            showOnly(document.querySelectorAll("#ratingline t"), x);

            document.getElementById("see-more").onclick = function () {
                x = (x + 2 <= size_listcommentsdiv) ? x + 2 : size_listcommentsdiv;
                showOnly(document.querySelectorAll("#ratingline t"), x);

                if (x === size_listcommentsdiv) {
                    document.getElementById("see-more").style.display = "none";
                }
            };

            function showOnly(nodeList, index) {
                for (i = 0; i < nodeList.length; i++) {
                    nodeList[i].style.display = i < index ? "block" : "none";
                }
            }
        </script>
        <script type="text/javascript" src="https://momentjs.com/downloads/moment.js"></script>
        <script src="//unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
        <script type="text/javascript" src="js/my-js.js"></script>
    </body>
</html>
