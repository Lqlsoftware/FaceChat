var isMain = true;

$(document).ready(function() {
    $('#sm').width($('#id').width() + 45);
});

function login() {
    if ($("#id").val() != '' && $("#pw").val() != '') {
        $.ajax({
            type: "POST",
            url: "http://lqlsoftware.top/test/login",
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