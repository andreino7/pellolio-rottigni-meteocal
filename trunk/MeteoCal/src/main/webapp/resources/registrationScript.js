/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () { 
    $(document).ready(function () {
        var x = $("#regForm\\:inputName").val();
            if (x === '')
                $("#regForm\\:labelName").show();
            else
                $("#regForm\\:labelName").hide();
        var y = $("#regForm\\:inputSurname").val();
            if (y === '')
                $("#regForm\\:labelSurname").show();
            else
                $("#regForm\\:labelSurname").hide();
        var z = $("#regForm\\:inputEmail").val();
            if (z === '')
                $("#regForm\\:labelEmail").show();
            else
                $("#regForm\\:labelEmail").hide();
        $("#regForm\\:inputPassword").val("");
        $("#regForm\\:labelPassword").show();
    });
    
    $("#regForm\\:inputName").keyup(function() {
        var x = $("#regForm\\:inputName").val();
            if (x === '')
                $("#regForm\\:labelName").show();
            else
                $("#regForm\\:labelName").hide();
    });
    
    $("#regForm\\:inputSurname").keyup(function() {
        var x = $("#regForm\\:inputSurname").val();
            if (x === '')
                $("#regForm\\:labelSurname").show();
            else
                $("#regForm\\:labelSurname").hide();
    });
    
    $("#regForm\\:inputEmail").keyup(function() {
        var x = $("#regForm\\:inputEmail").val();
            if (x === '')
                $("#regForm\\:labelEmail").show();
            else
                $("#regForm\\:labelEmail").hide();
    });
    
        $("#regForm\\:inputPassword").keyup(function() {
        var x = $("#regForm\\:inputPassword").val();
            if (x === '')
                $("#regForm\\:labelPassword").show();
            else
                $("#regForm\\:labelPassword").hide();
    });
    
    
});