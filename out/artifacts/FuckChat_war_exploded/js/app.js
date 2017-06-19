addEventListener("load", function() {
    setTimeout(hideURLbar, 0);
}, false);

function hideURLbar() {
    window.scrollTo(0, 1);
}

document.ontouchmove = function(e) {
    e.preventDefault();
}

$(document).ready(function() {
    $('#sm').width($('#id').width() + 45);
});

function login() {
    if ($("#id").val() != '' && $("#pw").val() != '') {
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/FuckChat/login",
            data: { "username": $("#id").val(), "password": $("#pw").val() },
            success: function(data) {
                if (data.errMsg == "")
                    self.location.href = "chat.html?token=" + data.token;
                else {
                    $("#id").val("");
                    $("#id").attr("placeholder", data.errMsg);
                }
            },
            dataType: "json"
        });
    }
}