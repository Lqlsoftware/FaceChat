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
            url: "/login",
            data: { username: $("#id").val(), password: $("#pw").val() },
            dataType: "json",
            success: function(data) {
                var res = JSON.parse(data);
                self.location.href = "chat.html?token=" + res.token;
            },
            error: function(data) {
                alert(data.get('errMsg'));
            }
        });
    }
}