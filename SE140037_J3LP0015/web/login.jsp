<%-- 
    Document   : login
    Created on : Jan 22, 2021, 11:14:51 PM
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
        <link href="assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/myCSS.css" rel="stylesheet" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Signin</title>
    </head>
    <body>
        <style>
            body {
                background-image: url(./image/login.jpg);
                background-position: top;
                background-color: #fff;
                background-repeat: no-repeat;
                background-size: cover;
                height: 100vh;
            }
        </style>
        <nav class="navbar navbar-expand-lg bg-white navbar-light">
            <a class="navbar-brand" href="home">
                <img src="image/logo.png" class="img-fluid" style="width: 120px; height: 120px;">
            </a>
            <ul class="navbar-nav d-flex flex-row ml-auto mr-5">
                <c:if test="${sessionScope.ROLE ne 'Admin'}">
                    <li class="nav-item ml-3"><a class="nav-link" href="ViewCartController">View Cart <c:if test="${sessionScope.CART != null}">
                                <span class="badge badge-info">${sessionScope.CART.getItems().size()}</span>
                            </c:if></a></li></c:if>
                    <li class="nav-item ml-3"><a class="nav-link" href="login-page">Login</a></li>
                    <li class="nav-item ml-3"><a class="nav-link" href="register-page">Sign Up</a></li>
                </ul>
            </nav>
            <div class="banner">
                <div class="content ml-0 p-0">
                    <div class="content-wrapper">
                        <div class="container d-flex flex-column align-items-center">
                            <div class="card bg-white border-primary shadow-lg w-50">
                                <div class="m-2 mt-3">
                                    <h3 class="text-center p-2 mt-3 mb-0 text-primary">Log in</h3>
                                    <div class="ml-3">
                                    <c:if test="${requestScope.LOGIN_ERROR != null}">
                                        <font color="red">
                                        ${requestScope.LOGIN_ERROR}
                                        </font>
                                    </c:if>
                                    <input type="hidden" id="create-account-msg" value="${requestScope.CREATE_ACCOUNT_SUCCESS}"/>
                                    <input type="hidden" id="verify-account-msg" value="${requestScope.VERIFY_ACCOUNT_SUCCESS}"/>
                                </div>
                            </div>
                            <div class="card-body">
                                <form action="login" method="POST">
                                    <div class="form-group">
                                        <label for="txtEmail">
                                            Email:
                                        </label>
                                        <input type="text" class="form-control" name="txtEmail" value="" />
                                    </div>
                                    <div class="form-group">
                                        <label for="txtPassword">
                                            Password:
                                        </label>
                                        <input type="password" class="form-control" name="txtPassword" value="" />
                                    </div>
                                    <div class="g-recaptcha mt-3 d-flex justify-content-center" data-sitekey="6Lc3oV0aAAAAAJ6mBR9sBfaB9tNxrUrc0JTnXZle"></div>
                                    <div class="d-flex justify-content-center mt-3">
                                        <input type="submit" class="btn btn-primary mb-3 mr-1" value="Login" style="width: 100px; height: 50px;"/>
                                        <input type="reset" class="btn btn-secondary mb-3" value="Reset" style="width: 100px; height: 50px;"/>
                                    </div>
                                    <hr class="my-2">
                                    <div class="d-flex justify-content-center align-items-center w-100 mt-2">
                                        <p>Don't have an account? <a href="register-page">Sign up here</a></p>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            window.onload = function () {
                var msg = document.getElementById("create-account-msg").value;
                if (msg !== '') {
                    swal({
                        title: "Sign up successfully",
                        text: msg,
                        icon: "success"
                    }).then(function () {
                        window.location.replace("http://localhost:8084/SE140037_J3LP0015/login-page");
                    });
                }
                
                var msg2 = document.getElementById("verify-account-msg").value;
                if (msg2 !== '') {
                    swal({
                        title: "Verify account successfully",
                        text: msg2,
                        icon: "success"
                    }).then(function () {
                        window.location.replace("http://localhost:8084/SE140037_J3LP0015/login-page");
                    });
                }
            };
        </script>
        <script src="//unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    </body>
</html>

