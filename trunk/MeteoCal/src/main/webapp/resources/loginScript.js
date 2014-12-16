/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () { 
    $(document).ready(function () {
        var z = $("#loginForm\\:inputEmail").val();
            if (z === '')
                $("#loginForm\\:labelEmail").show();
            else
                $("#loginForm\\:labelEmail").hide();
        $("#loginForm\\:inputPassword").val("");
        $("#loginForm\\:labelPassword").show();
    });

    $("#loginForm\\:inputEmail").keyup(function() {
        var x = $("#loginForm\\:inputEmail").val();
            if (x === '')
                $("#loginForm\\:labelEmail").show();
            else
                $("#loginForm\\:labelEmail").hide();
    });
    
        $("#loginForm\\:inputPassword").keyup(function() {
        var x = $("#loginForm\\:inputPassword").val();
            if (x === '')
                $("#loginForm\\:labelPassword").show();
            else
                $("#loginForm\\:labelPassword").hide();
    });
});