<%-- 
    Document   : home
    Created on : Feb 19, 2021, 8:41:53 PM
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
        <title>Home Page</title>
    </head>
    <body>
        <style>
            .content {
                background-image: url(./image/home.jpg);
                background-position: center;
                background-color: #fff;
                background-repeat: no-repeat;
                background-size: cover;
            }

            .banner-home {
                position: relative;
                height: 100vh;
                overflow: hidden;
            }

            .slogan {
                line-height: 50px;
                font-weight: 900;
                text-align: center;
                margin-bottom: 1.5rem;
            }

            .banner-home .container {
                position: absolute;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
            }

            .search {
                width: 100%;
                padding: 25px;
                background: rgba(0,0,0,.35);
                margin-bottom: 3rem;
                border-radius: 3px;
            }

            .wd-search__wrapper {
                width: 100%;
                background: rgba(0,0,0,.7);
                padding: 25px;
                color: #fff;
            }

            .home-select option{
                color: #000;
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
                <div class="banner-home">
                    <div class="container d-flex flex-column align-items-center justify-content-center">
                        <h1 class="slogan" style="color: rgb(75, 77, 82);">CAR RENTAL - EVERY TIME EVERY WHERE</h1>
                        <div class="search">
                            <div class="wd-search">
                                <div class="wd-search__wrapper">
                                    <form action="search" class="d-flex flex-column justify-content-center align-items-center w-100">
                                        <div>
                                            <div class="form-group">
                                                <div class="d-flex flex-row">
                                                    <div class="mr-3 d-flex align-content-center">
                                                        <label for="txtCarName" class="mr-2">Name:</label>
                                                        <input type="text" class="form-control" name="txtCarName" value="" placeholder="Enter car name . . ."/>
                                                    </div>
                                                    <div class="d-flex align-content-center">
                                                        <label for="types" class="mr-2">Type:</label>
                                                        <c:set var="typeList" value="${requestScope.CAR_TYPE_LIST}"/>
                                                        <select id="types" name="txtCarType" class="form-control">
                                                            <option value="">Choose Car Type</option>
                                                            <c:forEach var="type" items="${typeList}">
                                                                <option value="${type.typeId}">${type.seat} seats (${type.typeName})</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-search has-timer">
                                                <label class="home-label">Rental Date:</label>
                                                <div class="d-flex">
                                                    <div class="wrap-input home-datetimerange">
                                                        <div class="" style="display: inline-block">
                                                            <input type="hidden" id="rental-date" value=""/>
                                                            <div class="input-append date d-flex align-items-center" id="datepicker1" data-date-format="dd/mm/yyyy" data-date-start-date="d">
                                                                <input name="txtRentalDate" class="form-control" 
                                                                       value="" 
                                                                       type="text" readonly="" id="rfdate">
                                                                <span class="add-on ml-2"><i class="fa fa-calendar"></i></span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="home-select">
                                                        <select name="txtRentalTime" class="form-control ml-3" id="select-rental">
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
                                                    <input type="hidden" id="return-date" value=""/>
                                                    <div class="input-append date d-flex align-items-center" id="datepicker2" data-date-format="dd/mm/yyyy" data-date-start-date="d">
                                                        <input name="txtReturnDate" class="form-control" 
                                                               value="" 
                                                               type="text" readonly="" id="rtdate">
                                                        <span class="add-on ml-2"><i class="fa fa-calendar"></i></span>
                                                    </div>
                                                    <div class="home-select">
                                                        <select name="txtReturnTime" class="form-control ml-3" id="select-return">
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
                                            <div class="form-group d-flex mt-4">
                                                <label for="txtAmountOfCar" class="mr-2">
                                                    Amount Of Car:
                                                </label>
                                                <div class="btn btn-outline-secondary" id="decrease" onclick="decreaseValue()" value="Decrease Value" style="cursor: pointer;">-</div>
                                                <input type="number" class="form-control mx-2" id="number" name="txtAmountOfCar" value="0" readonly=""/>
                                                <div class="btn btn-outline-secondary" id="increase" onclick="increaseValue()" value="Increase Value" style="cursor: pointer;">+</div>
                                            </div>
                                            <div class="fn-search mt-3 pt-3">
                                                <input type="hidden" name="pageSize" value="${requestScope.PAGE_SIZE}"/>
                                                <input type="hidden" name="pageNo" value="${requestScope.PAGE_NO}"/>
                                                <button type="submit" class="btn btn-primary"><i class="fa fa-search mr-2"></i>Find Car Now</button>
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

        <script type="text/javascript">
            const today = new Date();
            const tomorrow = new Date(today);
            tomorrow.setDate(tomorrow.getDate() + 1);

            var arr = new Array();
            $('#select-rental option').each(function () {
                arr.push($(this).val());
            });
            
            var arr2 = new Array();
            $('#select-return option').each(function () {
                arr2.push($(this).val());
            });

            var time = today.getHours() * 60 + today.getMinutes();

            for (i = arr.length - 1; i > 0; i--) {
                if (parseInt(arr[i - 1]) >= time && time <= parseInt(arr[i])) {
                    $('#select-rental').val(arr[i - 1]);
                } else {
                    $("#select-rental option[value='" + arr[i - 1] + "']")
                            .attr("disabled", "disabled");
                }
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
                            $("#select-return option[value='" + arr2[i-1] + "']")
                                    .attr("disabled", "disabled");
                        }
                    }
                    
                    $("#select-return option[value='" + arr2[index] + "']")
                                    .attr("disabled", "disabled");
                } else {
                    for (i = arr2.length - 1; i >= 0; i--) {
                        $('#select-return').val(arr[0]);
                        $("#select-return option[value='" + arr2[i-1] + "']")
                                .removeAttr("disabled");
                    }
                }
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
        </script>
        <script type="text/javascript" src="https://momentjs.com/downloads/moment.js"></script>
        <script type="text/javascript" src="js/my-js.js"></script>
    </body>
</html>
