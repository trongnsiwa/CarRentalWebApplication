/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function formatCurrency(val) {
    val = val / 1000;
    return val + "K";
}

function formatPrice() {
    var val;
    var listTmpPrice = document.getElementsByClassName("tmpPrice");
    var listPrice = document.getElementsByClassName("price");
    for (var i = 0; i < listPrice.length; i++) {
        var price = parseInt(listTmpPrice[i].value, 10);
        val = formatCurrency(price);
        listPrice[i].innerHTML = val;
    }
}

function calculateEachTotalPrice() {
    var val = 0;
    var listTotal = document.getElementsByClassName("total");
    var listPrice = document.getElementsByClassName("price");
    var listAmount = document.getElementsByClassName("amount");
    for (var i = 0; i < listTotal.length; i++) {
        var price = parseInt(listPrice[i].value);
        var amount = parseInt(listAmount[i].value, 10);
        val = price * amount;
        val = val.toLocaleString('it-IT', {style: 'currency', currency: 'VND'});
        listTotal[i].innerHTML = val;
    }
}

function calculateTotalAmount() {
    var val = 0;
    var listAmount = document.getElementsByClassName("amount");
    for (var i = 0; i < listAmount.length; i++) {
        val += parseInt(listAmount[i].value, 10);
    }
    document.getElementById("total-amount").innerHTML = val;
}

function calculateTotalPrice() {
    var val = 0;
    var listTotal = document.getElementsByClassName("total");
    var listPrice = document.getElementsByClassName("price");
    var listAmount = document.getElementsByClassName("amount");
    var percent = document.getElementById("discount-percent");

    for (var i = 0; i < listTotal.length; i++) {
        var price = parseInt(listPrice[i].value);
        var amount = parseInt(listAmount[i].value, 10);
        val += price * amount;
    }
    
    var sum = val;

    if (percent.value !== '') {
        val = val.toLocaleString('it-IT', {style: 'currency', currency: 'VND'});
        document.getElementById("old-price").innerHTML = val;
        
        var discountPrice = sum * parseInt(percent.value, 10) / 100;
        sum -= discountPrice;
        discountPrice = discountPrice.toLocaleString('it-IT', {style: 'currency', currency: 'VND'});
        document.getElementById("discount-price").innerHTML = '- ' + discountPrice;
    }

    sum = sum.toLocaleString('it-IT', {style: 'currency', currency: 'VND'});
    document.getElementById("total-price").innerHTML = sum;
}

