<%-- 
    Document   : register
    Created on : Jan 22, 2021, 9:02:04 PM
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
        <title>Register</title>
    </head>
    <body>
        <c:if test="${sessionScope.FULLNAME != null}">
            <c:redirect url="home" />
        </c:if>
        <nav class="navbar navbar-expand-lg navbar-light">
            <a class="navbar-brand ml-5" href="home">
                <img src="image/logo.png" class="img-fluid" style="width: 120px; height: 120px;">
            </a>
            <ul class="navbar-nav d-flex flex-row ml-auto mr-5">
                <c:if test="${sessionScope.ROLE ne 'Admin'}">
                    <li class="nav-item ml-3"><a class="nav-link" href="cart">View Cart <c:if test="${sessionScope.CART != null}">
                                <span class="badge badge-info">${sessionScope.CART.getItems().size()}</span>
                            </c:if></a></li></c:if>
                    <li class="nav-item ml-3"><a class="nav-link" href="login-page">Login</a></li>
                    <li class="nav-item ml-3"><a class="nav-link" href="register-page">Sign Up</a></li>
                </ul>
            </nav>
            <div class="content ml-0 p-0">
                <div class="content-wrapper">
                    <div class="container-fluid mt-4 d-flex flex-column align-items-center" style="position: relative; height: 100%; padding-bottom: 6rem;">
                        <div class="card border-0" style="border-radius: 5px;">
                            <div class="card-body">
                                <div class="text-center text-primary my-3">
                                    <h4>Create New Account</h4>
                                </div>
                            <c:if test="${requestScope.CREATE_ACCOUNT_FAIL != null}">
                                <font color="red">
                                ${requestScope.CREATE_ACCOUNT_FAIL}
                                </font>
                            </c:if>
                            <form action="create-new-account" method="POST">
                                <c:set var="error" value="${requestScope.CREATE_ACCOUNT_ERROR}"/>
                                <div class="form-group mb-0 pb-0 d-flex flex-column">
                                    <label for="txtEmail">
                                        Email:
                                    </label>
                                    <input type="text" class="form-control" name="txtEmail" value="${param.txtEmail}"/><br/>
                                    <div class="mb-3 mt-0 pt-0">
                                        <c:if test="${not empty error.invalidEmail}">
                                            <font color="red">
                                            ${error.invalidEmail}
                                            </font>
                                        </c:if>
                                        <c:if test="${not empty error.duplicateEmail}">
                                            <font color="red">
                                            ${error.duplicateEmail}
                                            </font>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-group mb-0 pb-0 d-flex flex-column">
                                    <label for="txtPassword">
                                        Password:
                                    </label>
                                    <input type="password" name="txtPassword" class="form-control" value=""/><br/>
                                    <div class="mb-3 mt-0 pt-0">
                                        <c:if test="${not empty error.emptyPassword}">
                                            <font color="red">
                                            ${error.emptyPassword}
                                            </font>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-group mb-0 pb-0 d-flex flex-column">
                                    <label for="txtConfirm">
                                        Confirm password:
                                    </label>
                                    <input type="password" name="txtConfirm" class="form-control" value="" placeholder=""/>
                                    <div class="mb-3 mt-0 pt-0">
                                        <c:if test="${not empty error.confirmNotMatchPassword}">
                                            <font color="red">
                                            ${error.confirmNotMatchPassword}
                                            </font>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-group mb-0 pb-0 d-flex flex-column">
                                    <div class="d-flex flex-row justify-content-between mt-5">
                                        <label for="txtFirstName" class="mr-3">
                                            Firstname:
                                        </label>
                                        <input type="text" class="form-control mr-3" name="txtFirstName" value="${param.txtFirstName}" />

                                        <label for="txtLastname" class="mr-3">
                                            Lastname:
                                        </label>
                                        <input type="text" class="form-control" name="txtLastname" value="${param.txtLastname}"/>

                                    </div>
                                    <div class="d-flex flex-row justify-content-between mb-5">
                                        <div class="mb-3 mt-0 pt-0">
                                            <c:if test="${not empty error.emptyFirstName}">
                                                <font color="red">
                                                ${error.emptyFirstName}
                                                </font>
                                            </c:if>
                                        </div>
                                        <div class="mb-3 mt-0 pt-0">
                                            <c:if test="${not empty error.emptyLastName}">
                                                <font color="red">
                                                ${error.emptyLastName}
                                                </font>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group mb-0 pb-0 d-flex flex-column">
                                    <label for="txtPhone">
                                        Phone Number:
                                    </label>
                                    <input type="text" class="form-control" name="txtPhone" value="${param.txtPhone}"/><br/>
                                    <div class="mb-3 mt-0 pt-0">
                                        <c:if test="${not empty error.invalidPhone}">
                                            <font color="red">
                                            ${error.invalidPhone}
                                            </font>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-group d-flex flex-column">
                                    <label for="txtAddress">
                                        Address:
                                    </label>
                                    <input type="text" class="form-control" name="txtAddress" value="${param.txtAddress}" /><br/>
                                    <div class="mb-3 mt-0 pt-0">
                                        <c:if test="${not empty error.emptyAddress}">
                                            <font color="red">
                                            ${error.emptyAddress}
                                            </font>
                                        </c:if>
                                    </div>
                                </div>
                                <hr class="my-4">
                                <div class="d-flex justify-content-center align-items-center w-100 mt-4">
                                    <input type="submit" class="btn btn-primary" value="Register" style="width: 100px; height: 50px;"/>
                                    <input type="reset" class="btn btn-secondary ml-2" value="Reset" style="width: 100px; height: 50px;"/>
                                </div>
                                <div class="d-flex justify-content-center align-items-center w-100 mt-2">
                                    <p>Already have an account? <a href="login-page">Signin</a></p>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
