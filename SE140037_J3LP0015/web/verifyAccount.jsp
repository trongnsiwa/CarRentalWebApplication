<%-- 
    Document   : verifyAccount
    Created on : Feb 19, 2021, 3:10:42 PM
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
        <title>Verify Account</title>
    </head>
    <body>
        <c:if test="${sessionScope.FULLNAME != null}">
            <c:redirect url="home" />
        </c:if>
        <c:if test="${sessionScope.USER == null}">
            <c:redirect url="login" />
        </c:if>
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
                    <li class="nav-item ml-3"><a class="nav-link" href="login">Login</a></li>
                    <li class="nav-item ml-3"><a class="nav-link" href="register">Sign Up</a></li>
                </ul>
            </nav>
            <div class="content ml-0 p-0">
                <div class="content-wrapper">
                    <div class="container d-flex flex-column align-items-center">
                        <div>
                            <div class="my-3">
                                <a href="login-page" class="btn btn-secondary">Back to Login</a>
                            </div>
                            <div class="card bg-white border-primary shadow-lg w-100">
                                <div class="card-body">
                                    <div class="m-2 mt-3">
                                        <h3>Please verify your account before login</h3>
                                    <c:if test="${requestScope.LOGIN_ERROR != null}">
                                        <font color="red">
                                        ${requestScope.LOGIN_ERROR}
                                        </font>
                                    </c:if>
                                    <c:if test="${requestScope.VERIFY_ACCOUNT_ERROR != null}">
                                        <font color="red">
                                        ${requestScope.VERIFY_ACCOUNT_ERROR}
                                        </font>
                                    </c:if>
                                </div>
                                <form action="verify-account" method="POST">
                                    <div class="form-group mb-3 mt-3">
                                        <label for="code">
                                            Code: 
                                        </label>
                                        <input type="text" class="form-control" name="code" value="" />
                                    </div>
                                    <input type="submit" class="btn btn-outline-primary mb-3" value="Verify" style="width: 100px;"/>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
